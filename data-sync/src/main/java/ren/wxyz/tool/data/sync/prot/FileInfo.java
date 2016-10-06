/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import lombok.*;

import java.util.Date;

/**
 * 文件信息
 *
 * @auther wxyz 2016-09-06_17:41
 * @since 1.0
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude={"absolutePath"})
public class FileInfo {

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件类型
     */
    private Type fileType;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 文件类型
     */
    public enum Type {
        /**
         * 目录文件
         */
        DIR,

        /**
         * 连接文件
         */
        LNK,

        /**
         * 普通文件
         */
        REG,

        /**
         * 字符设备
         */
        CHR,

        /**
         * 块设备
         */
        BLK,

        /**
         * 管道
         */
        FIFO,

        /**
         * 套接字
         */
        SOCK
    }
}
