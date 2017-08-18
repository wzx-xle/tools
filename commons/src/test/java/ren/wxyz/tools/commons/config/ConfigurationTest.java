/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import org.junit.Test;
import ren.wxyz.tools.commons.file.PathHelper;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置文件测试类
 *
 * @author wxyz 2017-04-26_22:25
 * @since 1.0
 */
public class ConfigurationTest {
    /**
     * 类路径
     */
    private static final String CLASS_PATH = PathHelper.normalizePath(PathHelper.getClassPath(ConfigurationTest.class));

    @Test
    public void parse() throws Exception {
        String configPath = CLASS_PATH + "app-read.xml";

        Configuration.parse(configPath);

        Configuration config = Configuration.getConfiguration();
        assertNotNull(config);

        assertEquals("1.0", config.getAppVersion());

        assertNotNull(config.getProxies());
        assertEquals(2, config.getProxies().size());
        ProxyConfig local = config.getProxies().get(0);
        assertEquals("local", local.getName());
        assertEquals("SOCKS5", local.getProtocol());
        assertEquals("127.0.0.1", local.getHost());

        ProxyConfig rasp22 = config.getProxies().get(1);
        assertEquals("rasp_22", rasp22.getName());
        assertEquals("SOCKS5", rasp22.getProtocol());
        assertEquals("127.0.0.1", rasp22.getHost());

        assertNotNull(config.getUseProxy());
        assertEquals("local", config.getUseProxy());
        assertEquals("local", config.getUsingProxy().getName());

        config.setUseProxy("rasp_22");
        assertEquals("rasp_22", config.getUseProxy());
        assertEquals("rasp_22", config.getUsingProxy().getName());

        assertNotNull(config.getApps());
        assertEquals(2, config.getApps().size());

        List<AppConfig> apps = config.getApps();

        // 第一个应用配置
        AppConfig httpDownload = apps.get(0);
        assertNotNull(httpDownload);
        assertEquals("http-download", httpDownload.getName());
        assertEquals("ren.wxyz.tools.http.download.App", httpDownload.getClz());
        assertEquals("1.0", httpDownload.getVersion());
        assertTrue(httpDownload.getEnabled());
        assertEquals("HTTP下载", httpDownload.getDescription());
        assertEquals("outputdir/:date:", httpDownload.getParam("outputFolder").getValue());
        assertEquals("3", httpDownload.getParam("downloadThreadNum").getValue());

        // 第二个应用配置
        AppConfig httpDownload2 = apps.get(1);
        assertNotNull(httpDownload2);
        assertEquals("http-download2", httpDownload2.getName());
        assertEquals("ren.wxyz.tools.http.download.App", httpDownload2.getClz());
        assertEquals("1.0", httpDownload2.getVersion());
        assertFalse(httpDownload2.getEnabled());
        assertEquals("HTTP下载", httpDownload2.getDescription());
        assertEquals("outputdir/:date:", httpDownload2.getParam("outputFolder").getValue());
        assertEquals("3", httpDownload2.getParam("downloadThreadNum").getValue());
    }

    @Test
    public void save() throws Exception {
        String readConfigPath = CLASS_PATH + "app-read.xml";
        String writeConfigPath = CLASS_PATH + "app-write.xml";

        Configuration.parse(readConfigPath);
        Configuration config = Configuration.getConfiguration();
        assertNotNull(config);

        config.setUseProxy("rasp_22");

        config.setConfigPath(writeConfigPath);
        config.save();
    }

}