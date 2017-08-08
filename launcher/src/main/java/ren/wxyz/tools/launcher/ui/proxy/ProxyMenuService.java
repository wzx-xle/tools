/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.launcher.ui.proxy;

import ren.wxyz.tools.commons.config.Configuration;
import ren.wxyz.tools.commons.config.ProxyConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 代理菜单构建
 *
 * @author wxyz 2017-08-08_23:00
 * @since 0.0.1
 */
public class ProxyMenuService {

    /**
     * 代理设置菜单
     */
    private JMenu proxySettings;

    /**
     * 全局配置对象
     */
    private Configuration configuration;

    /**
     * 构建一个菜单处理对象
     *
     * @param proxySettings 代理设置菜单
     * @param configuration 全局配置对象
     */
    public ProxyMenuService(JMenu proxySettings, Configuration configuration) {
        this.proxySettings = proxySettings;
        this.configuration = configuration;
    }

    /**
     * 代理选项组
     */
    private ButtonGroup proxyOptionsButton = new ButtonGroup();

    /**
     * 初始化多个代理选项
     *
     * @return 自身
     */
    public ProxyMenuService initOptionsMenu() {
        // 多个代理选项
        List<ProxyConfig> proxies = configuration.getProxies();
        // 读取配置的代理
        for (ProxyConfig proxy : proxies) {
            JRadioButtonMenuItem proxyItem = new JRadioButtonMenuItem(proxy.getName());
            proxyItem.setActionCommand(proxy.getName());
            proxyItem.addActionListener(proxyOptionsListener);

            proxySettings.add(proxyItem);
            proxyOptionsButton.add(proxyItem);

            // 激活配置的选项
            if (Configuration.getConfiguration().getUseProxy().equals(proxy.getName())) {
                proxyOptionsButton.setSelected(proxyItem.getModel(), true);
            }
        }

        return this;
    }

    // 切换代理动作处理
    private ActionListener proxyOptionsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            configuration.setUseProxy(e.getActionCommand());
        }
    };

    /**
     * 添加分隔符
     *
     * @return 自身
     */
    public ProxyMenuService addSeparator() {
        proxySettings.addSeparator();

        return this;
    }

    /**
     * 初始化操作菜单
     *
     * @return 自身
     */
    public ProxyMenuService initOperateMenu() {
        // 代理添加功能
        JMenuItem proxySubItemAdd = new JMenuItem("添加");
        proxySubItemAdd.setActionCommand("add");
        proxySubItemAdd.addActionListener(proxyOperateListener);

        JMenuItem proxySubItemEdit = new JMenuItem("编辑");
        proxySubItemEdit.setActionCommand("edit");
        proxySubItemEdit.addActionListener(proxyOperateListener);

        proxySettings.add(proxySubItemAdd);
        proxySettings.add(proxySubItemEdit);

        return this;
    }

    // 代理操作
    private ActionListener proxyOperateListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "add":
                    // TODO 弹出添加窗口
                    break;
                case "edit":
                    // TODO 弹出编辑窗口
                    break;
                default:

            }
        }
    };
}
