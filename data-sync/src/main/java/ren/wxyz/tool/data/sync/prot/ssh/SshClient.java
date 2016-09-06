/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot.ssh;

import com.jcraft.jsch.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * SFTP 客户端
 *
 * @auther wxyz 2016-09-06_11:49
 * @since 1.0
 */
@Setter
@Slf4j
public class SshClient {

    /**
     * 默认会话超时时间，60s
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 60000;

    /**
     * 域名或IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 登陆用户名
     */
    private String username;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 会话超时
     */
    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    /**
     * 登陆后的默认目录
     */
    private String homeDir;

    /**
     * 会话连接
     */
    private Session session = null;

    public SshClient(String host, int port, String username, String password) {
        setHost(host);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }

    /**
     * 打开会话
     */
    public synchronized void openSession() {
        if (null != session && session.isConnected()) {
            return;
        }

        this.session = createSession();
    }

    /**
     * 创建一个会话
     *
     * @return 会话对象
     */
    private Session createSession() {
        JSch jSch = new JSch();

        try {
            Session session = jSch.getSession(this.username, this.host, this.port);
            session.setPassword(this.password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(this.sessionTimeout);
            session.connect();

            return session;
        }
        catch (JSchException e) {
            log.error("SFTP 会话创建异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建一个SFTP管道
     * @return
     */
    private ChannelSftp createChannelSftp() {
        try {
            Channel channel = session.openChannel(SshType.SFTP);
            return (ChannelSftp) channel;
        }
        catch (JSchException e) {
            log.error("SFTP 管道创建异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭会话
     */
    public void closeSession() {
        if (null != session) {
            session.disconnect();
            session = null;
        }
    }

    /**
     * SSH管道的类型
     */
    public interface SshType {
        String SFTP = "sftp";
    }
}
