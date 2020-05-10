package com.github.guang19.knife;

import java.util.Collection;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>判空工具类</p>
 */
public class AssertUtils
{

    /**
     * 如果下标超出范围将抛出异常
     *
     * @param index 下标值
     * @param min   下标最小值
     * @param max   下标自大值
     */
    public static void exceptionIfIndexOutOfRange(int index, int min, int max)
    {
        if (index < min || index > max)
        {
            throw new IndexOutOfBoundsException(String.format("Index out of range, min index cannot be lower %d , max index cannot be exceed %d.", min, max));
        }
    }


    /**
     * 如果为字符串null或empty将抛出异常
     *
     * @param str     字符串
     * @param message 异常消息
     */
    public static void exceptionIfStrBlank(String str, String message)
    {
        if (str == null || str.isEmpty() || str.trim().isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }

    /***
     * 判断字符串是否为null或empty
     * @param str  字符串
     * @return 如果字符串为null或empty就返回true
     */
    public static boolean assertStrBlank(String str)
    {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }


    /***
     * 判断字符串是否为null或empty
     * @param str  字符串
     * @return 如果字符串不为null或empty就返回true
     */
    public static boolean assertStrNotBlank(String str)
    {
        return str != null && !str.isEmpty() && !str.trim().isEmpty();
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return 如果数组长度小于等于0，则返回true
     */
    public static boolean assertArrayEmpty(Object[] array)
    {
        return array.length <= 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 如果数组长度大于0，则返回true
     */
    public static boolean assertArrayNotEmpty(Object[] array)
    {
        return array.length > 0;
    }


    /**
     * 如果集合为null或empty将抛出异常
     *
     * @param collection 集合
     * @param message    异常消息
     */
    public static void exceptionIfCollectionEmpty(Collection<?> collection, String message)
    {
        if (collection == null || collection.isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 判断集合是否为null或empty
     *
     * @param collection 集合
     * @return 如果集合为null或empty就返回true
     */
    public static boolean assertCollectionEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否为不null或empty
     *
     * @param collection 集合
     * @return 如果集合不为null或empty就返回true
     */
    public static boolean assertCollectionNotEmpty(Collection<?> collection)
    {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 如果对象为null将抛出异常
     *
     * @param obj     对象
     * @param message 异常消息
     */
    public static void exceptionIfObjNull(Object obj, String message)
    {
        if (obj == null)
        {
            throw new NullPointerException(message);
        }
    }

    /**
     * 判断对象是否为null
     *
     * @param obj object
     * @return 如果为null就返回true
     */
    public static boolean assertObjNull(Object obj)
    {
        return obj == null;
    }

    /**
     * 判断对象是否不为null
     *
     * @param obj object
     * @return 如果不为null就返回true
     */
    public static boolean assertObjNotNull(Object obj)
    {
        return obj != null;
    }


    /**
     * 判断下标是否超出范围
     *
     * @param index 下标值
     * @param min   下标最小值
     * @param max   下标自大值
     */
    public static boolean assertIndexOutOfRange(int index, int min, int max)
    {
        if (index < min || index > max)
        {
            return false;
        }
        return true;
    }

}
