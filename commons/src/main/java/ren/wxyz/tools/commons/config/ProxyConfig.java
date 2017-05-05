/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 代理配置
 *
 * @author wxyz 2017-04-26_22:01
 * @since 1.0
 */
@Setter
@Getter
@ToString
@XStreamAlias("proxy")
public class ProxyConfig {

    /**
     * 代理服务器名
     */
    @XStreamAsAttribute
    private String name;

    /**
     * 启用标志，为空也是不启用
     */
    @XStreamAsAttribute
    private boolean enable;

    /**
     * 代理服务器的访问协议
     */
    private String protocol;

    /**
     * 代理服务器的访问地址
     */
    private String host;

    /**
     * 代理服务器的访问端口
     */
    private int port;

    /**
     * 代理服务器的访问用户名
     */
    private String username;

    /**
     * 代理服务器的访问密码
     */
    private String password;
}
