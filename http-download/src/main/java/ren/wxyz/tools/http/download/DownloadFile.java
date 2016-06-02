package ren.wxyz.tools.http.download;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * 下载文件
 * </p>
 *
 * @author wxyz
 * @datetime 2016/5/31 23:14
 */
public class DownloadFile {

    /**
     * 下载缓冲区大小
     */
    private static final int DOWNLOAD_BUFFER_SIZE = 1024 * 1024;

    /**
     * 临时文件后缀
     */
    private static final String TMP_FILE_SUFFIX = ".tmp";

    /**
     * 配置类对象
     */
    private Configuration config;

    /**
     * 代理配置
     */
    private HttpHost proxy;

    /**
     * 文件保存根目录
     */
    private File rootPath;

    /**
     * 以给定配置项加载下载对象
     *
     * @param config 配置对象
     */
    public DownloadFile(Configuration config) {
        this.config = config;
        if (config.isProxyEnable()) {
            this.proxy = new HttpHost(config.getProxyHost(), config.getProxyPort(), "http");
        }
    }

    /**
     * 下载
     *
     * @param urls URL列表
     */
    public void download(List<String> urls) {
        File rootPath = new File(config.getOutputFolder());
        if (!rootPath.exists()) {
            System.out.println("创建目录 " + rootPath.getAbsolutePath());
            rootPath.mkdirs();
        }
        this.rootPath = rootPath;

        List<Status> resList = mutiThreadDownload(urls, config.getDownloadThreadNum(), progressListener);

        // 找出网络出错的URL
        List<String> reDownloadList = new ArrayList<>();
        for (Status status : resList) {
            switch (status.getCode()) {
                case Status.OK:
                    System.out.println("ok: " + status.getUrl() + ", time: " + status.getTimeOfSecond());
                    break;
                case Status.HTTP_ERROR:
                    System.out.println("fail: " + status.getUrl() + ", httpCode: " + status.getHttpCode());
                    break;
                case Status.NET_ERROR:
                case Status.SYS_ERROR:
                    System.out.println("fail: " + status.getUrl() + ", msg: " + status.getMsg());
                    reDownloadList.add(status.getUrl());
                    break;
                default:
                    break;
            }
        }

        // 重新下载
        mutiThreadDownload(reDownloadList, config.getDownloadThreadNum(), progressListener);
    }

    /**
     * 多线程下载
     * @param urls URL地址
     * @param ThreadNum 线程数
     * @param progressListener 进度监听器
     * @return 文件状态列表
     */
    private List<Status> mutiThreadDownload(List<String> urls, int ThreadNum, ProgressListener progressListener) {
        // 线程池
        ExecutorService pool = Executors.newFixedThreadPool(ThreadNum);

        // 下载线程句柄列表
        List<Future<Status>> futures = new ArrayList<>();
        // 下载状态列表
        List<Status> resList = new ArrayList<>();

        // 循环多线程下载
        for (final String url : urls) {
            if (StringUtils.isBlank(url)) {
                continue;
            }
            final File saveFile = new File(rootPath, getFileNameFromUrl(url));

            Future<Status> future = pool.submit(() -> download(url, saveFile, progressListener));
            futures.add(future);
        }

        // 等待下载完成
        for (Future<Status> future : futures) {
            try {
                resList.add(future.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        pool.shutdown();

        return resList;
    }

    /**
     * 统一同一个进度监听器
     */
    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void progress(String url, int rate) {
            if (rate % 10 == 0) {
                System.out.println(url + "," + rate);
            }
        }
    };

    /**
     * 下载单个文件
     *
     * @param url      URL地址
     * @param filePath 保存文件路径
     * @param progressListener 进度监听器
     * @return 下载的状态
     */
    private Status download(String url, File filePath, ProgressListener progressListener) {
        Status status = new Status(Status.OK, url, filePath);

        progressListener.progress(url, 0);
        long startTime = System.currentTimeMillis();

        // 创建客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpHost target = HttpHost.create(getRootUrl(url));

            // 构建GET请求
            HttpGet httpGet = new HttpGet(url);
            if (config.isProxyEnable()) {
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpGet.setConfig(config);
            }
            // 执行下载
            try (CloseableHttpResponse resp = httpClient.execute(target, httpGet)) {
                int statusCode = resp.getStatusLine().getStatusCode();
                if (statusCode < 200 || statusCode >= 300) {
                    progressListener.progress(url, -1);
                    status.setCode(Status.HTTP_ERROR, statusCode);

                    status.setTimeOfSecond(System.currentTimeMillis() - startTime);
                    return status;
                }
                status.setHttpCode(statusCode);

                // 文件总长度
                Header[] respHeader = resp.getHeaders("Content-Length");
                long length = -1L;
                if (respHeader.length > 0) {
                    length = Long.parseLong(respHeader[0].getValue());
                }

                // 读取内容
                HttpEntity entity = resp.getEntity();
                if (null != entity) {
                    // 初始化临时变量
                    long currLen = 0;
                    File tmpFile = new File(filePath.getParent(), filePath.getName() + TMP_FILE_SUFFIX);
                    byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
                    int len;
                    try (InputStream is = entity.getContent();
                            OutputStream os = new FileOutputStream(tmpFile)) {
                        // 读取文件内容
                        while ((len = is.read(buffer)) > 0) {
                            // 写入到临时文件中
                            os.write(buffer, 0, len);

                            // 更新进度
                            progressListener.progress(url, (int)((currLen += len) * 1.0 / length * 100));
                        }
                        os.flush();
                    }

                    // 更新异常进度
                    if (-1L != length && currLen != length) {
                        progressListener.progress(url, -1);
                        status.setCode(Status.SYS_ERROR);
                        status.setMsg("Content-Length is " + length + ", but actual is " + currLen);

                        status.setTimeOfSecond(System.currentTimeMillis() - startTime);
                        return status;
                    }
                    else if (-1L == length) {
                        progressListener.progress(url, 100);
                    }

                    // 重命名文件
                    if (filePath.exists()) {
                        filePath.delete();
                    }
                    tmpFile.renameTo(filePath);

                    status.setTimeOfSecond(System.currentTimeMillis() - startTime);
                    return status;
                }
            }
        }
        catch (Exception e) {
            progressListener.progress(url, -1);
            status.setCode(Status.NET_ERROR, e);
            status.setMsg("exception type is " + e.getClass() + ", msg is " + e.getMessage());
        }

        status.setTimeOfSecond(System.currentTimeMillis() - startTime);
        return status;
    }

    /**
     * 从URL中得到文件名
     * @param url URL
     * @return 文件名
     */
    private String getFileNameFromUrl(String url) {
        if (null != url) {
            int beginIdx = url.lastIndexOf("/") + 1;
            if (beginIdx > 0) {
                int endIdx = url.indexOf("?", beginIdx);
                if (endIdx < 0) {
                    endIdx = url.length();
                }
                return url.substring(beginIdx, endIdx);
            }
        }

        return "";
    }

    /**
     * 从URL提取根URL
     * @param url URL
     * @return 根URL
     */
    private String getRootUrl(String url) {
        if (null != url) {
            int beginIdx = url.indexOf("://");
            if (beginIdx > 0) {
                int endIdx = url.indexOf("/", beginIdx + 3);
                if (endIdx < 0) {
                    endIdx = url.length();
                }

                return url.substring(0, endIdx);
            }
        }

        return url;
    }

    /**
     * 下载状态类
     */
    @Getter
    @Setter
    private static class Status {
        /**
         * OK
         */
        public static final int OK = 0x00;

        /**
         * HTTP错误
         */
        public static final int HTTP_ERROR = 0x01;

        /**
         * 网络错误
         */
        public static final int NET_ERROR = 0x02;

        /**
         * 系统错误
         */
        public static final int SYS_ERROR = 0x03;

        /**
         * 通过三个必须的参数初始化对象
         * @param code 状态码
         * @param url 下载的URL
         * @param filePath 保存的路径
         */
        public Status(int code, String url, File filePath) {
            this.setCode(code);
            this.setUrl(url);
            this.setFilePath(filePath);
        }
        /**
         * 状态码
         */
        private int code;

        /**
         * 消息
         */
        private String msg;

        /**
         * 下载的URL
         */
        private String url;

        /**
         * 文件的保存路径
         */
        private File filePath;

        /**
         * HTTP的返回状态码
         */
        private int httpCode;

        /**
         * 异常
         */
        private Exception exception;

        /**
         * 秒级时间
         */
        private long timeOfSecond;

        /**
         * 设置状态码，并同时设置HTTP状态码
         * @param code 状态码
         * @param httpCode HTTP状态吗
         */
        public void setCode(int code, int httpCode) {
            this.setCode(code);
            this.setHttpCode(httpCode);
        }

        /**
         * 设置状态码，并同时设置异常
         * @param code 状态码
         * @param exception 异常
         */
        public void setCode(int code, Exception exception) {
            this.setCode(code);
            this.setException(exception);
        }
    }
}
