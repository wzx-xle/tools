/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import org.junit.BeforeClass;
import org.junit.Test;
import ren.wxyz.tool.common.file.PathHelper;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试本地客户端
 *
 * @auther wxyz 2016-09-27_11:49
 * @since 1.0
 */
public class LocalClientTest {

    private static LocalClient localClient;

    @BeforeClass
    public static void setUp() throws Exception {
        localClient = new LocalClient(PathHelper.getClassPath(LocalClientTest.class));
    }

    @Test
    public void testList() throws Exception {
        List<FileInfo> files = localClient.list("test-local-client", true);
        assertEquals(8, files.size());
        int count = 0;
        for (FileInfo fi : files) {
            if (fi.getRelativePath().equals(PathHelper.normalizePath("test"))) {
                count++;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub1\\ttsub1.txt"))) {
                count++;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub1\\ttsub2.txt"))) {
                count++;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub2\\ttsub2.txt"))) {
                count++;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub2\\sub2-sub1\\ttsub2-sub1.txt"))) {
                count++;
            }
        }
        assertEquals(5, count);

        files = localClient.list("test-local-client", false);
        assertEquals(3, files.size());
        count = 0;
        for (FileInfo fi : files) {
            if (fi.getRelativePath().equals(PathHelper.normalizePath("test"))) {
                count++;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub1\\ttsub1.txt"))) {
                count--;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub1\\ttsub2.txt"))) {
                count--;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub2\\ttsub2.txt"))) {
                count--;
            }
            if (fi.getRelativePath().equals(PathHelper.normalizePath("sub2\\sub2-sub1\\ttsub2-sub1.txt"))) {
                count--;
            }
        }
        assertEquals(1, count);

        files = localClient.list("", true);
        assertNotNull(files);
        assertTrue(files.size() >= 8);

        files = localClient.list("test-local-client\\test", true);
        assertEquals(1, files.size());
        assertEquals("test", files.get(0).getRelativePath());

        files = localClient.list("test-local-client\\dfsdfsfsdfs", true);
        assertNotNull(files);
        assertEquals(0, files.size());
    }
}