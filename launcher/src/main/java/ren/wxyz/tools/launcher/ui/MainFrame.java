/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.launcher.ui;

import ren.wxyz.tools.commons.bean.ObjectHelper;
import ren.wxyz.tools.commons.config.AppConfig;
import ren.wxyz.tools.commons.config.Configuration;
import ren.wxyz.tools.launcher.lambda.VoidFunction;
import ren.wxyz.tools.launcher.ui.proxy.ProxyMenuService;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主窗口
 *
 * @author wxyz 2017-04-26_21:03
 * @since 1.0
 */
public class MainFrame extends JFrame {

    /**
     * 窗体的宽和高
     */
    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 450;

    /**
     * 窗口关闭回调
     */
    private final VoidFunction onWindowClosing;

    /**
     * 代理菜单处理服务
     */
    private ProxyMenuService proxyMenuService;

    /**
     * 窗体启动构造器
     *
     * @param title 标题
     */
    public MainFrame(String title, VoidFunction onWindowClosing) {
        this.setTitle(title);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        initMenu();
        initTabbedPanel();
        initEventInvoke();

        this.onWindowClosing = onWindowClosing;
        this.setVisible(true);
    }

    /**
     * 初始化菜单
     */
    private void initMenu() {
        // 菜单条
        JMenuBar menuBar = new JMenuBar();

        /**
         * 文件菜单
         */
        JMenu file = new JMenu("文件");
        menuBar.add(file);

        /**
         * 设置菜单
         */
        JMenu settings = new JMenu("设置");
        menuBar.add(settings);

        // 代理菜单
        JMenu proxySettings = new JMenu("代理");
        proxyMenuService = new ProxyMenuService(proxySettings, Configuration.getConfiguration());
        proxyMenuService.initOptionsMenu()
                .addSeparator()
                .initOperateMenu();

        settings.add(proxySettings);

        JMenuItem config = new JMenuItem("配置");
        settings.add(config);

        /**
         * 帮助菜单
         */
        JMenu help = new JMenu("帮助");
        menuBar.add(help);

        this.setJMenuBar(menuBar);
    }

    /**
     * 选项卡
     */
    private JTabbedPane tabbedPane;

    /**
     * 应用列表
     */
    private List<JPanel> apps = new ArrayList<>();

    /**
     * 应用大小与存储
     */
    private List<Dimension> appSizes = new ArrayList<>();

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    /**
     * 初始化选项卡
     */
    private void initTabbedPanel() {
        tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.add(tabbedPane, BorderLayout.CENTER);

        // 加载App
        loadApps();

        // 初始化窗口大小
        changeWindowSizeByTabbedPane();
    }

    /**
     * 初始化事件调用
     */
    private void initEventInvoke() {
        // 窗口事件处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onWindowClosing.invoke();
                System.exit(0);
            }
        });

        // 监听选项卡切换事件
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeWindowSizeByTabbedPane();
            }
        });
    }

    /**
     * 根据选项卡大小调整窗口大小
     */
    private void changeWindowSizeByTabbedPane() {
        Dimension size = appSizes.get(tabbedPane.getSelectedIndex());

        if (size.getHeight() <= 10 || size.getWidth() <= 10) {
            setSize(FRAME_WIDTH, FRAME_HEIGHT);
            return;
        }

        setSize((int) size.getWidth() + 21, (int) size.getHeight() + 90);
    }

    /**
     * 加载应用
     */
    private void loadApps() {
        for (AppConfig app : Configuration.getConfiguration().getApps()) {
            // 跳过取消的
            if (!app.getEnabled()) {
                continue;
            }
            JPanel panel = ObjectHelper.getInstance(app.getClz(), app);
            tabbedPane.add(app.getName(), panel);
            apps.add(panel);
            appSizes.add(panel.getSize());
        }
    }
}
