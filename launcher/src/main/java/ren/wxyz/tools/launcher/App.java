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
import ren.wxyz.tools.launcher.lambda.VoidFunction;
import ren.wxyz.tools.launcher.ui.MainFrame;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 入口类
 *
 * @author wxyz 2017-04-26_21:03
 * @since 1.0
 */
@Slf4j
public class App {

    private static final String APP_NAME = "java-tools";

    /**
     * 入口方法
     *
     * @param args
     */
    public static void main(String[] args) {
        log.info("启动 {} 工具", APP_NAME);
        // 解析参数成功后，启动程序
        if (cli(args)) {
            start();
        }
        else {
            log.error("意外停止 {} 工具", APP_NAME);
        }
    }

    /**
     * 启动
     */
    static void start() {
        // 启动窗体
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame(APP_NAME + "  v" + Configuration.getConfiguration().getAppVersion()
                        , new VoidFunction() {
                    @Override
                    public void invoke() {
                        App.onWindowColsing();
                    }
                });
            }
        });
    }

    /**
     * 解析命令参数
     *
     * @param args 命令行参数
     * @return 控制流码
     */
    static boolean cli(String[] args) {
        boolean status = false;

        Options opts = new Options();
        opts.addOption("h", "help", false, "帮助信息");
        opts.addOption("c", "config", true, "配置文件的路径");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(opts, args, false);
            HelpFormatter hf = new HelpFormatter();
            // 帮助
            if (cmd.hasOption("h")) {
                hf.printHelp(APP_NAME, opts);
            }
            else {
                // 配置文件
                String configFile = Configuration.DEFAULT_CONFIGUATION_FILE;
                if (cmd.hasOption('c')) {
                    configFile = cmd.getOptionValue('c');
                    log.info("配置文件 {}", configFile);
                }
                else {
                    log.warn("没有指定配置文件，将启用默认的配置文件 {}", configFile);
                }
                status = Configuration.parse(configFile);

                if (!status) {
                    return false;
                }

                status = true;
            }
        }
        catch (ParseException e) {
            log.warn("参数解析错误", e);
        }
        catch (Exception e) {
            log.error("额外异常", e);
        }

        return status;
    }

    /**
     * 窗口关闭后调用的方法
     */
    static void onWindowColsing() {
        Configuration.getConfiguration().save();
        log.info("停止 {} 工具", APP_NAME);
    }
}
