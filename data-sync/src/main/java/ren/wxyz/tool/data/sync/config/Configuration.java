/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 配置类
 *
 * @author wxyz 2016-09-03_09:43
 * @since 1.0
 */
@Data
@XStreamAlias("apps")
public class Configuration {

    @XStreamImplicit
    private List<Connection> conns;

    @XStreamImplicit
    private List<SyncAppInfo> apps;

    /**
     * 默认的配置文件路径
     */
    public static final String DEFAULT_CONFIGUATION_FILE = "app.xml";

    /**
     * 解析配置文件
     *
     * @param configFile 配置文件
     * @return 解析的配置对象
     */
    public static Configuration parse(String configFile) {
        XStream xStream = new XStream();
        xStream.processAnnotations(Configuration.class);
        return (Configuration) xStream.fromXML(new File(configFile));
    }
}
