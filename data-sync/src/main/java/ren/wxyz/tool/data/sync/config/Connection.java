/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

/**
 * 远程连接信息
 *
 * @author wxyz 2016-09-03_10:55
 * @since 1.0
 */
@Data
@XStreamAlias("conn")
public class Connection {

    /**
     * ID，唯一标识一个连接
     */
    @XStreamAsAttribute
    private String id;

    /**
     * 连接协议
     */
    private String protocol;

    /**
     * 域名或IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 连接用户名
     */
    private String username;

    /**
     * 连接密码
     */
    private String password;

    /**
     * 工作目录
     */
    private String workDir;
}
