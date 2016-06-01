package ren.wxyz.tools.http.download;

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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        final File rootPath = new File(config.getOutputFolder());
        if (!rootPath.exists()) {
            System.out.println("创建目录 " + rootPath.getAbsolutePath());
            rootPath.mkdirs();
        }

        // 线程池
        ExecutorService pool = Executors.newFixedThreadPool(config.getDownloadThreadNum());

        List<Future<Boolean>> futures = new ArrayList<>();

        // 循环多线程下载
        for (final String url : urls) {
            if (StringUtils.isBlank(url)) {
                continue;
            }
            final File saveFile = new File(rootPath, getFileNameFromUrl(url));

            Future<Boolean> future = pool.submit(() -> download(url, saveFile, progressListener));
            futures.add(future);
        }

        // 等待下载完成
        for (Future<Boolean> future : futures) {
            try {
                future.get();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
    }

    /**
     * 统一同一个进度监听器
     */
    private ProgressListener progressListener = new ProgressListener() {
        @Override
        public void progress(String url, int rate, String msg) {
            if (rate == 10 || rate == -1) {
                System.out.println(url + "," + rate + "," + msg);
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
    private boolean download(String url, File filePath, ProgressListener progressListener) {
        progressListener.progress(url, 0, "started");
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
                    progressListener.progress(url, -1, "status code is " + statusCode);
                }
                // 文件总长度
                Header[] respHeader = resp.getHeaders("Content-Length");
                long length = -1L;
                if (respHeader.length > 0) {
                    length = Long.parseLong(respHeader[0].getValue());
                }
                long currLen = 0;
                // 读取内容
                HttpEntity entity = resp.getEntity();
                if (null != entity) {
                    // 初始化临时变量
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
                            progressListener.progress(url, (int)((currLen += len) * 1.0 / length * 10)
                                    , "ok, time=" + ((System.currentTimeMillis() - startTime) / 1000));
                        }
                        os.flush();
                    }

                    // 更新异常进度
                    if (-1L != length && currLen != length) {
                        progressListener.progress(url, -1, "Content-Length is " + length + ", but actual is " + currLen);
                        return false;
                    }
                    else if (-1L == length) {
                        progressListener.progress(url, 10, "ok, time=" + ((System.currentTimeMillis() - startTime) / 1000));
                    }

                    // 重命名文件
                    if (filePath.exists()) {
                        filePath.delete();
                    }
                    tmpFile.renameTo(filePath);

                    return true;
                }
            }
        }
        catch (Exception e) {
            progressListener.progress(url, -1, "exception type is " + e.getClass() + ", msg is " + e.getMessage());
        }

        return false;
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
}
