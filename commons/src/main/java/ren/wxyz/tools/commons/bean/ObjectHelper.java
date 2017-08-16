/**
 * Copyright (C) 2013-2017 wxyz <hyhjwzx@126.com>
 * <p/>
 * This program can be distributed under the terms of the GNU GPL Version 2.
 * See the file LICENSE.
 */
package ren.wxyz.tools.commons.bean;

import java.lang.reflect.Constructor;

/**
 * 对象和类帮助类
 *
 * @author wxyz 2017-08-16_21:46
 * @since 1.0
 */
public class ObjectHelper {
    private ObjectHelper() {}

    /**
     * 通过类的全名获取到类对象
     *
     * @param fullName 类全名，包含包名
     * @return 类对象，{@code null} 找不到类时返回
     */
    public static Class<?> getClass(final String fullName) {
        try {
            // 一般尽量采用这种形式
            return Class.forName(fullName);
        }
        catch (ClassNotFoundException e) {
            System.out.println("加载类失败，没有找到该类！" + e.getMessage());
        }

        return null;
    }

    /**
     * 获取类的一个实例
     *
     * @param classFullName 类全名，包含包名
     * @param args 调用的类的构造器参数
     * @param <T> 返回的类对象的类型
     * @return 一个实例，{@code null} 对象创建失败时返回
     */
    public static <T> T getInstance(final String classFullName, Object... args) {
        Class<?> tmp = getClass(classFullName);
        if (tmp != null) {
            try {
                Object obj = null;
                // 得到参数类型列表
                Class<?>[] paramTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                }
                // 得到指定的构造器
                Constructor<?> con = tmp.getConstructor(paramTypes);
                // 创建实例
                obj = con.newInstance(args);

                return (T) obj;
            }
            catch (Exception e) {
                System.out.println("对象创建失败！" + e.getMessage());
            }
        }

        return null;
    }
}
