/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.common.file;

import org.junit.Test;

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
        assertEquals("/t", PathHelper.join(null, "/t"));
        assertEquals("t", PathHelper.join(null, "t"));
        assertEquals("t/pp", PathHelper.join(null, "t", "pp"));
        assertEquals("t/pp", PathHelper.join(null, "t", "/pp"));
        assertEquals("t/pp/", PathHelper.join(null, "t", "/pp/"));
        assertEquals("t/pp/ee", PathHelper.join(null, "t", "/pp/", "ee"));
        assertEquals("/t", PathHelper.join("/t", null));

        assertEquals("t/pp", PathHelper.join("t", "pp"));
        assertEquals("t/pp", PathHelper.join("t", "/pp"));
        assertEquals("t/pp/", PathHelper.join("t", "/pp/"));
        assertEquals("t/pp/ee", PathHelper.join("t", "/pp/", "ee"));

        assertEquals("t/pp/ee", PathHelper.join("t", "pp", null, "ee"));
        assertEquals("t/pp/ee", PathHelper.join("t", "pp", null, "  ee"));
    }
}