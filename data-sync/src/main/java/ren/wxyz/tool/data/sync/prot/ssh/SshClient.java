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
import org.apache.commons.lang3.StringUtils;
import ren.wxyz.tool.common.file.PathHelper;
import ren.wxyz.tool.data.sync.prot.FileInfo;

import java.util.*;

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
    private String workDirectory;

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
     *
     * @return
     */
    private ChannelSftp createChannelSftp() {
        try {
            ChannelSftp sftp = (ChannelSftp) session.openChannel(SshType.SFTP);
            sftp.connect();

            // 设置或更新工作目录
            if (StringUtils.isNotBlank(this.workDirectory)) {
                sftp.cd(this.workDirectory);
            }
            else {
                this.workDirectory = sftp.pwd();
            }

            return sftp;
        }
        catch (JSchException | SftpException e) {
            log.error("SFTP 管道创建异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定路径下文件列表
     *
     * @param path   远程路径
     * @param subDir 是否包含子目录
     * @return 文件列表
     */
    public List<FileInfo> list(final String path, boolean subDir) {
        ChannelSftp sftp = createChannelSftp();

        List<FileInfo> files = Collections.EMPTY_LIST;
        try {
            // 当前路径下
            files = list(sftp, path);

            // 路径是文件
            if (files.size() == 1 && files.get(0).getAbsolutePath().startsWith(path)) {
                files.get(0).setAbsolutePath(path);
                subDir = false;
            }

            // 遍历子目录
            if (subDir) {
                List<FileInfo> subFiles = new ArrayList<>();

                for (FileInfo file : files) {
                    if (file.getFileType() == FileInfo.Type.DIR) {
                        List<FileInfo> tmp = list(sftp, file.getAbsolutePath());
                        subFiles.addAll(tmp);
                    }
                }

                files.addAll(subFiles);
            }
        }
        finally {
            sftp.disconnect();
        }

        return files;
    }

    /**
     * 列出路径下的所有文件
     *
     * @param sftp sftp管道
     * @param path 路径
     * @return 文件列表
     */
    private List<FileInfo> list(ChannelSftp sftp, final String path) {
        final String currPath = path.startsWith("/") ? path : PathHelper.join(this.workDirectory, path);

        final List<FileInfo> files = new ArrayList<>();
        try {
            sftp.ls(path, new ChannelSftp.LsEntrySelector() {
                @Override
                public int select(ChannelSftp.LsEntry entry) {
                    if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) {
                        return 0;
                    }

                    FileInfo info = new FileInfo();
                    info.setAbsolutePath(PathHelper.join(currPath, entry.getFilename()));
                    info.setFilename(entry.getFilename());
                    info.setFileSize(entry.getAttrs().getSize());
                    info.setModifyDate(new Date(entry.getAttrs().getMTime() * 1000L));
                    info.setFileType(getFileType(entry.getAttrs()));

                    files.add(info);
                    return 0;
                }
            });
        }
        catch (SftpException e) {
            log.error("读取远程文件列表异常：{}", e.getMessage());
        }

        return files;
    }

    /**
     * 获取文件类型
     *
     * @param attrs 文件属性对象
     * @return 问价类型
     */
    private FileInfo.Type getFileType(SftpATTRS attrs) {
        if (attrs.isReg()) {
            return FileInfo.Type.REG;
        }
        if (attrs.isDir()) {
            return FileInfo.Type.DIR;
        }

        if (attrs.isLink()) {
            return FileInfo.Type.LNK;
        }

        if (attrs.isBlk()) {
            return FileInfo.Type.BLK;
        }

        if (attrs.isChr()) {
            return FileInfo.Type.CHR;
        }

        if (attrs.isFifo()) {
            return FileInfo.Type.FIFO;
        }

        if (attrs.isSock()) {
            return FileInfo.Type.SOCK;
        }

        throw new RuntimeException("找不到文件类型");
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
