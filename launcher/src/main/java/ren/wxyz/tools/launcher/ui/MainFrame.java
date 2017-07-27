/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.launcher.ui;

import ren.wxyz.tools.launcher.lambda.VoidFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        this.setJMenuBar(menuBar);

        // 文件菜单
        JMenu file = new JMenu("文件");
        menuBar.add(file);

        // 设置菜单
        JMenu settings = new JMenu("设置");
        menuBar.add(settings);

        // 帮助菜单
        JMenu help = new JMenu("帮助");
        menuBar.add(help);
    }

    /**
     * 选项卡
     */
    private JTabbedPane tabbedPane;

    /**
     * 初始化选项卡
     */
    private void initTabbedPanel() {
        tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//        tabbedPane.addT
        this.add(tabbedPane, BorderLayout.CENTER);

        // 全局设置
        JPanel globalSettings = new JPanel();
        tabbedPane.add("全局设置", globalSettings);

        for (String name : new String[] {"测试1", "测试2", "测试2", "测试2", "测试2", "测试2"}) {
            JPanel panel = new JPanel();
            tabbedPane.add(name, panel);
        }
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
    }
}
