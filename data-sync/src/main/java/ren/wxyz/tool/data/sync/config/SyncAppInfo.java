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
 * 以应用为单位同步数据的配置信息
 *
 * @author wxyz 2016-09-03_11:02
 * @since 1.0
 */
@Data
@XStreamAlias("app")
public class SyncAppInfo {

    @XStreamAsAttribute
    @XStreamAlias("source-ref")
    private String sourceRef;

    @XStreamAsAttribute
    @XStreamAlias("target-ref")
    private String targetRef;

    @XStreamImplicit
    private List<SyncInfo> syncs;

    private String beforeExec;

    private String afterExec;
}
