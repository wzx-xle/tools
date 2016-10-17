/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.sync;

import ren.wxyz.tool.data.sync.config.SyncAppInfo;
import ren.wxyz.tool.data.sync.config.Connection;
import ren.wxyz.tool.data.sync.prot.FileInfo;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 文件同步
 *
 * @auther wxyz 2016-09-30_15:26
 * @since 1.0
 */
public class FileSync {

    /**
     * 源连接
     */
    private Connection source;

    /**
     * 目标连接
     */
    private Connection target;

    /**
     * 同步应用
     */
    private SyncAppInfo app;

    /**
     * 构建一个文件同步
     *
     * @param source 源连接
     * @param target 目标连接
     * @param app 同步应用
     */
    public FileSync(Connection source, Connection target, SyncAppInfo app) {
        this.source = source;
        this.target = target;
        this.app = app;
    }

    /**
     * 比较文件
     *
     * @return 源和目标差异的文件列表，该列表
     */
    public Map<FileInfo, FileInfo> compare() {
        throw new NoSuchMethodError();
    }

    /**
     * 同步文件
     *
     * @return 同步文件数
     */
    public int sync() {
        throw new NoSuchMethodError();
    }
}
