package com.github.guang19.knife;

import com.github.guang19.knife.reflectionutils.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author yangguang
 * @date 2020/3/26
 * @description <p>JNI工具</p>
 */
public class JNIUtils
{

    public static final String JAVA_LIBRARY_PATH_PROPERTIES_KEY = "java.library.path";

    private static final String JAVA_LIBRARY_PATH = "sys_paths";

    /**
     * 添加java动态连接库搜索目录
     * 动态连接搜索目录是JVM在加载JNI动态链接库时需要搜索的目录，此目录属性只会加载一次，
     * 直接使用System.setProperty("java.library.path")是没用的。
     *
     * 如果我们想要加载我们自定义的JNI动态库的话，需要外部指定-Djava.library.path=xxx
     * 但是这样做灵活性太差，所以以程序的方式来对这个目录进行设置
     *
     * 此方法的思路来源于：[简书](https://www.jianshu.com/p/1f2cfb0aba7f)
     *
     * @param dir   新增的动态链接库搜索的目录
     */
    public static void addJavaLibraryPath(String dir)
    {
        if(dir != null)
        {
            //在不影响原动态库搜索目录的路径下，新增动态库搜索目录
            System.setProperty(JAVA_LIBRARY_PATH_PROPERTIES_KEY,System.getProperty(JAVA_LIBRARY_PATH_PROPERTIES_KEY)+":"+dir);
            try
            {
                //ClassLoader的sys_paths属性就是JVM加载动态库的路径，如果把此路径为null，那么jvm就会重新加载动态库的路径
                //这样就能够使得JVM可以使用到我们指定的dir来搜索路径
                final Field usrPaths = ReflectionUtils.getFieldByFindSuperClass(ClassLoader.class,JAVA_LIBRARY_PATH);
                if(usrPaths == null)
                {
                    throw new NoSuchFieldException("cannot found field: usr_paths in class loader.");
                }
                usrPaths.setAccessible(true);
                //设置动态库搜索路径为null
                usrPaths.set(null,null);
            }
            catch (NoSuchFieldException | IllegalAccessException e)
            {
                throw new RuntimeException("cannot set java library path.",e);
            }
        }
    }
}
