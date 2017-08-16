/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.content.search;

import ren.wxyz.tools.commons.config.AppConfig;

import javax.swing.*;

/**
 * 内容查询的主界面
 *
 * @author wxyz 2017-08-16_21:19
 * @since 1.0
 */
public class App extends JPanel {

    private AppConfig config;

    private JButton test = new JButton("test");

    public App(AppConfig config) {
        this.config = config;
        this.setSize(400, 300);

        System.out.println("启动了");

        add(test);
    }
}
