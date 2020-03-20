package com.github.guang19.knife;



import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>反射工具类</p>
 */
public class ReflectionUtil
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

    /***************************************** Constructor **********************************************************/

    /**
     * <p>
     *     获取指定类的默认构造器,默认构造器必须为public
     *     否则句柄使用私有构造器会出现异常
     * </p>
     * @param clazz   指定的Class
     * @return        默认构造器MethodHandle
     */
    public static MethodHandle getDefaultConstructor(Class<?> clazz)
    {
        return lookupConstructor(clazz);
    }

    /**
     * <p>
     *    获取指定类的构造器，默认构造器必须为public
     *    否则句柄使用私有构造器会出现异常
     * </p>
     *
     * @param clazz   指定的Class
     * @param parameterClasses 构造器参数类型
     * @return        默认构造器MethodHandle
     */
    public static MethodHandle getPublicConstructor(Class<?> clazz,Class<?> ...parameterClasses)
    {
        return lookupConstructor(clazz,parameterClasses);
    }

    /**
     * 查找指定类的构造器句柄
     * @param clazz                     指定的Class
     * @param parameterClasses           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupConstructor(Class<?> clazz,Class<?> ...parameterClasses)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        AssertUtil.assertObjNull(parameterClasses,"parameter type cannot be null");
        try
        {
            return methodFinder.findConstructor(clazz,MethodType.methodType(void.class,parameterClasses));
        }
        catch (Throwable e)
        {
            //can not get constructor
            return null;
        }
    }

    /**
     * 获取指定类已声明的构造器
     * @param clazz                 指定的Class
     * @param parameterClasses       参数类型
     * @param <T>                   Class type
     * @return                      指定的构造器，如果构造器没有声明则为null
     */
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz,Class<?> ...parameterClasses)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        AssertUtil.assertObjNull(parameterClasses,"parameter type cannot be null");
        try
        {
            return clazz.getDeclaredConstructor(parameterClasses);
        }
        catch (NoSuchMethodException e)
        {
            //cannot get declared constructor
            return null;
        }
    }

    /**
     * 获取指定类的所有已声明的构造器
     * @param clazz     指定的Class
     * @return          所有已声明的构造器
     */
    public static Constructor<?>[] getDeclaredConstructors(Class<?> clazz)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        return clazz.getDeclaredConstructors();
    }

    /***************************************** Method **********************************************************/

    /**
     * <p>
     *     获取指定类的指定方法句柄,如果方法是private,那么方法句柄执行会出现异常
     * </p>
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param returnType                返回类型
     * @param parameterClasses           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterClasses)
    {
        return lookupPublicMethod(clazz, methodName, false, returnType,parameterClasses);
    }

    /**
     * <p>
     *     获取指定类的指定静态方法,如果方法是private,那么方法句柄执行会出现异常
     * </p>
     * @param clazz                     指定的Class
     * @param methodName                静态方法名
     * @param returnType                返回类型
     * @param parameterClasses           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicStaticMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterClasses)
    {
        return lookupPublicMethod(clazz, methodName, true, returnType,parameterClasses);
    }

    /**
     * 查找指定类的指定方法句柄
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param isStatic                  是否为静态方法
     * @param returnType                方法返回类型
     * @param parameterClasses           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupPublicMethod(Class<?> clazz, String methodName,boolean isStatic,Class<?> returnType,Class<?> ...parameterClasses)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        AssertUtil.assertObjNull(methodName,"method name cannot be null");
        AssertUtil.assertObjNull(returnType,"return type cannot be null");
        AssertUtil.assertObjNull(parameterClasses,"parameter type cannot be null");
        try
        {
            return isStatic ? methodFinder.findStatic(clazz, methodName, MethodType.methodType(returnType, parameterClasses)) :
                    methodFinder.findVirtual(clazz, methodName, MethodType.methodType(returnType, parameterClasses));
        }
        catch (NoSuchMethodException | IllegalAccessException e)
        {
            //can not get method
            return null;
        }
    }

    /**
     * 获取指定类的所有public方法(包括继承的方法,不包括protected)
     * @param clazz     指定的Class
     * @return          所有的方法
     */
    public static Method[] getPublicMethods(Class<?> clazz)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        return clazz.getMethods();
    }

    /**
     * 获取指定类的所有的已声明的方法
     * @param clazz              指定的Class
     * @return                   所有的已声明的方法
     */
    public static Method[] getDeclaredMethods(Class<?> clazz)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        return clazz.getDeclaredMethods();
    }

    /**
     * 通过指定的类和其父类(包括接口)，查找指定的方法
     * @param clazz              指定的Class
     * @param methodName         方法名
     * @param parameterClasses    参数类型
     * @return                   指定的方法，如果方法没有声明，那么返回null
     */
    public static Method getMethodByFindSuperClass(Class<?> clazz,String methodName,Class<?> ...parameterClasses)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        AssertUtil.assertObjNull(methodName,"method name cannot be null");
        AssertUtil.assertObjNull(parameterClasses,"parameter type name cannot be null");
        Method method = null;
        //尝试获取本类方法
        if((method = getMethod(clazz,methodName,parameterClasses)) == null)
        {
            //获取父类的方法
            if((method = getParentClassMethod(clazz,methodName,parameterClasses)) == null)
            {
                //获取接口的方法
                method = getInterfaceMethod(clazz,methodName,parameterClasses);
            }
        }
        return method;
    }


    /**
     * 在指定的类中查找指定的方法
     * @param clazz              指定的Class
     * @param methodName         方法名
     * @param parameterClasses   参数类型
     * @return                   要查找的方法，可能为null
     */
    private static Method getMethod(Class<?> clazz,String methodName,Class<?> ...parameterClasses)
    {
        Method method = null;
        try
        {
            //尝试获取public方法
            if((method = clazz.getMethod(methodName,parameterClasses)) != null)
            {
                return method;
            }
        }
        catch (NoSuchMethodException e)
        {}
        try
        {
            //尝试获取已声明方法
            if((method = clazz.getDeclaredMethod(methodName,parameterClasses)) != null)
            {
                return method;
            }
        }
        catch (NoSuchMethodException e)
        {}
        return null;
    }

    //获取父类方法
    private static Method getParentClassMethod(Class<?> clazz,String methodName,Class<?> ...parameterClasses)
    {
        Class<?> parentClass = clazz.getSuperclass();
        Method method = null;
        if(parentClass != null)
        {
            if((method = getMethod(parentClass,methodName,parameterClasses)) == null)
            {
                if((method = getParentClassMethod(parentClass,methodName,parameterClasses)) != null)
                {
                    return method;
                }
            }
        }
        return method;
    }

    //获取接口方法
    private static Method getInterfaceMethod(Class<?> clazz,String methodName,Class<?> ...parameterClasses)
    {
        Class<?>[] interfaces = clazz.getInterfaces();
        Method method = null;
        for (Class<?> interface0 : interfaces)
        {
            if((method = getMethod(interface0,methodName,parameterClasses)) != null)
            {
                return method;
            }
        }
        return null;
    }


    /***************************************** Field **********************************************************/

    /**
     * 获取指定类的public字段(包括继承)
     * @param clazz     指定的Class
     * @return          类的public字段数组
     */
    public static Field[] getFields(Class<?> clazz)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        return clazz.getFields();
    }

    /**
     * 获取指定类的所有已声明的字段
     * @param clazz     指定的Class
     * @return          所有已声明的字段
     */
    public static Field[] getDeclareFields(Class<?> clazz)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        return clazz.getDeclaredFields();
    }

    /**
     * 通过指定的类和其父类(包括结构),查找指定的字段
     * @param clazz         指定的Class
     * @param fieldName     字段名
     * @return              字段
     */
    public static Field getFieldByFindSuperClass(Class<?> clazz , String fieldName)
    {
        AssertUtil.assertObjNull(clazz,"class cannot be null");
        AssertUtil.assertObjNull(fieldName,"field name cannot be null");
        Field field = null;
        //尝试获取本类字段
        if((field = getField(clazz,fieldName)) == null)
        {
            //尝试获取父类字段
            if((field = getParentField(clazz,fieldName)) == null)
            {
                //尝试获取接口字段
                field = getInterfaceField(clazz,fieldName);
            }
        }
        return field;

    }

    /**
     * 在指定的类中查找指定的字段
     * @param clazz            指定的Class
     * @param fieldName        字段名
     * @return                 字段
     */
    private static Field getField(Class<?> clazz , String fieldName)
    {
        Field field = null;
        try
        {
            //尝试获取public字段
            if((field =  clazz.getField(fieldName)) != null)
            {
                return field;
            }
        }
        catch (NoSuchFieldException e)
        {}
        try
        {
            //尝试获取已声明字段
            if((field =  clazz.getDeclaredField(fieldName)) != null)
            {
                return field;
            }
        }
        catch (NoSuchFieldException e)
        {}
        return null;
    }

    //从父类获取字段
    private static Field getParentField(Class<?> clazz , String fieldName)
    {
        Class<?> parentClass = clazz.getSuperclass();
        Field field = null;
        if(parentClass != null)
        {
            if((field = getField(parentClass,fieldName)) == null)
            {
                return getParentField(parentClass,fieldName);
            }
        }
        return field;
    }

    //从接口获取字段
    private static Field getInterfaceField(Class<?> clazz , String fieldName)
    {
        Class<?>[] interfaces = clazz.getInterfaces();
        Field field = null;
        for (Class<?> interface0 : interfaces)
        {
            if((field = getField(interface0,fieldName)) != null)
            {
                return field;
            }
        }
        return null;
    }
}
