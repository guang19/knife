package com.github.guang19.knife;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>反射工具类</p>
 */
public class ClassUtil
{
    //方法查找器
    private static final MethodHandles.Lookup methodFinder;

    static
    {
        methodFinder = MethodHandles.lookup();
    }

    /**
     * 以默认构造器创建对象
     * @param clazz     指定的Class
     * @param <T>       Class type
     * @return          Class实例
     * @throws Throwable 创建对象过程中的错误或异常，很大几率是反射异常。
     */
    @SuppressWarnings("unchecked")
    public static  <T> T createInstanceWithDefaultConstructor(Class<T> clazz) throws Throwable
    {
        MethodHandle defaultConstructor = getDefaultConstructor(clazz);
        if(defaultConstructor == null)
        {
            throw new NoSuchMethodException("cannot get default constructor");
        }
        return (T)defaultConstructor.invoke();
    }

    /**
     * 获取指定类的默认构造器
     * @param clazz   指定的Class
     * @return        默认构造器MethodHandle
     */
    public static MethodHandle getDefaultConstructor(Class<?> clazz)
    {
        return lookupConstructor(clazz);
    }

    /**
     * 获取指定类的构造器
     * @param clazz   指定的Class
     * @param parameterClazzs 构造器参数类型
     * @return        默认构造器MethodHandle
     */
    public static MethodHandle getConstructor(Class<?> clazz,Class<?> ...parameterClazzs)
    {
        return lookupConstructor(clazz,parameterClazzs);
    }

    /**
     * 查找指定类的构造器句柄
     * @param clazz                     指定的Class
     * @param parameterClazzs           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupConstructor(Class<?> clazz,Class<?> ...parameterClazzs)
    {
        EmptyUtil.assertObjNull(clazz,"class cannot be null");
        EmptyUtil.assertObjNull(parameterClazzs,"parameter type cannot be null");
        try
        {
            return methodFinder.findConstructor(clazz,MethodType.methodType(void.class,parameterClazzs));
        }
        catch (Throwable e)
        {
            //can not get constructor
            return null;
        }
    }

    /**
     * 获取指定类的指定方法
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param returnType                返回类型
     * @param parameterClazzs           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterClazzs)
    {
        return lookupPublicMethod(clazz, methodName, false, returnType,parameterClazzs);
    }

    /**
     * 获取指定类的指定静态方法
     * @param clazz                     指定的Class
     * @param methodName                静态方法名
     * @param returnType                返回类型
     * @param parameterClazzs           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicStaticMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterClazzs)
    {
        return lookupPublicMethod(clazz, methodName, true, returnType,parameterClazzs);
    }

    /**
     * 查找指定类的指定方法句柄
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param isStatic                  是否为静态方法
     * @param returnType                方法返回类型
     * @param parameterClazzs           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupPublicMethod(Class<?> clazz, String methodName,boolean isStatic,Class<?> returnType,Class<?> ...parameterClazzs)
    {
        EmptyUtil.assertObjNull(clazz,"class cannot be null");
        EmptyUtil.assertObjNull(methodName,"method name cannot be null");
        EmptyUtil.assertObjNull(returnType,"return type cannot be null");
        EmptyUtil.assertObjNull(parameterClazzs,"parameter type cannot be null");
        try
        {
            return isStatic ? methodFinder.findStatic(clazz, methodName, MethodType.methodType(returnType, parameterClazzs)) :
                    methodFinder.findVirtual(clazz, methodName, MethodType.methodType(returnType, parameterClazzs));
        }
        catch (NoSuchMethodException | IllegalAccessException e)
        {
            //can not get method
            return null;
        }
    }

}
