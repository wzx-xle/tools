package ren.wxyz.tools.http.download;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
     * 配置类对象
     */
    private Configuration config;

    /**
     * 以给定配置项加载下载对象
     *
     * @param config 配置对象
     */
    public DownloadFile(Configuration config) {
        this.config = config;
    }

    /**
     * 下载
     *
     * @param urls URL列表
     */
    public void download(List<String> urls) {

    }

    /**
     * 下载单个文件
     *
     * @param url      URL地址
     * @param filePath 保存文件路径
     */
    private void download(String url, String filePath) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            byte[] respBody = httpClient.execute(httpGet, new ResponseHandler<byte[]>() {
                @Override
                public byte[] handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {

                    }
                    return new byte[0];
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
