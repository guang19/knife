package com.github.guang19.knife.reflectionutils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
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
     * @return                      指定的构造器，如果构造器没有声明则为null
     */
    public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz,Class<?> ...parameterTypes)
    {
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
        return clazz.getDeclaredConstructors();
    }

    /**
     * 判断指定的类是否有公共的构造器
     * @param clazz             指定的类
     * @param parameterTypes    参数类型
     * @return                  如果有返回true，否则返回false
     */
    public static boolean hasPublicConstructor(Class<?> clazz,Class<?> ...parameterTypes)
    {
        try
        {
            return clazz.getConstructor(parameterTypes) != null;
        }
        catch (NoSuchMethodException e)
        {
            //没有找到构造器
            return false;
        }
    }

    /**
     * 判断指定的类是否有声明的构造器
     * @param clazz             指定的类
     * @param parameterTypes    参数类型
     * @return                  如果有返回true，否则返回false
     */
    public static boolean hasDeclaredConstructor(Class<?> clazz,Class<?> parameterTypes)
    {
        try
        {
            return clazz.getDeclaredConstructor(parameterTypes) != null;
        }
        catch (NoSuchMethodException e)
        {
            //没有找到构造器
            return false;
        }
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
        return clazz.getMethods();
    }

    /**
     * 获取指定类的所有的已声明的方法
     * @param clazz              指定的Class
     * @return                   所有的已声明的方法
     */
    public static Method[] getDeclaredMethods(Class<?> clazz)
    {
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
    public static Method getGetterMethod(Class<?> clazz,String fieldName)
    {
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
    public static Method getSetterMethod(Class<?> clazz,String fieldName)
    {
        try
        {
            return new PropertyDescriptor(fieldName,clazz).getWriteMethod();
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    /**
     * 判断指定类是否拥有指定公共方法
     * @param clazz             类
     * @param methodName        方法名
     * @param parameterTypes    参数类型
     * @return                  是否拥有指定公共方法
     */
    public static boolean hasPublicMethod(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        try
        {
            return clazz.getMethod(methodName, parameterTypes) != null;
        }
        catch (NoSuchMethodException e)
        {
            //没有找到方法
            return false;
        }
    }

    /**
     * 判断指定类是否拥有已声明的方法
     * @param clazz             类
     * @param methodName        方法名
     * @param parameterTypes    参数类型
     * @return                  是否拥有已声明的方法
     */
    public static boolean hasDeclaredMethod(Class<?> clazz,String methodName,Class<?> ...parameterTypes)
    {
        try
        {
            return clazz.getDeclaredMethod(methodName, parameterTypes) != null;
        }
        catch (NoSuchMethodException e)
        {
            //没有找到方法
            return false;
        }
    }


    /**
     * 判断类是否有指定的Getter方法
     * @param clazz         指定的类
     * @param fieldName     字段名
     * @return              是否有指定的Getter方法
     */
    public static boolean hasGetterMethod(Class<?> clazz,String fieldName)
    {
        return getGetterMethod(clazz,fieldName) != null;
    }


    /**
     * 判断类是否有指定的Setter方法
     * @param clazz         指定的类
     * @param fieldName     字段名
     * @return              是否有指定的Setter方法
     */
    public static boolean hasSetterMethod(Class<?> clazz,String fieldName)
    {
        return getSetterMethod(clazz,fieldName) != null;
    }

    /***************************************** Parameter **********************************************************/

    /**
     * 获取构造器的参数数量
     * @param constructor   指定构造器
     * @return              参数数量
     */
    public static int getConstructorParameterSize(Constructor<?> constructor)
    {
        return constructor.getParameterCount();
    }

    /**
     * 获取构造器的所有参数
     * @param constructor   指定构造器
     * @return              参数数组
     */
    public static Parameter[] getConstructorParameters(Constructor<?> constructor)
    {
        return constructor.getParameters();
    }

    /**
     * 获取构造器指定位置的参数
     * @param constructor   指定构造器
     * @param index         参数下标
     * @return              参数
     */
    public static Parameter getConstructorParameter(Constructor<?> constructor, int index)
    {
        return constructor.getParameters()[index];
    }

    /**
     * 获取方法参数数量
     * @param method    指定方法
     * @return          参数数量
     */
    public static int getMethodParameterSize(Method method)
    {
        return method.getParameterCount();
    }


    /**
     * 获取指定方法的所有参数名
     * @param method    指定方法
     * @return          参数名数组
     */
    public static String[] getMethodParameterNames(Method method)
    {
        //由于java8需要指定jvm -parameters启动参数才能获取到方法参数名，不太灵活，所以此处使用ASM库来获取方法参数名
        return MethodClassVisitor.getMethodParameterNames(method);
    }


    /**
     * 获取指定方法的所有参数
     * @param method  指定方法
     * @return        参数数组
     */
    public static Parameter[] getMethodParameters(Method method)
    {
        return method.getParameters();
    }


    /**
     * 获取指定方法的指定参数
     * @param method            指定方法
     * @param parameterName     指定的参数名
     * @return                  获取的参数
     */
    public static Parameter getMethodParameter(Method method,String parameterName,Class<?> parameterType)
    {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = MethodClassVisitor.getMethodParameterNames(method);
        for (int i = 0; i < parameters.length; ++i)
        {
            if (parameters[i].getType().equals(parameterType) && parameterName.equals(parameterNames[i]))
            {
                return parameters[i];
            }
        }
        return null;
    }

    /**
     * 获取方法的指定位置的参数
     * @param method            指定方法
     * @param index             参数下标
     * @return                  参数
     */
    public static Parameter getMethodParameter(Method method,int index)
    {
        return method.getParameters()[index];
    }
    
    
    /***************************************** Field **********************************************************/

    /**
     * 获取指定类的public字段(包括继承的子段，不包括protected)
     * @param clazz     指定的Class
     * @return          类的public字段数组
     */
    public static Field[] getFields(Class<?> clazz)
    {
        return clazz.getFields();
    }

    /**
     * 获取指定类的所有已声明的字段
     * @param clazz     指定的Class
     * @return          所有已声明的字段
     */
    public static Field[] getDeclareFields(Class<?> clazz)
    {
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

    /**
     * 判断指定类是否拥有指定字段(public ,declare)
     * @param clazz         指定的类
     * @param fieldName     字段名
     * @return              是否拥有指定字段
     */
    public static boolean hasField(Class<?> clazz,String fieldName)
    {
        return findField(clazz,fieldName) != null;
    }

    /***************************************** Annotation **********************************************************/

    /**
     * 获取指定类的所有注解
     * @param clazz     指定的class
     * @return          注解数组
     */
    public static Annotation[] getClassAnnotations(Class<?> clazz)
    {
        return clazz.getAnnotations();
    }

    /**
     * 获取指定类的指定类型的注解
     * @param clazz             指定的class
     * @param annotationType    注解类型
     * @return                  指定的注解
     */
    public static <A extends Annotation> A getClassAnnotation(Class<?> clazz , Class<A> annotationType)
    {
        return clazz.getAnnotation(annotationType);
    }

    /**
     * 获取指定类的相同类型的注解
     * @param clazz             指定的class
     * @param annotationType    注解类型
     * @return                  指定的注解
     */
    public static <A extends Annotation> A[] getClassSameAnnotations(Class<?> clazz , Class<A> annotationType)
    {
        return clazz.getAnnotationsByType(annotationType);
    }

    /**
     * 获取构造器的所有注解
     * @param constructor   指定构造器
     * @return              注解数组
     */
    public static Annotation[] getConstructorAnnotations(Constructor<?> constructor)
    {
        return constructor.getAnnotations();
    }

    /**
     * 获取指定构造器的指定类型的注解
     * @param constructor        指定构造器
     * @param annotationType    注解类型
     * @return                  指定的注解
     */
    public static <A extends Annotation> A getConstructorAnnotation(Constructor<?> constructor, Class<A> annotationType)
    {
        return constructor.getAnnotation(annotationType);
    }

    /**
     * 获取指定构造器的相同类型的所有注解
     * @param constructor        指定构造器
     * @param annotationType    注解类型
     * @return                  指定的注解
     */
    public static <A extends Annotation> A[] getConstructorSameAnnotations(Constructor<?> constructor, Class<A> annotationType)
    {
        return constructor.getAnnotationsByType(annotationType);
    }

    /**
     * 获取构造器参数的所有注解
     * @param constructor   指定构造器
     * @return              所有参数上的所有注解
     */
    public static Annotation[][] getConstructorParametersAnnotations(Constructor<?> constructor)
    {
        return constructor.getParameterAnnotations();
    }

    /**
     * 获取构造器指定参数的所有注解
     * @param constructor   指定构造器
     * @param index         参数下标
     * @return              参数上的所有注解
     */
    public static Annotation[] getConstructorParameterAnnotations(Constructor<?> constructor,int index)
    {
        return constructor.getParameterAnnotations()[index];
    }


    /**
     * 获取指定方法的所有注解
     * @param method   指定方法
     * @return         注解数组
     */
    public static Annotation[] getMethodAnnotations(Method method)
    {
        return method.getAnnotations();
    }

    /**
     * 获取指定方法的指定类型的注解
     * @param method   指定方法
     * @param annotationType   注解类型
     * @return         指定注解
     */
    public static <A extends Annotation> A getMethodAnnotation(Method method,Class<A> annotationType)
    {
        return method.getAnnotation(annotationType);
    }

    /***
     * 获取指定方法的相同类型的所有注解
     * @param method 指定方法
     * @param annotationType 注解类型
     * @return          相同类型的所有注解
     */
    public static <A extends Annotation> A[] getMethodSameAnnotations(Method method,Class<A> annotationType)
    {
        return method.getAnnotationsByType(annotationType);
    }


    /**
     * 获取指定方法参数的所有注解
     * @param method   指定方法
     * @return         所有参数的所有注解
     */
    public static Annotation[][] getMethodParametersAnnotations(Method method)
    {
        return method.getParameterAnnotations();
    }

    /**
     * 获取指定方法指定参数的所有注解
     * @param method   指定方法
     * @param index    参数下标
     * @return         参数上的所有注解
     */
    public static Annotation[] getMethodParameterAnnotations(Method method, int index)
    {
        return method.getParameterAnnotations()[index];
    }

    /***
     * 获取指定参数的所有注解
     * @param parameter 指定参数
     * @return          参数的所有注解
     */
    public static Annotation[] getParameterAnnotations(Parameter parameter)
    {
        return parameter.getAnnotations();
    }

    /***
     * 获取指定参数的指定类型的注解
     * @param parameter 指定参数
     * @param annotationType 注解类型
     * @return          要获取的注解
     */
    public static <A extends Annotation> A getParameterAnnotation(Parameter parameter,Class<A> annotationType)
    {
        return parameter.getAnnotation(annotationType);
    }

    /***
     * 获取指定参数的相同类型的所有注解
     * @param parameter 指定参数
     * @param annotationType 注解类型
     * @return          相同类型的所有注解
     */
    public static <A extends Annotation> A[] getParameterSameAnnotations(Parameter parameter,Class<A> annotationType)
    {
        return parameter.getAnnotationsByType(annotationType);
    }

    /**
     * 获取指定字段的所有注解
     * @param field                 指定字段
     * @return                      获取的注解
     */
    public static Annotation[] getFiledAnnotations(Field field)
    {
        return field.getAnnotations();
    }

    /**
     * 获取指定字段的指定类型的注解
     * @param field                 指定字段
     * @param annotationType        注解类型
     * @return                      获取的注解
     */
    public static  <A extends Annotation> A getFieldAnnotation(Field field,Class<A> annotationType)
    {
        return field.getAnnotation(annotationType);
    }

    /***
     * 获取指定字段的相同类型的所有注解
     * @param field          指定字段
     * @param annotationType 注解类型
     * @return               相同类型的所有注解
     */
    public static <A extends Annotation> A[] getFieldSameAnnotations(Field field,Class<A> annotationType)
    {
        return field.getAnnotationsByType(annotationType);
    }

    /**
     * 判断指定的类是否拥有指定的注解
     * @param clazz              指定的类
     * @param annotationType     注解类型
     * @return                   是否拥有指定的注解
     */
    public static <A extends Annotation> boolean hasClassAnnotation(Class<?> clazz, Class<A> annotationType)
    {
        return getClassAnnotation(clazz,annotationType) != null;
    }

    /**
     * 判断指定的构造器是否拥有指定的注解
     * @param constructor        指定的构造器
     * @param annotationType     注解类型
     * @return                   是否拥有指定的注解
     */
    public static <A extends Annotation> boolean hasConstructorAnnotation(Constructor<?> constructor, Class<A> annotationType)
    {
        return getConstructorAnnotation(constructor,annotationType) != null;
    }

    /**
     * 判断构造器的哪个参数上有指定的注解
     * @param constructor           构造器
     * @param annotationType        注解类型
     * @return                      如果构造器的某个参数拥有指定注解，那么返回其参数的下标，如果没有，返回-1.
     */
    public static <A extends Annotation> int hasConstructorParameterAnnotation(Constructor<?> constructor, Class<A> annotationType)
    {
        Parameter[] constructorParameters = getConstructorParameters(constructor);
        for (int i = 0 ; i < constructorParameters.length; ++i)
        {
            if(constructorParameters[i].getAnnotation(annotationType) != null)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断指定的方法是否拥有指定的注解
     * @param method             指定的方法
     * @param annotationType     注解类型
     * @return                   是否拥有指定的注解
     */
    public static <A extends Annotation> boolean hasMethodAnnotation(Method method, Class<A> annotationType)
    {
        return getMethodAnnotation(method,annotationType) != null;
    }


    /**
     * 判断方法的哪个参数上有指定的注解
     * @param method           指定方法
     * @param annotationType   注解类型
     * @return                 如果方法的某个参数拥有指定注解，那么返回其参数的下标，如果没有，返回-1.
     */
    public static <A extends Annotation> int hasMethodParameterAnnotation(Method method, Class<A> annotationType)
    {
        Parameter[] methodParameters = getMethodParameters(method);
        for (int i = 0 ; i < methodParameters.length; ++i)
        {
            if(methodParameters[i].getAnnotation(annotationType) != null)
            {
                return i;
            }
        }
        return -1;
    }


    /**
     * 判断指定参数是否有指定注解
     * @param parameter     指定参数
     * @param annotationType    注解类型
     * @return          是否有指定注解
     */
    public static <A extends Annotation> boolean hasParameterAnnotation(Parameter parameter, Class<A> annotationType)
    {
        return getParameterAnnotation(parameter, annotationType) != null;
    }


    /**
     * 判断指定字段是否有指定注解
     * @param field     指定字段
     * @param annotationType    注解类型
     * @return          是否有指定注解
     */
    public static <A extends Annotation> boolean hasFieldAnnotation(Field field, Class<A> annotationType)
    {
        return getFieldAnnotation(field, annotationType) != null;
    }

    /************************************************************************************************************************/

    /**
     * 判断类型是否为原生类型
     * @param clazz 要判断的类型
     * @return      是否为原生类型
     */
    public static boolean isPrimitive(Class<?> clazz)
    {
        return clazz.isPrimitive();
    }

    /**
     * 判断类型是否为包装类型
     * @param clazz 要判断的类型
     * @return      是否为包装类型
     */
    public static boolean isWrapper(Class<?> clazz)
    {
        return clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Short.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class)
                || clazz.equals(Byte.class) || clazz.equals(Boolean.class);
    }

}
