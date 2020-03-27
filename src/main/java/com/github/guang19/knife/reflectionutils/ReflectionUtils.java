package com.github.guang19.knife.reflectionutils;

import com.github.guang19.knife.AssertUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>反射工具类</p>
 */
public class ReflectionUtils
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
            throw new NoSuchMethodException("cannot get default constructor.");
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
     * @param parameterTypes 构造器参数类型
     * @return        默认构造器MethodHandle
     */
    public static MethodHandle getPublicConstructor(Class<?> clazz,Class<?> ...parameterTypes)
    {
        return lookupConstructor(clazz,parameterTypes);
    }

    /**
     * 查找指定类的构造器句柄
     * 只能获取public或protected级别的构造器
     * @param clazz                     指定的Class
     * @param parameterTypes           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupConstructor(Class<?> clazz,Class<?> ...parameterTypes)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfObjNull(parameterTypes,"parameter type can be omitted , but cannot be null.");
        try
        {
            return methodFinder.findConstructor(clazz,MethodType.methodType(void.class,parameterTypes));
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
     * @param parameterTypes       参数类型
     * @param <T>                   Class type
     * @return                      指定的构造器，如果构造器没有声明则为null
     */
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz,Class<?> ...parameterTypes)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfObjNull(parameterTypes,"parameter type can be omitted , but cannot be null.");
        try
        {
            return clazz.getDeclaredConstructor(parameterTypes);
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
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        return clazz.getDeclaredConstructors();
    }

    /***************************************** Method **********************************************************/

    /**
     * <p>
     *     获取指定类的指定方法句柄
     *     只能获取public或protected级别的方法句柄，对于private，返回null
     * </p>
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param returnType                返回类型
     * @param parameterTypes           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterTypes)
    {
        return lookupPublicMethod(clazz, methodName, false, returnType,parameterTypes);
    }

    /**
     * <p>
     *    获取指定类的指定静态方法句柄
     *    只能获取public或protected级别的方法句柄，对于private，返回null
     * </p>
     * @param clazz                     指定的Class
     * @param methodName                静态方法名
     * @param returnType                返回类型
     * @param parameterTypes           参数类型
     * @return                          方法句柄
     */
    public static MethodHandle getPublicStaticMethod(Class<?> clazz, String methodName,Class<?> returnType,Class<?> ...parameterTypes)
    {
        return lookupPublicMethod(clazz, methodName, true, returnType,parameterTypes);
    }

    /**
     * 查找指定类的指定方法句柄
     * @param clazz                     指定的Class
     * @param methodName                方法名
     * @param isStatic                  是否为静态方法
     * @param returnType                方法返回类型
     * @param parameterTypes           方法参数类型
     * @return                          方法句柄
     */
    private static MethodHandle lookupPublicMethod(Class<?> clazz, String methodName,boolean isStatic,Class<?> returnType,Class<?> ...parameterTypes)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(methodName,"method name cannot be blank.");
        AssertUtils.exceptionIfObjNull(returnType,"return type cannot be null.");
        AssertUtils.exceptionIfObjNull(parameterTypes,"parameter type can be omitted , but cannot be null.");
        try
        {
            return isStatic ? methodFinder.findStatic(clazz, methodName, MethodType.methodType(returnType, parameterTypes)) :
                    methodFinder.findVirtual(clazz, methodName, MethodType.methodType(returnType, parameterTypes));
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
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        return clazz.getMethods();
    }

    /**
     * 获取指定类的所有的已声明的方法
     * @param clazz              指定的Class
     * @return                   所有的已声明的方法
     */
    public static Method[] getDeclaredMethods(Class<?> clazz)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        return clazz.getDeclaredMethods();
    }


    /**
     * <p>
     *     查找指定类的指定方法(包括public，declare),
     * </p>
     * @param clazz              指定的Class
     * @param methodName         方法名
     * @param parameterTypes    参数类型
     * @return                   指定的方法，如果方法没有声明，那么返回null
     */
    public static Method getMethod(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(methodName,"method name cannot be blank.");
        AssertUtils.exceptionIfObjNull(parameterTypes,"parameter type can be omitted , but cannot be null.");
        return findMethod(clazz,methodName,parameterTypes);
    }

    /**
     * <p>
     *
     *     查找指定类的指定方法(包括public，declare),
     *     如果在指定的类没有查找到，就寻找其父类的方法(包括public，declare),
     *     如果其父类也没有查找到，就寻找其接口方法
     *
     * </p>
     *
     *
     * @param clazz              指定的Class
     * @param methodName         方法名
     * @param parameterTypes    参数类型
     * @return                   指定的方法，如果方法没有声明，那么返回null
     */
    public static Method getMethodBySuperClass(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(methodName,"method name cannot be blank.");
        AssertUtils.exceptionIfObjNull(parameterTypes,"parameter type can be omitted , but cannot be null.");
        Method method = null;
        //尝试获取本类方法
        if((method = findMethod(clazz,methodName,parameterTypes)) == null)
        {
            //获取父类的方法
            if((method = findMethodBySuperClass(clazz,methodName,parameterTypes)) == null)
            {
                //获取接口的方法
                method = findMethodByInterfaces(clazz,methodName,parameterTypes);
            }
        }
        return method;
    }


    //在指定的类中查找指定的方法(包括public，declare),
    private static Method findMethod(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        Method method = null;
        try
        {
            //尝试获取public方法
            if((method = clazz.getMethod(methodName,parameterTypes)) != null)
            {
                return method;
            }
        }
        catch (NoSuchMethodException e)
        {}
        try
        {
            //尝试获取declare方法
            if((method = clazz.getDeclaredMethod(methodName,parameterTypes)) != null)
            {
                return method;
            }
        }
        catch (NoSuchMethodException e)
        {}
        return null;
    }

    //获取父类方法
    private static Method findMethodBySuperClass(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        Class<?> parentClass = clazz.getSuperclass();
        Method method = null;
        if(parentClass != null)
        {
            if((method = findMethod(parentClass,methodName,parameterTypes)) == null)
            {
                if((method = findMethodBySuperClass(parentClass,methodName,parameterTypes)) != null)
                {
                    return method;
                }
            }
        }
        return method;
    }

    //获取接口方法
    private static Method findMethodByInterfaces(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        Class<?>[] interfaces = clazz.getInterfaces();
        Method method = null;
        for (Class<?> interface0 : interfaces)
        {
            if((method = findMethod(interface0,methodName,parameterTypes)) != null)
            {
                return method;
            }
        }
        return null;
    }


    /**
     * 获取指定类的指定字段的getter方法
     * @param clazz         class
     * @param fieldName     字段名
     * @return              getter方法
     */
    public static Method getGetter(Class<?> clazz,String fieldName)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(fieldName,"field name cannot be blank.");
        try
        {
            return new PropertyDescriptor(fieldName,clazz).getReadMethod();
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    /**
     * 获取指定类的指定字段的setter方法
     * @param clazz         class
     * @param fieldName     字段名
     * @return              setter方法
     */
    public static Method getSetter(Class<?> clazz,String fieldName)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(fieldName,"field name cannot be blank.");
        try
        {
            return new PropertyDescriptor(fieldName,clazz).getWriteMethod();
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    /***************************************** Parameter **********************************************************/

    /**
     * 获取指定方法的所有参数
     * @param method  指定方法
     * @return        参数数组
     */
    public static Parameter[] getMethodParameters(Method method)
    {
        AssertUtils.exceptionIfObjNull(method,"method cannot be null.");
        return method.getParameters();
    }


    /**
     * 获取指定方法的指定参数
     * @param method            指定方法
     * @param parameterName     指定的参数名
     * @return                  获取的参数
     */
    public static Parameter getMethodParameter(Method method,String parameterName)
    {
        AssertUtils.exceptionIfObjNull(method,"method cannot be null.");
        AssertUtils.exceptionIfStrBlank(parameterName,"parameter name cannot be blank.");

        return null;
    }


    /**
     * 获取方法参数数量
     * @param method    指定方法
     * @return          参数数量
     */
    public static int getMethodParameterSize(Method method)
    {
        AssertUtils.exceptionIfObjNull(method,"method cannot be null.");
        return method.getParameterCount();
    }

    /***************************************** Field **********************************************************/

    /**
     * 获取指定类的public字段(包括继承的子段，不包括protected)
     * @param clazz     指定的Class
     * @return          类的public字段数组
     */
    public static Field[] getFields(Class<?> clazz)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        return clazz.getFields();
    }

    /**
     * 获取指定类的所有已声明的字段
     * @param clazz     指定的Class
     * @return          所有已声明的字段
     */
    public static Field[] getDeclareFields(Class<?> clazz)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        return clazz.getDeclaredFields();
    }


    /**
     * <p>
     *     查找指定类的指定字段(包括public，declare)
     * </p>
     *
     * @param clazz         指定的Class
     * @param fieldName     字段名
     * @return              字段
     */
    public static Field getField(Class<?> clazz,String fieldName)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(fieldName,"field name cannot be blank.");
        return findField(clazz,fieldName);
    }

    /**
     * <p>
     *     查找指定类的指定字段(包括public，declare),
     *     如果在指定的类没有查找到，就寻找其父类的字段(包括public，declare),
     *     如果其父类也没有查找到，就寻找其接口字段
     * </p>
     *
     * @param clazz         指定的Class
     * @param fieldName     字段名
     * @return              字段
     */
    public static Field getFieldByFindSuperClass(Class<?> clazz , String fieldName)
    {
        AssertUtils.exceptionIfObjNull(clazz,"class cannot be null.");
        AssertUtils.exceptionIfStrBlank(fieldName,"field name cannot be blank.");
        Field field = null;
        //尝试获取本类字段
        if((field = findField(clazz,fieldName)) == null)
        {
            //尝试获取父类字段
            if((field = findFieldBySuperClass(clazz,fieldName)) == null)
            {
                //尝试获取接口字段
                field = findFieldByInterfaces(clazz,fieldName);
            }
        }
        return field;

    }


    //在指定的类中查找指定的字段
    private static Field findField(Class<?> clazz , String fieldName)
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
    private static Field findFieldBySuperClass(Class<?> clazz , String fieldName)
    {
        Class<?> parentClass = clazz.getSuperclass();
        Field field = null;
        if(parentClass != null)
        {
            if((field = findField(parentClass,fieldName)) == null)
            {
                return findFieldBySuperClass(parentClass,fieldName);
            }
        }
        return field;
    }

    //从接口获取字段
    private static Field findFieldByInterfaces(Class<?> clazz , String fieldName)
    {
        Class<?>[] interfaces = clazz.getInterfaces();
        Field field = null;
        for (Class<?> interface0 : interfaces)
        {
            if((field = findField(interface0,fieldName)) != null)
            {
                return field;
            }
        }
        return null;
    }


    /***************************************** Annotation **********************************************************/


//    public static A

}
