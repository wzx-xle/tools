/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.common.file;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * 测试路径帮助类
 *
 * @auther wxyz 2016-09-07_18:08
 * @since 1.0
 */
public class PathHelperTest {

    @Test
    public void testJoin() throws Exception {
        assertEquals("", PathHelper.join(null));
        assertEquals("", PathHelper.join(null, null));

        assertEquals("", PathHelper.join("  ", null));

        assertEquals("", PathHelper.join(null, null, null));
        if (SystemUtils.IS_OS_WINDOWS) {
            assertEquals("t\\pp\\ee", PathHelper.join(null, "t", "pp", "ee"));
        }
        else {
            assertEquals("t/pp/ee", PathHelper.join(null, "t", "pp", "ee"));
        }

        assertEquals("t\\pp", PathHelper.join("\\", "t", "pp"));
        assertEquals("t/pp", PathHelper.join("\\", "t", "/pp"));
        assertEquals("t/pp/", PathHelper.join("\\", "t", "/pp/"));
        assertEquals("t/pp/ee", PathHelper.join("\\", "t", "/pp/", "ee"));
        assertEquals("\\t", PathHelper.join("\\", "\\t", null));

        assertEquals("t\\pp\\ee", PathHelper.join("\\", "t", "pp", null, "ee"));
        assertEquals("t\\pp\\ee", PathHelper.join("\\", "t", "pp", null, "  ee"));

        assertEquals("t/pp", PathHelper.join("/", "t", "pp"));
        assertEquals("t/pp", PathHelper.join("/", "t", "/pp"));
        assertEquals("t/pp/", PathHelper.join("/", "t", "/pp/"));
        assertEquals("t/pp/ee", PathHelper.join("/", "t", "/pp/", "ee"));
        assertEquals("/t", PathHelper.join("/", "/t", null));
    }

    @Test
    public void testIsAbsolute() throws Exception {
        try {
            PathHelper.isAbsolute(null);
        }
        catch (NullPointerException e) {
            assertEquals("path is null.", e.getMessage());
        }

        assertFalse(PathHelper.isAbsolute(""));
        assertFalse(PathHelper.isAbsolute("  "));

        assertTrue(PathHelper.isAbsolute("E:\\test.txt"));
        assertTrue(PathHelper.isAbsolute("E:\\"));
        assertTrue(PathHelper.isAbsolute("/E:/test.txt"));
        assertTrue(PathHelper.isAbsolute("/test.txt"));

        assertFalse(PathHelper.isAbsolute("test.txt"));
        assertFalse(PathHelper.isAbsolute("test/test.txt"));
        assertFalse(PathHelper.isAbsolute("test\\test.txt"));
        assertFalse(PathHelper.isAbsolute("test/test.txt"));
        assertFalse(PathHelper.isAbsolute("\\test\\test.txt"));
    }

    @Test
    public void testGetClassPath() {
        String workPath = new File("").getAbsolutePath();
        assertTrue(PathHelper.getClassPath(PathHelperTest.class).startsWith(workPath));
    }

    @Test
    public void testNormalizePath() {
        if (SystemUtils.IS_OS_WINDOWS) {
            assertEquals("C:\\dd\\tt.txt", PathHelper.normalizePath("C:\\dd\\tt.txt"));
            assertEquals("C:\\dd\\tt.txt", PathHelper.normalizePath("/C:/dd/tt.txt"));
        }
        else {
            assertEquals("C:/dd/tt.txt", PathHelper.normalizePath("C:\\dd\\tt.txt"));
            assertEquals("/dd/tt.txt", PathHelper.normalizePath("\\dd\\tt.txt"));
        }
    }
}