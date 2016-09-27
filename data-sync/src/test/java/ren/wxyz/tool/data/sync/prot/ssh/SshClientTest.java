/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot.ssh;

import org.junit.*;
import ren.wxyz.tool.data.sync.prot.FileInfo;

import java.util.List;

/**
 * 测试SSH客户端
 *
 * @auther wxyz 2016-09-07_16:20
 * @since 1.0
 */
//@Ignore
public class SshClientTest {

    private static SshClient ssh;

    @BeforeClass
    public static void setUp() throws Exception {
        ssh = new SshClient("172.16.10.40", 22, "root", "iflytek!40", ".");
        ssh.openSession();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ssh.closeSession();
        ssh = null;
    }

    @Test
    public void testList() throws Exception {
        String workDir = "/root/data-service";

        String path = "/root";
        List<FileInfo> files = ssh.list(path, true);
        System.out.println(files.size());
        for (FileInfo file : files) {
            System.out.println(file.getAbsolutePath());
        }

        path = "/root/uc.zip";
        files = ssh.list(path, true);
        System.out.println(files.size());
        for (FileInfo file : files) {
            System.out.println(file.getAbsolutePath());
        }

        ssh.setWorkDirectory(workDir);
        path = "dataexchange";
        files = ssh.list(path, true);
        System.out.println(files.size());
        for (FileInfo file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}