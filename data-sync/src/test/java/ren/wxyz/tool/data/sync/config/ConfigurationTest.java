/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.config;

import org.junit.Test;
import ren.wxyz.tool.common.file.PathHelper;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试XML配置
 *
 * @author wxyz 2016-09-04_10:03
 * @since 1.0
 */
public class ConfigurationTest {

    private String classPath = PathHelper.normalizePath(PathHelper.getClassPath(ConfigurationTest.class));

    @Test
    public void testParse() throws Exception {
        Configuration config = Configuration.parse(classPath + "app-test.xml");
        assertEquals(2, config.getConns().size());

        Connection conn = config.getConns().get(0);
        assertEquals("local", conn.getId());
        assertEquals("local",conn.getProtocol());
        assertEquals("D:\\test", conn.getWorkDir());

        conn = config.getConns().get(1);
        assertEquals("103", conn.getId());
        assertEquals("ssh",conn.getProtocol());
        assertEquals("192.168.1.103",conn.getHost());
        assertEquals(22,conn.getPort());
        assertEquals("test",conn.getUsername());
        assertEquals("test",conn.getPassword());
        assertEquals("/server", conn.getWorkDir());

        SyncAppInfo app = config.getApps().get(0);
        assertEquals("local", app.getSourceRef());
        assertEquals("103", app.getTargetRef());
        assertEquals("/server/tomcat/bin/shutdown.sh", app.getBeforeExec());
        assertEquals("/server/tomcat/bin/start.sh", app.getAfterExec());
        assertEquals(1, app.getSyncs().size());

        SyncInfo sync = app.getSyncs().get(0);
        assertTrue(sync.isDelete());
        assertEquals(2, sync.getItems().size());

        List<SyncInfo.Item> items = sync.getItems();
        assertEquals("./conf", items.get(0).getSource());
        assertEquals("/server/conf", items.get(0).getTarget());
        assertEquals("./tomcat/webapps", items.get(1).getSource());
        assertEquals("/server/tomcat/webapps", items.get(1).getTarget());
    }
}