/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.data.sync.prot;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ren.wxyz.tool.common.file.PathHelper;
import ren.wxyz.tool.data.sync.prot.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 访问本地文件
 *
 * @auther wxyz 2016-09-08_09:40
 * @since 1.0
 */
@Slf4j
@Setter
public class LocalClient {

    /**
     * 默认目录
     */
    private String workDir;

    /**
     * 构造客户端事，设置工作目录
     *
     * @param workDir 工作目录
     */
    public LocalClient(String workDir) {
        setWorkDir(workDir);
    }

    /**
     * 获取路径下的文件列表
     *
     * @param path 要列表的路径
     * @param subDir 是否处理子目录
     * @return 文件列表
     */
    public List<FileInfo> list(String path, boolean subDir) {
        // 根路径
        String rootPath = PathHelper.isAbsolute(path) ? path : PathHelper.join(null, this.workDir, path);
        rootPath = PathHelper.normalizePath(rootPath);
        rootPath = rootPath.endsWith(File.separator) ? rootPath : rootPath + File.separator;

        List<FileInfo> files = list(path);

        if (subDir) {
            int idx = 0;
            while (idx < files.size()) {
                FileInfo file = files.get(idx++);
                if (file.getFileType() == FileInfo.Type.DIR) {
                    List<FileInfo> tmp = list(file.getAbsolutePath());
                    files.addAll(tmp);
                }
            }
        }

        // 相对路径
        for (FileInfo fi : files) {
            fi.setRelativePath(fi.getAbsolutePath().replace(rootPath, ""));
        }

        return files;
    }

    /**
     * 获取路径下的文件列表，不包括子目录
     *
     * @param path 要列表的路径
     * @return 文件列表
     */
    private List<FileInfo> list(String path) {
        List<FileInfo> files = new ArrayList<>();
        File p = new File(PathHelper.isAbsolute(path) ? path : PathHelper.join("\\", this.workDir, path));

        if (!p.exists()) {
            return files;
        }
        // 遍历目录下的文件
        File[] filelist;
        if (p.isFile()) {
            filelist = new File[] {p};
        }
        else {
            filelist = p.listFiles();
        }

        for (File f : filelist) {
            FileInfo fi = new FileInfo();
            fi.setAbsolutePath(f.getAbsolutePath());
            fi.setFilename(f.getName());
            fi.setFileSize(f.length());
            fi.setModifyDate(new Date(f.lastModified()));
            fi.setFileType(getFileType(f));

            files.add(fi);
        }

        return files;
    }

    /**
     * 获取文件类型
     *
     * @param f 文件对象
     * @return 文件类型
     */
    private FileInfo.Type getFileType(File f) {
        if (f.isFile()) {
            return FileInfo.Type.REG;
        }

        if (f.isDirectory()) {
            return FileInfo.Type.DIR;
        }

        throw new RuntimeException("找不到文件类型");
    }
}
