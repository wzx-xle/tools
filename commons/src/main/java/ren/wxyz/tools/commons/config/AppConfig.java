/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个应用配置
 *
 * @author wxyz 2017-04-26_22:14
 * @since 1.0
 */
@Setter
@Getter
@ToString
@XStreamAlias("app")
public class AppConfig {

    /**
     * 应用名称
     */
    @XStreamAsAttribute
    private String name;

    /**
     * 应用版本
     */
    @XStreamAsAttribute
    private String version;

    /**
     * 启动类
     */
    @XStreamAsAttribute
    @XStreamAlias("class")
    private String clz;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 参数集合
     */
    @XStreamImplicit
    private List<ConfigParam> params;

    /**
     * 获取一个配置参数
     *
     * @param name 参数名
     * @return
     */
    public ConfigParam getParam(String name) {
        if (null != params || !StringUtils.isBlank(name)) {
            for (ConfigParam param : params) {
                if (name.equals(param.getName())) {
                    return param;
                }
            }
        }

        return null;
    }

    /**
     * 设置一个配置参数
     *
     * @param param 参数
     */
    public void setParam(ConfigParam param) {
        if (null == param || StringUtils.isBlank(param.getName())) {
            return;
        }

        if (null == params) {
            params = new ArrayList<>();
        }
        else {
            // 找到原来的参数替换掉
            for (ConfigParam p : params) {
                // 同一个对象，不用替换
                if (p == param) {
                    return;
                }
                // 新对象，同参数，用传入的值替换就旧值
                else if (param.getName().equals(p.getName())) {
                    p.setValue(param.getValue());
                    return;
                }
            }
        }

        params.add(param);
    }

    /**
     * 设置一个新参数
     *
     * @param name 名称
     * @param value 值
     */
    public void setParam(String name, String value) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        if (null == params) {
            params = new ArrayList<>();
        }
        else {
            // 找到就替换新值
            for (ConfigParam p : params) {
                if (name.equals(p.getName())) {
                    p.setValue(value);
                    return;
                }
            }
        }

        params.add(new ConfigParam(name, value));
    }
}
