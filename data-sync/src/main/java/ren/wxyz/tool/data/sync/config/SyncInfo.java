/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * 同步信息
 *
 * @author wxyz 2016-09-03_11:04
 * @since 1.0
 */
@Data
@XStreamAlias("sync")
public class SyncInfo {

    @XStreamAsAttribute
    private boolean delete;

    @XStreamImplicit
    private List<Item> items;

    @Data
    @XStreamAlias("item")
    public static class Item {
        /**
         * 本地目录
         */
        @XStreamAsAttribute
        private String local;

        /**
         * 远程目录
         */
        @XStreamAsAttribute
        private String remote;
    }
}
