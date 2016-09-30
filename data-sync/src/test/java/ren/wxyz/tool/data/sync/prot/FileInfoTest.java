/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * 测试文件信息本身
 *
 * @auther wxyz 2016-09-30_16:46
 * @since 1.0
 */
public class FileInfoTest {

    @Test
    public void testEquals() throws Exception {
        Date now = new Date();

        FileInfo fi1 = new FileInfo();
        fi1.setFilename("test.txt");
        fi1.setFileType(FileInfo.Type.REG);
        fi1.setFileSize(100);
        fi1.setRelativePath("t2\\test.txt");
        fi1.setAbsolutePath("E:\\t2\\test.txt");
        fi1.setModifyDate(now);

        FileInfo fi2 = new FileInfo();
        fi2.setFilename("test.txt");
        fi2.setFileType(FileInfo.Type.REG);
        fi2.setFileSize(100);
        fi2.setRelativePath("t2\\test.txt");
        fi2.setAbsolutePath("E:\\t2\\test.txt");
        fi2.setModifyDate(now);

        assertTrue(fi1.equals(fi2));

        // 修改名称
        fi2.setFilename("test2.txt");
        assertFalse(fi1.equals(fi2));
        fi2.setFilename(fi1.getFilename());

        // 修改类型
        fi2.setFileType(FileInfo.Type.DIR);
        assertFalse(fi1.equals(fi2));
        fi2.setFileType(fi1.getFileType());

        // 修改大小
        fi2.setFileSize(200);
        assertFalse(fi1.equals(fi2));
        fi2.setFileSize(fi1.getFileSize());

        // 修改目录
        fi2.setRelativePath("t3\\test.txt");
        assertFalse(fi1.equals(fi2));
        fi2.setRelativePath(fi1.getRelativePath());

        // 文件被修改
        fi2.setModifyDate(new Date());
        assertFalse(fi1.equals(fi2));
        fi2.setModifyDate(fi1.getModifyDate());

        // 绝对路径不参与比较
        fi2.setAbsolutePath("G:\\t2\\test.txt");
        assertTrue(fi1.equals(fi2));
        fi2.setAbsolutePath(fi1.getAbsolutePath());
    }
}