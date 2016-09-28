/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.common.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * 路径帮助类
 *
 * @auther wxyz 2016-09-07_17:48
 * @since 1.0
 */
public class PathHelper {

    /**
     * 拼接多个路径
     *
     * @param separator 分隔符
     * @param paths     后面多个路径
     * @return 拼接后的完整路径
     */
    public static String join(String separator, String... paths) {
        if (null == separator) {
            separator = File.separator;
        }
        if (null == paths || paths.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // 合并另外的路径
        for (String p : paths) {
            if (StringUtils.isNotBlank(p)) {
                String tmpPath = p.trim();
                if (sb.length() > 0) {
                    char lastChar = sb.charAt(sb.length() - 1);
                    // 原有路径不以分隔符结尾且新接路径也不已分隔符开头
                    if (!isSeparatorChar(lastChar) && !hasLeftSeparatorChar(tmpPath)) {
                        sb.append(separator);
                    }
                    // 原有路径以分隔符结尾且新接路径也已分隔符开头
                    if (isSeparatorChar(lastChar) && hasLeftSeparatorChar(tmpPath)) {
                        tmpPath = trimLeftSeparatorChar(tmpPath);
                    }
                }
                sb.append(tmpPath);
            }
        }

        return sb.toString();
    }

    /**
     * 判断是绝对路径
     *
     * @param path 路径字符串
     * @return
     */
    public static boolean isAbsolute(String path) {
        if (StringUtils.isBlank(path)) {
            throw new NullPointerException("path is blank.");
        }

        if (path.startsWith("/")) {
            return true;
        }

        if (SystemUtils.IS_OS_WINDOWS && path.charAt(1) == ':') {
            return true;
        }

        return false;
    }

    /**
     * 获取类的路径
     *
     * @param clz 被参考的类对象
     * @return 类路径
     */
    public static String getClassPath(Class<?> clz) {
        String path = clz.getResource("/").getPath();
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.substring(1, path.length()).replace('/', '\\');
        }

        return path;
    }

    /**
     * 标准化路径，将给定的路径标准化为当前操作系统的路径
     *
     * @param path 路径
     * @return 标准化之后的路径
     */
    public static String normalizePath(final String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }

        String resPath = path;
        if (SystemUtils.IS_OS_WINDOWS) {
            if (resPath.startsWith("/")) {
                resPath = resPath.substring(1, resPath.length());
            }
            resPath = resPath.replace('/', File.separatorChar);
        }
        else {
            resPath = resPath.replace('\\', File.separatorChar);
        }

        return resPath;
    }

    /**
     * 去除路径左侧的分隔符
     *
     * @param path 路径字符串
     * @return
     */
    private static String trimLeftSeparatorChar(String path) {
        if (path.startsWith("/") || path.startsWith("\\")) {
            return path.substring(1, path.length());
        }

        return path;
    }

    /**
     * 路径左侧是否有分隔符
     *
     * @param path 路径字符串
     * @return
     */
    private static boolean hasLeftSeparatorChar(String path) {
        if (path.startsWith("/") || path.startsWith("\\")) {
            return true;
        }

        return false;
    }

    /**
     * 路径右侧是否有分隔符
     *
     * @param path 路径字符串
     * @return
     */
    private static boolean hasRightSeparatorChar(String path) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否是分隔符
     *
     * @param str 字符串
     * @return
     */
    private static boolean isSeparatorChar(String str) {
        if ("/".equals(str) || "\\".equals(str)) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符是否是分隔符
     *
     * @param ch 字符
     * @return
     */
    private static boolean isSeparatorChar(char ch) {
        if ('/' == ch || '\\' == ch) {
            return true;
        }

        return false;
    }
}
