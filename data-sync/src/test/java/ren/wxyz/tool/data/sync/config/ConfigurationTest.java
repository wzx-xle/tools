/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.config;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试XML配置
 *
 * @author wxyz 2016-09-04_10:03
 * @since 1.0
 */
public class ConfigurationTest {

    private String classPath = ConfigurationTest.class.getResource("/").getPath();

    @Test
    public void testParse() throws Exception {
        Configuration config = Configuration.parse(classPath + "app-test.xml");
        assertEquals(1, config.getConns().size());

        RemoteConnection conn = config.getConns().get(0);
        assertEquals("103", conn.getId());
        assertEquals("SSH",conn.getProtocol());
        assertEquals("192.168.1.103",conn.getHost());
        assertEquals(22,conn.getPort());
        assertEquals("test",conn.getUsername());
        assertEquals("test",conn.getPassword());

        SyncAppInfo app = config.getApps().get(0);
        assertEquals("103", app.getConn());
        assertEquals("/server/tomcat/bin/restart.sh", app.getOkExec());
        assertEquals(1, app.getSyncs().size());

        SyncInfo sync = app.getSyncs().get(0);
        assertTrue(sync.isDelete());
        assertEquals(2, sync.getItems().size());

        List<SyncInfo.Item> items = sync.getItems();
        assertEquals("./conf", items.get(0).getLocal());
        assertEquals("/server/conf", items.get(0).getRemote());
        assertEquals("./tomcat/webapps", items.get(1).getLocal());
        assertEquals("/server/tomcat/webapps", items.get(1).getRemote());
    }
}