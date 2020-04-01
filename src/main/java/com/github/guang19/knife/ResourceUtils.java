package com.github.guang19.knife;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  静态资源工具类
 * </p>
 */
public class ResourceUtils
{
    /**
     * 加载类路径下的资源文件
     * @param file  类路径下的资源文件
     * @return      静态资源文件的内容流
     */
    public static InputStream loadClasspathResource(String file)
    {
        AssertUtils.exceptionIfStrBlank(file,"file path cannot not be blank");
        return ClassLoader.getSystemResourceAsStream(file);
    }


    /**
     * 加载系统文件路径下的资源文件
     * @param file  系统路径下的资源文件
     * @return      静态资源文件的内容流
     */
    public static InputStream loadSystemFileResource(String file)
    {
        AssertUtils.exceptionIfStrBlank(file,"file path cannot not be blank");
        try
        {
            return new FileInputStream(file);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
