package com.github.guang19.knife.timeutils;

import com.github.guang19.knife.JNIUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yangguang
 * @date 2020/3/26
 * @description <p>系统时间类</p>
 */
public final class SystemTime
{
    static
    {
        try
        {
            //更改JVM加载JNI动态库的路径，并加载 SystemTime 库
            addJavaLibraryPath();
            System.loadLibrary("SystemTime");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * 使用clock_gettime获取当前系统时间戳
     *
     * @return 当前系统时间戳
     */
    public static native long currentTimeMillis();


    //SystemTime lib文件
    private static final String SYSTEMTIME_LIB_FILE = "jni/time/libSystemTime.so";



    /**
     * 更改jvm加载JNI动态库的路径，使得jvm能够加载自定义的jni库
     */
    private static void addJavaLibraryPath()
    {
        //获取jni动态库文件的的位置
        URL resource = ClassLoader.getSystemResource(SYSTEMTIME_LIB_FILE);
        try
        {
           if(resource == null)
           {
               throw new FileNotFoundException();
           }
           //获取jni文件所在的目录
           Path jniDir = Paths.get(resource.toURI()).getParent();
           //更改JVM搜索JNI动态库的路径
           JNIUtils.addJavaLibraryPath(jniDir.toString());
        }
        catch (FileNotFoundException | URISyntaxException e)
        {
            throw new RuntimeException("cannot found system time lib of knife.");
        }
    }
}
