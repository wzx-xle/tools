/**
 * Copyright (C) 2001-2017 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.bean;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 引用对象的测试类
 *
 * @auther wxyz 2017-04-24_12:47
 * @since 1.0.0
 */
public class RefObjectTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        String expected;

        expected = "";
        RefObject<String> aa = new RefObject<>(expected);
        assertEquals(expected, aa.get());

        expected = "test";
        aa.set(expected);
        assertEquals(expected, aa.get());
    }
}