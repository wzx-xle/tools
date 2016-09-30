/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import ren.wxyz.tool.data.sync.config.Configuration;

/**
 * 入口类
 *
 * @author wxyz 2016-09-03_09:43
 * @since 1.0
 */
@Slf4j
public class App {
    /**
     * 版本号
     */
    private static final String APP_VERISON = "0.1";

    /**
     * 应用配置
     */
    private static Configuration config = null;

    /**
     * 标记只比较，不同步
     */
    private static boolean onlyCompare = false;

    /**
     * 后台任务定时扫描周期，单位秒，0表示不后台定时
     */
    private static int deamonPeriodSecond = 0;

    public static void main(String[] args) {
        int code = cli(args);
        switch (code) {
            case 0:
                // 命令行方式启动
                break;
            case 1:
                // 图形界面方式启动
                break;
            default:
                break;
        }
    }

    /**
     * 解析命令参数
     * @param args 命令行参数
     * @return 控制流码
     */
    static int cli(String[] args) {
        int statusCode = 0;

        Options opts = new Options();
        opts.addOption("h", "help", false, "帮助信息");
        opts.addOption("v", "version", false, "输出版本");
        opts.addOption("c", "config", true, "配置文件的路径");
        opts.addOption("g", "graph", false, "启动图形界面");
        opts.addOption("C", "compare", false, "只比较，不同步");
        opts.addOption("d", "deamon", true, "作为后台任务启动，参数为定时扫描周期，单位秒");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(opts, args, false);
            HelpFormatter hf = new HelpFormatter();
            // 帮助
            if (cmd.hasOption("h")) {
                hf.printHelp("data-sync", opts);
            }
            // 程序版本
            else if (cmd.hasOption("v")) {
                System.out.println("version is " + APP_VERISON);
            }
            else {
                // 配置文件
                String configFile = Configuration.DEFAULT_CONFIGUATION_FILE;
                if (cmd.hasOption('c')) {
                    configFile = cmd.getOptionValue('c');
                    log.info("配置文件 {}", configFile);
                }
                else {
                    log.info("使用默认的配置文件 {}", Configuration.DEFAULT_CONFIGUATION_FILE);
                }
                config = Configuration.parse(configFile);

                // 启动图形界面
                if (cmd.hasOption('g')) {
                    statusCode = 1;
                }

                // 只比较，不同步
                if (cmd.hasOption('C')) {
                    onlyCompare = true;
                }

                // 作为后台任务启动
                if (cmd.hasOption('d')) {
                    int peroid = Integer.valueOf(cmd.getOptionValue('d'), 10);
                    if (peroid > 0) {
                        deamonPeriodSecond = peroid;
                    }
                    else {
                        log.warn("周期数必须大于-1，{}", peroid);
                    }
                }

                statusCode = 1;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            log.warn("参数解析错误", e);
        }

        return statusCode;
    }
}
