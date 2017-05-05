/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.launcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import ren.wxyz.tools.commons.config.Configuration;

/**
 * 入口类
 *
 * @author wxyz 2017-04-26_21:03
 * @since 1.0
 */
@Slf4j
public class App {

    /**
     * 入口方法
     *
     * @param args
     */
    public static void main(String[] args) {
        if (!cli(args)) {
            return;
        }

        System.out.println("Hello World!");
    }

    static void start() {
        
    }

    private static Configuration config;

    /**
     * 解析命令参数
     *
     * @param args 命令行参数
     * @return 控制流码
     */
    static boolean cli(String[] args) {
        boolean statusCode = false;

        Options opts = new Options();
        opts.addOption("h", "help", false, "帮助信息");
        opts.addOption("c", "config", true, "配置文件的路径");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(opts, args, false);
            HelpFormatter hf = new HelpFormatter();
            // 帮助
            if (cmd.hasOption("h")) {
                hf.printHelp("java-tools", opts);
            }
            else {
                // 配置文件
                if (cmd.hasOption('c')) {
                    String configFile = cmd.getOptionValue('c');
                    Configuration.parse(configFile);
                    log.info("配置文件 {}", configFile);
                }
                else {
                    log.warn("没有指定配置文件，将启用默认的配置文件 {}", Configuration.DEFAULT_CONFIGUATION_FILE);
                }

                statusCode = true;
            }
        }
        catch (ParseException e) {
            log.warn("参数解析错误", e);
        }
        catch (Exception e) {
            log.error("额外异常", e);
        }

        return statusCode;
    }
}
