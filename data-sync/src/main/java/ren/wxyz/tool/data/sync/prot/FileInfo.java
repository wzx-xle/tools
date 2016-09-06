/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import lombok.Data;

import java.util.Date;

/**
 * 文件信息
 *
 * @auther wxyz 2016-09-06_17:41
 * @since 1.0
 */
@Data
public class FileInfo {

    private String filename;

    private String fileType;

    private String absolutePath;

    private String relativePath;

    private Date modifyDate;

    private Date createDate;
}
