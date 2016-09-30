/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot.local;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ren.wxyz.tool.common.file.PathHelper;
import ren.wxyz.tool.data.sync.prot.FileInfo;

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
        String path = "test-local-client";

        List<FileInfo> files = localClient.list(path, true);
        printFileInfo(files);

        files = localClient.list(path, false);
        printFileInfo(files);

        files = localClient.list("", true);
        printFileInfo(files);
    }

    private void printFileInfo(List<FileInfo> files) {
        String printFormat = "%s,%d,%d";
        System.out.println(files.size());
        for (FileInfo f : files) {
            System.out.println(String.format(printFormat,
                    f.getAbsolutePath(), f.getFileSize(), f.getModifyDate().getTime()));
        }
    }
}