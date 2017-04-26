/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 配置类
 *
 * @author wxyz 2017-04-26_21:11
 * @since 1.0
 */
@Setter
@Getter
@Slf4j
@XStreamAlias("configs")
public class Configuration {

    /**
     * 缓存配置对象
     */
    private static Configuration configuration;

    /**
     * 解析配置文件
     *
     * @param configPath 配置文件路径
     */
    public static Configuration parse(String configPath) {
        log.debug("正在读取配置文件：{}", configPath);

        XStream xStream = new XStream();
        xStream.processAnnotations(Configuration.class);

        configuration = (Configuration) xStream.fromXML(new File(configPath));
        return configuration;
    }

    /**
     * 保存配置
     *
     * @param configPath 配置文件保存路径
     */
    public static boolean save(String configPath) {
        log.debug("正在写入配置文件：{}", configPath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Configuration.class);
        xStream.autodetectAnnotations(true);

        try (FileOutputStream fos = new FileOutputStream(configPath)){
            xStream.toXML(configuration, fos);

            return true;
        }
        catch (FileNotFoundException e) {
            log.warn("配置文件路径找不到！");
        }
        catch (IOException e) {
            log.warn("配置文件写入错误！");
        }

        return false;
    }

    /**
     * 代理服务器列表
     */
    @XStreamImplicit
    private List<ProxyConfig> proxies;

    /**
     * 应用配置
     */
    @XStreamImplicit
    private List<AppConfig> configs;
}
