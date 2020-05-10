package com.github.guang19.knife.beanutils;

import com.github.guang19.knife.AssertUtils;
import com.github.guang19.knife.reflectionutils.ReflectionUtils;
import net.sf.cglib.beans.BeanCopier;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p>Bean工具类</p>
 */
public class BeanUtils
{

    //使用过的BeanCopier将加入缓存
    private static final ConcurrentHashMap<String, BeanCopier> beanCopierCache;

    static
    {
        beanCopierCache = new ConcurrentHashMap<>();
    }


    /**
     * <p>将源对象属性拷贝到目标对象属性</p>
     *
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     * @param <S>       Source Class
     * @param <T>       Target Class
     */
    public static <S, T> void copyProperties(S sourceObj, T targetObj)
    {
        copyProperties(sourceObj, targetObj, null);
    }

    /**
     * 使用指定转换器定义的规则拷贝bean
     *
     * @param sourceObj                 源对象
     * @param targetObj                 目标对象
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     */
    public static <S, T> void copyProperties(S sourceObj, T targetObj, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        AssertUtils.exceptionIfObjNull(sourceObj, "Source object cannot be null.");
        AssertUtils.exceptionIfObjNull(targetObj, "Target object cannot be null.");
        copy(sourceObj, targetObj, beanFieldValTypeConverter);
    }

    /**
     * <p>
     * 将一种类型的集合转为另一种类型的集合.
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> List<T> createTargetCollection(List<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return createTargetCollection(sourceCollection, targetCollectionElementType, null);
    }

    /**
     * <p>
     * 使用转换器定义的规则,将一种类型的集合转为另一种类型的集合.
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param beanFieldValTypeConverter   bean属性值转换器
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> List<T> createTargetCollection(List<S> sourceCollection, Class<T> targetCollectionElementType, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        AssertUtils.exceptionIfObjNull(sourceCollection, "Source collection cannot be null.");
        AssertUtils.exceptionIfObjNull(targetCollectionElementType, "Target collection element type cannot be null.");
        return sourceCollection.
                stream().
                map(element ->
                {
                    if (element != null)
                    {
                        try
                        {
                            return create(element, targetCollectionElementType, beanFieldValTypeConverter);
                        }
                        catch (Throwable e)
                        {
                            //不做处理
                        }
                    }
                    return null;
                }).
                filter(Objects::nonNull).
                collect(Collectors.toList());
    }


    /**
     * <p>
     * 将一种类型的集合转为另一种类型的集合.
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> Set<T> createTargetCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return createTargetCollection(sourceCollection, targetCollectionElementType, null);
    }


    /**
     * <p>
     * 将一种类型的集合转为另一种类型的集合.
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param beanFieldValTypeConverter   bean属性值转换器
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> Set<T> createTargetCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        AssertUtils.exceptionIfObjNull(sourceCollection, "Source collection cannot be null.");
        AssertUtils.exceptionIfObjNull(targetCollectionElementType, "Target collection element type cannot be null.");
        return sourceCollection.
                stream().
                map(element ->
                {
                    if (element != null)
                    {
                        try
                        {
                            return create(element, targetCollectionElementType, beanFieldValTypeConverter);
                        }
                        catch (Throwable e)
                        {
                            //不做处理
                        }
                    }
                    return null;
                }).
                filter(Objects::nonNull).
                collect(Collectors.toSet());
    }


    /**
     * <p>
     * 创建目标对象，并将源对象的属性拷贝到目标对象。
     * 如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     *
     * @param sourceObj   源对象
     * @param targetClass 目标Class
     * @param <S>         Source Class
     * @param <T>         Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     */
    public static <S, T> T createTargetObj(S sourceObj, Class<T> targetClass)
    {
        return createTargetObj(sourceObj, targetClass, null);
    }

    /**
     * <p>
     * 创建目标对象，并根据bean属性值转换器定义的转换规则将源对象的属性拷贝到目标对象。
     * 如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     *
     * @param sourceObj                 源对象
     * @param targetClass               目标Class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     */
    public static <S, T> T createTargetObj(S sourceObj, Class<T> targetClass, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        AssertUtils.exceptionIfObjNull(sourceObj, "Source object cannot be null.");
        AssertUtils.exceptionIfObjNull(targetClass, "Target class cannot be null.");
        try
        {
            return create(sourceObj, targetClass, beanFieldValTypeConverter);
        }
        catch (Throwable e)
        {
            //不做处理
            return null;
        }
    }


    /**
     * <p>以源对象创建目标Class对象</p>
     *
     * @param sourceObj                 源对象
     * @param targetClass               目标Class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     * @throws Throwable 反射创建对象时的异常
     */
    private static <S, T> T create(S sourceObj, Class<T> targetClass, BeanFieldValTypeConverter beanFieldValTypeConverter)
            throws Throwable
    {
        //以默认构造器创建目标对象
        T target = ReflectionUtils.createInstanceWithDefaultConstructor(targetClass);
        copy(sourceObj, target, beanFieldValTypeConverter);
        return target;
    }


    /**
     * <p>浅拷贝对象</p>
     *
     * @param sourceObj                 源对象
     * @param targetObj                 目标对象
     * @param beanFieldValTypeConverter bean属性值转换器
     */
    private static void copy(Object sourceObj, Object targetObj, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        getCachedBeanCopier(sourceObj.getClass(), targetObj.getClass(), beanFieldValTypeConverter).
                copy(sourceObj, targetObj,
                        beanFieldValTypeConverter == null ? null :
                                (fieldVal, clazz, obj) -> beanFieldValTypeConverter.convertFieldValType(fieldVal));
    }

    /**
     * <p>获取已缓存的BeanCopier，如果没有就创建</p>
     *
     * @param sourceClass               源对象class
     * @param targetClass               目标对象class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @return BeanCopier
     */
    private static BeanCopier getCachedBeanCopier(Class<?> sourceClass, Class<?> targetClass, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        BeanCopier beanCopier = null;
        String beanCopierClassKey = generateBeanCopierClassKey(sourceClass, targetClass, beanFieldValTypeConverter);
        if ((beanCopier = beanCopierCache.get(beanCopierClassKey)) == null)
        {
            beanCopierCache.put(beanCopierClassKey, beanCopier =
                    createBeanCopier(sourceClass, targetClass, beanFieldValTypeConverter));
        }
        return beanCopier;
    }

    /**
     * 创BeanCopier
     *
     * @param sourceClass               源对象Class
     * @param targetClass               目标对象Class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @return BeanCopier
     */
    private static BeanCopier createBeanCopier(Class<?> sourceClass, Class<?> targetClass, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        return beanFieldValTypeConverter == null ? BeanCopier.create(sourceClass, targetClass, false) :
                BeanCopier.create(sourceClass, targetClass, true);
    }

    /**
     * <p>生成BeanCopier缓存的key</p>
     *
     * @param clazz1                    class1
     * @param clazz2                    class2
     * @param beanFieldValTypeConverter bean属性值转换器
     * @return string key
     */
    private static String generateBeanCopierClassKey(Class<?> clazz1, Class<?> clazz2, BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        return beanFieldValTypeConverter == null ? BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName() :
                BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName() + BEANCOPIER_CACHE_KEY_SUFFIX;
    }


    //BeanCopier缓存key的前缀
    private static final String BEANCOPIER_CACHE_KEY_PREFIX = "BEANCOPIER:";

    //BeanCopier缓存key的连接符
    private static final String CONNECTION_SYNBOL = "::";

    //BeanCopier缓存key的后缀
    private static final String BEANCOPIER_CACHE_KEY_SUFFIX = "USE:BeanFieldValTypeConverter";
}
