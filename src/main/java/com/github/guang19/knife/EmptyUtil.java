package com.github.guang19.knife;

import java.util.Collection;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>判空工具类</p>
 */
public class EmptyUtil
{

    /**
     * 判断字符串是否为空,如果为null或empty将抛出异常
     * @param str          字符串
     * @param message      异常消息
     */
    public static void assertStrBlank(String str,String message)
    {
        if(str == null || str.isEmpty())
        {
            throw new NullPointerException(message);
        }
    }

    /**
     * 判断集合是否为空,如果为null或empty将抛出异常
     * @param collection    集合
     * @param message      异常消息
     */
    public static void assertCollectionEmpty(Collection<?> collection,String message)
    {
        if(collection == null || collection.isEmpty())
        {
            throw new NullPointerException(message);
        }
    }

    /**
     * 判断对象是否为空,如果为null将抛出异常
     * @param obj          对象
     * @param message      异常消息
     */
    public static void assertObjNull(Object obj,String message)
    {
        if(obj == null)
        {
            throw new NullPointerException(message);
        }
    }
}
