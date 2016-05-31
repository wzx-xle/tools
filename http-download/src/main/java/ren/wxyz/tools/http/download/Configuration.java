package ren.wxyz.tools.http.download;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;

/**
 * <p>
 * 配置文件的映射类
 * </p>
 *
 * @author wxyz
 * @datetime 2016/5/31 22:07
 */
@Getter
@Setter
public class Configuration {
    /**
     * 默认的配置文件
     */
    public static final String DEFAULT_CONFIGUATION_FILE = "./app.properties";
    /**
     * 代理服务器的主机名的参数名
     */
    public static final String PROXY_HOST = "proxy.host";
    /**
     * 代理服务器的端口号的参数名
     */
    public static final String PROXY_PORT = "proxy.port";

    /**
     * 是否启用代理的参数名
     */
    public static final String PROXY_ENABLE = "proxy.enable";

    /**
     * URL列表文件的参数名
     */
    public static final String URLS_FILE = "app.urls.file";

    /**
     * 下载文件的文件夹参数名
     */
    public static final String OUTPUT_FOLDER = "app.output.folder";

    /**
     * 加载属性配置
     */
    public void load(Properties props) {
        // 加载代理配置
        this.setProxyHost(props.getProperty(PROXY_HOST, ""));
        this.setProxyPort(Integer.parseInt(props.getProperty(PROXY_PORT, "-1")));
        this.setProxyEnable(Boolean.parseBoolean(props.getProperty(PROXY_ENABLE, "false")));

        // 加载应用配置
        this.setUrlsFile(props.getProperty(URLS_FILE, ""));
        this.setOutputFolder(props.getProperty(OUTPUT_FOLDER, ""));
    }

    /**
     * 从默认路径加载属性文件
     */
    public void loadDefault() {
        // 类路径
        String classPath = Configuration.class.getClassLoader().getResource("/").getPath();

        // 读取配置文件
        try {
            loadFile(new File(classPath, DEFAULT_CONFIGUATION_FILE));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从属性文件加载属性
     * @param propertiesFile 属性文件
     */
    public void loadFile(File propertiesFile) throws FileNotFoundException {
        // 读取配置文件
        Properties props = new Properties();
        try (Reader reader = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8")) {
            props.load(reader);
            load(props);
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代理服务器的主机
     */
    private String proxyHost;

    /**
     * 代理服务器的端口
     */
    private int proxyPort;

    /**
     * 是否启用代理
     */
    private boolean proxyEnable;

    /**
     * URL列表文件
     */
    private String urlsFile;

    /**
     * 下载文件的文件夹
     */
    private String outputFolder;
}
