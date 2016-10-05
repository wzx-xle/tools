/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.common.bean;

import lombok.Data;

/**
 * 引用对象
 *
 * @author wxyz 2016-10-05_15:55
 * @since 1.0
 */
@Data
public class RefObject<T> {

    private volatile T obj;
}
