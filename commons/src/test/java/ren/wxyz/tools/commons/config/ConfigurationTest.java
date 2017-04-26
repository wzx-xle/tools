/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import org.junit.Test;
import ren.wxyz.tools.commons.file.PathHelper;

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

        Configuration config = Configuration.parse(configPath);
        assertNotNull(config);
        assertNotNull(config.getProxies());
        assertEquals(2, config.getProxies().size());
        ProxyConfig local = config.getProxies().get(0);
        assertEquals("local", local.getName());
        assertEquals("SOCKS5", local.getProtocol());
        assertEquals("127.0.0.1", local.getHost());

        assertNotNull(config.getConfigs());


    }

    @Test
    public void save() throws Exception {

    }

}