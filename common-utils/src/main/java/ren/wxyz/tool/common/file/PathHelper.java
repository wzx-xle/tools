/**
 * Copyright (C) 2001-2016 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tool.common.file;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;

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
     * @param path 第一个路径
     * @param paths 后面多个路径
     * @return 拼接后的完整路径
     */
    public static String join(String path, String... paths) {
        if (null == path) {
            path = "";
        }

        StringBuilder sb = new StringBuilder(path.trim());

        if (null == paths || paths.length == 0) {
            return sb.toString();
        }

        for (String p : paths) {
            if (StringUtils.isNotBlank(p)) {
                if (sb.length() > 0
                        && sb.charAt(sb.length() - 1) != '/'
                        && !p.startsWith("/")) {
                    sb.append("/");
                }
                sb.append(p.trim());
            }
        }

        return sb.toString();
    }
}
