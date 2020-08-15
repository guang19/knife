package com.github.guang19.knife.beanutils;

import net.sf.cglib.beans.BeanCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    //使用过的BeanCopier将加入缓存
    private static final ConcurrentHashMap<String, BeanCopier> beanCopierCache;

    static
    {
        beanCopierCache = new ConcurrentHashMap<>(8);
    }


    /**
     * <p>默认拷贝</p>
     *
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     * @param <S>       Source Class
     * @param <T>       Target Class
     */
    public static <S, T> void copy(S sourceObj, T targetObj)
    {
        shallowCopy(sourceObj, targetObj, null);
    }

    /**
     * 使用指定转换器定义的规则拷贝bean
     *
     * @param sourceObj                 源对象
     * @param targetObj                 目标对象
     * @param beanFieldValueConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     */
    public static <S, T> void copy(S sourceObj, T targetObj, BeanFieldValueConverter beanFieldValueConverter)
    {
        shallowCopy(sourceObj, targetObj, beanFieldValueConverter);
    }

    /**
     * <p>
     * 根据源类型的集合创建另一种类型的集合
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> List<T> createNewTypeCollection(List<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return createNewTypeCollection(sourceCollection, targetCollectionElementType, null);
    }

    /**
     * <p>
     * 根据源类型的集合，并以BeanFieldValueConverter定义的规则 创建另一种类型的集合,
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param beanFieldValueConverter   bean属性值转换器
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> List<T> createNewTypeCollection(List<S> sourceCollection, Class<T> targetCollectionElementType, BeanFieldValueConverter beanFieldValueConverter)
    {
        return sourceCollection.
                stream().
                map(element ->
                {
                    if (element != null)
                    {
                        try
                        {
                            return create(element, targetCollectionElementType, beanFieldValueConverter);
                        }
                        catch (ReflectiveOperationException e)
                        {
                            if (LOGGER.isDebugEnabled())
                            {
                                LOGGER.debug("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetCollectionElementType);
                                e.printStackTrace();
                            }
                            LOGGER.error("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetCollectionElementType);
                        }
                    }
                    return null;
                }).
                filter(Objects::nonNull).
                collect(Collectors.toList());
    }


    /**
     * <p>
     * 根据源类型的集合创建另一种类型的集合
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> Set<T> createNewTypeCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return createNewTypeCollection(sourceCollection, targetCollectionElementType, null);
    }


    /**
     * <p>
     * 根据源类型的集合，并以BeanFieldValueConverter定义的规则 创建另一种类型的集合,
     * 即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     *
     * @param sourceCollection            源集合
     * @param targetCollectionElementType 目标集合元素类型
     * @param beanFieldValueConverter   bean属性值转换器
     * @param <S>                         Source Class
     * @param <T>                         Target Class
     * @return 目标类型元素的集合
     */
    public static <S, T> Set<T> createNewTypeCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType,
                                                       BeanFieldValueConverter beanFieldValueConverter)
    {
        return sourceCollection.
                stream().
                map(element ->
                {
                    if (element != null)
                    {
                        try
                        {
                            return create(element, targetCollectionElementType, beanFieldValueConverter);
                        }
                        catch (ReflectiveOperationException e)
                        {
                            if (LOGGER.isDebugEnabled())
                            {
                                LOGGER.debug("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetCollectionElementType);
                                e.printStackTrace();
                            }
                            LOGGER.error("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetCollectionElementType);
                        }
                    }
                    return null;
                }).
                filter(Objects::nonNull).
                collect(Collectors.toSet());
    }


    /**
     * <p>
     * 根据源对象创建目标对象，并将源对象的属性拷贝到目标对象。
     * 如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     *
     * @param sourceObj   源对象
     * @param targetClass 目标Class
     * @param <S>         Source Class
     * @param <T>         Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     */
    public static <S, T> T createNewTypeObj(S sourceObj, Class<T> targetClass)
    {
        return createNewTypeObj(sourceObj, targetClass, null);
    }

    /**
     * <p>
     * 根据源对象创建目标对象，并根据BeanFieldValueConverter定义的转换规则将源对象的属性拷贝到目标对象。
     * 如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     *
     * @param sourceObj                 源对象
     * @param targetClass               目标Class
     * @param beanFieldValueConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     */
    public static <S, T> T createNewTypeObj(S sourceObj, Class<T> targetClass, BeanFieldValueConverter beanFieldValueConverter)
    {
        try
        {
            return create(sourceObj, targetClass, beanFieldValueConverter);
        }
        catch (ReflectiveOperationException e)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetClass);
                e.printStackTrace();
            }
            LOGGER.error("captured an exception when using [BeanUtils] create object of class type : [{}]" , targetClass);
            return null;
        }
    }


    /**
     * <p>根据源对象创建目标Class对象</p>
     *
     * @param sourceObj                 源对象
     * @param targetClass               目标Class
     * @param beanFieldValueConverter bean属性值转换器
     * @param <S>                       Source Class
     * @param <T>                       Target Class
     * @return 填充属性后的目标对象(浅拷贝)
     * @throws ReflectiveOperationException 反射创建对象时的异常
     */
    private static <S, T> T create(S sourceObj, Class<T> targetClass, BeanFieldValueConverter beanFieldValueConverter)
            throws ReflectiveOperationException
    {
        //以默认构造器创建目标对象
        T target = targetClass.getConstructor().newInstance();
        shallowCopy(sourceObj, target, beanFieldValueConverter);
        return target;
    }


    /**
     * <p>浅拷贝对象</p>
     *
     * @param sourceObj                 源对象
     * @param targetObj                 目标对象
     * @param beanFieldValueConverter bean属性值转换器
     */
    private static void shallowCopy(Object sourceObj, Object targetObj, BeanFieldValueConverter beanFieldValueConverter)
    {
        getCachedBeanCopier(sourceObj.getClass(), targetObj.getClass(), beanFieldValueConverter).
                copy(sourceObj, targetObj,
                        beanFieldValueConverter == null ? null :
                                (fieldVal, clazz, obj) -> beanFieldValueConverter.convertFieldValue(fieldVal));
    }

    /**
     * <p>获取已缓存的BeanCopier</p>
     *
     * @param sourceClass               源对象class
     * @param targetClass               目标对象class
     * @param beanFieldValueConverter bean属性值转换器
     * @return BeanCopier
     */
    private static BeanCopier getCachedBeanCopier(Class<?> sourceClass, Class<?> targetClass, BeanFieldValueConverter beanFieldValueConverter)
    {
        BeanCopier beanCopier;
        String beanCopierCacheKey = generateBeanCopierCacheKey(sourceClass, targetClass, beanFieldValueConverter);
        if ((beanCopier = beanCopierCache.get(beanCopierCacheKey)) == null)
        {
            beanCopierCache.put(beanCopierCacheKey, beanCopier =
                    createBeanCopier(sourceClass, targetClass, beanFieldValueConverter));
        }
        return beanCopier;
    }

    /**
     * 创BeanCopier
     *
     * @param sourceClass               源对象Class
     * @param targetClass               目标对象Class
     * @param beanFieldValueConverter bean属性值转换器
     * @return BeanCopier
     */
    private static BeanCopier createBeanCopier(Class<?> sourceClass, Class<?> targetClass, BeanFieldValueConverter beanFieldValueConverter)
    {
        return beanFieldValueConverter == null ? BeanCopier.create(sourceClass, targetClass, false) :
                BeanCopier.create(sourceClass, targetClass, true);
    }

    /**
     * <p>生成BeanCopier缓存的key</p>
     *
     * @param clazz1                    class1
     * @param clazz2                    class2
     * @param beanFieldValueConverter bean属性值转换器
     * @return string key
     */
    private static String generateBeanCopierCacheKey(Class<?> clazz1, Class<?> clazz2, BeanFieldValueConverter beanFieldValueConverter)
    {
        return beanFieldValueConverter == null ? BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName() :
                BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName() + BEANCOPIER_CACHE_KEY_SUFFIX;
    }


    //BeanCopier缓存key的前缀
    private static final String BEANCOPIER_CACHE_KEY_PREFIX = "BEAN:";

    //BeanCopier缓存key的连接符
    private static final String CONNECTION_SYNBOL = "::";

    //BeanCopier缓存key的后缀
    private static final String BEANCOPIER_CACHE_KEY_SUFFIX = "USE:BeanFieldValueConverter";
}
