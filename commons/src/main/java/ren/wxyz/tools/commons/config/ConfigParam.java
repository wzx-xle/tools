/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;

/**
 * 应用配置的参数
 *
 * @author wxyz 2017-04-26_22:19
 * @since 1.0
 */
@Data
@XStreamAlias("param")
public class ConfigParam {

    /**
     * 名称
     */
    @XStreamAsAttribute
    private String name;

    /**
     * 内容
     */
    @XStreamAsAttribute
    private String value;
}
