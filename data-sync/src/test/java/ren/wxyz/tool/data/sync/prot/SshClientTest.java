/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import org.junit.*;
import ren.wxyz.tool.data.sync.prot.FileInfo;
import ren.wxyz.tool.data.sync.prot.SshClient;

import java.util.List;

/**
 * 测试SSH客户端
 *
 * @auther wxyz 2016-09-07_16:20
 * @since 1.0
 */
@Ignore
public class SshClientTest {

    private static SshClient ssh;

    @BeforeClass
    public static void setUp() throws Exception {
//        ssh = new SshClient("172.16.10.40", 22, "root", "iflytek!40", ".");
        ssh = new SshClient("192.168.1.31", 22, "root", "123456", ".");
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
        printFileInfo(files, path);

        path = "/root/uc.zip";
        files = ssh.list(path, true);
        printFileInfo(files, path);

        path = "uc.zip"; // TODO 有BUG
        files = ssh.list(path, true);
        printFileInfo(files, path);

        ssh.setWorkDir(workDir);
        path = "dataexchange";
        files = ssh.list(path, true);
        printFileInfo(files, path);

        path = "dataexchange/dfsfsdfsfs";
        files = ssh.list(path, true);
        printFileInfo(files, path);

        path = "";
        files = ssh.list(path, true);
        printFileInfo(files, "空路径");
    }

    private void printFileInfo(List<FileInfo> files, String info) {
        String printFormat = "%s,%d,%d";
        System.out.println(info + "：" + files.size());
        for (FileInfo f : files) {
            if (f.getFileType() != FileInfo.Type.DIR) {
                System.out.println(String.format(printFormat,
                        f.getRelativePath(), f.getFileSize(), f.getModifyDate().getTime()));
            }
        }
    }
}