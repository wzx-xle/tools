package ren.wxyz.tools.http.download;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 入口类
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
    private static final Configuration config = new Configuration();

    public static void main(String[] args) {
        int code = cli(args);
        switch (code) {
            case 1:
                log.info("加载下载文件列表 {}", config.getUrlsFile());
                DownloadFile downloading = new DownloadFile(config);
                try {
                    List<String> urls = FileUtils.readLines(
                            new File(config.getCurrPath(), config.getUrlsFile()), "utf-8");
                    log.info("共有 {} 文件", urls.size());
                    if (urls.size() > 0) {
                        downloading.download(urls);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    log.error("下载异常", e);
                }
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
        opts.addOption("f", "file", true, "需要下载的文件列表的文件");
        opts.addOption("o", "output", true, "下载的文件的存放目录");
        opts.addOption("c", "config", true, "配置文件的路径");
        opts.addOption("v", "version", false, "输出版本");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(opts, args, false);
            HelpFormatter hf = new HelpFormatter();
            // 帮助
            if (cmd.hasOption("h")) {
                hf.printHelp("http-download", opts);
            }
            // 程序版本
            else if (cmd.hasOption("v")) {
                System.out.println("version is " + APP_VERISON);
            }
            else {
                // 配置文件
                if (cmd.hasOption('c')) {
                    String configFile = cmd.getOptionValue('c');
                    config.loadFile(new File(configFile));
                    log.info("使用执行的配置文件 {}", configFile);
                }
                else {
                    config.loadDefault();
                    log.info("使用默认的配置文件 {}", Configuration.DEFAULT_CONFIGUATION_FILE);
                }

                // 需要下载的文件列表的文件
                if (cmd.hasOption('f')) {
                    String urlsFile = cmd.getOptionValue('f');
                    config.setUrlsFile(urlsFile);
                }
                else {
                    if (StringUtils.isBlank(config.getUrlsFile())) {
                        throw new FileNotFoundException("需要下载的文件列表的文件找不到！");
                    }
                }

                statusCode = 1;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            log.warn("找不到下载文件列表", e);
        }
        catch (ParseException e) {
            e.printStackTrace();
            log.warn("参数解析错误", e);
        }

        return statusCode;
    }

}
