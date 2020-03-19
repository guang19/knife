package com.github.guang19.knife;

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
public class BeanUtil
{

    //使用过的BeanCopier将加入缓存
    private static final ConcurrentHashMap<String,BeanCopier> beanCopierCache;

    static
    {
        beanCopierCache = new ConcurrentHashMap<String, BeanCopier>();
    }

    //BeanCopier缓存key的前缀
    private static final String BEANCOPIER_CACHE_KEY_PREFIX = "BEANCOPIER:";

    //BeanCopier缓存key的连接符
    private static final String CONNECTION_SYNBOL = "::";

    //BeanCopier缓存key的后缀
    private static final String BEANCOPIER_CACHE_KEY_SUFFIX = "USE:BeanFieldValTypeConverter";


    /**
     * <p>将源对象属性拷贝到目标对象属性</p>
     * @param sourceObj     源对象
     * @param targetObj     目标对象
     * @param <S>           Source Class
     * @param <T>           Target Class
     */
    public static <S,T> void copyProperties(S sourceObj,T targetObj)
    {
        copyProperties(sourceObj,targetObj,null);
    }

    //函数式接口，简单化BeanCopier的Converter,只对bean的属性值的类型做转换
    @FunctionalInterface
    public interface BeanFieldValTypeConverter
    {

        /**
         * 请看完以下代码，相信你很快能明白BeanFieldValTypeConverter的作用
         * e.g.
         *
         * ```````````````````````````````````````code
         *
         *    public class Inner{}
         *
         *    public class Person1
         *     {
         *        //wrapper type
         *         private Long id;
         *
         *         private String name;
         *
         *         //wrapper type
         *         private Integer age;
         *
         *         private Inner inner;
         *
         *         //setter
         *         ...
         *     }
         *
         *     public class Person2
         *     {
         *         //primitive type
         *         private long id;
         *
         *         private String name;
         *
         *         //primitive type
         *         private int age;
         *
         *         //type different Inner
         *         private String inner;
         *
         *         //setter
         *         ...
         *     }
         *
         *  ````
         *
         *  Person1 person1 = new Person1(1L,"yxg",19,new Inner);
         *  Person2 person2 = new Person2();
         *
         *  //普通的BeanUtil  normal
         *  BeanUtil.copy(person1,person2);
         *
         *  ````
         *
         * result:
         *
         *    person2{id:0L,name:"yxg",age:0,inner:null};
         *
         * ```````````````````````````````````````
         *
         *  以上这段代码在普通的BeanUtil中，只有name属性能够被正确的拷贝，
         *  其他属性因为类型不同，不会被拷贝成功。
         *
         *  而此接口就是为了解决这个问题而生的，你可以定义你想要转换的属性的值。
         *
         *  它的lambda写法如下:
         *
         *   BeanFieldValTypeConverter converter = field ->
         *   {
         *       //如果当前拷贝的属性值是age属性
         *       if(field instanceof Integer)
         *       {
         *           return (Integer)field;
         *       }
         *
         *       //如果当前拷贝的属性值是id属性
         *       if(fieldVal instanceof Long)
         *       {
         *           return (Long) fieldVal;
         *       }
         *
         *       //如果当前拷贝的属性值是Inner属性
         *       if(fieldVal instanceof Inner)
         *       {
         *           return fieldVal.toString();
         *       }
         *       return field;
         *   }
         *
         *   BeanUtil.copyProperties(person1,person2,converter);
         *
         * result(copy success):
         *
         *    person2{id:1L,name:"yxg",age:19, inner::toString()};
         *
         *
         *  其实这个接口是为了简化BeanCopier接口而生的。
         *
         *  FieldVal: BeanCopier在设置属性时会调用此接口，
         *            FieldVal就是当前正要设置的属性值。
         *
         */

        /**
         * 转换bean的属性值
         * @param   fieldVal    属性值
         * @return              转换后的属性值
         */
        public abstract Object convertFieldValType(Object fieldVal);
    }

    /**
     * 使用指定转换器定义的规则拷贝bean
     * @param sourceObj                     源对象
     * @param targetObj                     目标对象
     * @param beanFieldValTypeConverter     bean属性值转换器
     * @param <S>                           Source Class
     * @param <T>                           Target Class
     */
    public static <S,T> void copyProperties(S sourceObj,T targetObj,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        EmptyUtil.assertObjNull(sourceObj,"source object cannot be null.");
        EmptyUtil.assertObjNull(targetObj,"target object cannot be null.");
        copy(sourceObj,targetObj,beanFieldValTypeConverter);
    }

    /**
     * <p>
     *     将一种类型的集合转为另一种类型的集合.
     *     即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     * @param sourceCollection      源集合
     * @param targetCollectionElementType    目标集合元素类型
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      目标类型元素的集合
     */
    public static <S,T> List<T> copyCollection(List<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return copyCollection(sourceCollection,targetCollectionElementType,null);
    }

    /**
     * <p>
     *     使用转换器定义的规则,将一种类型的集合转为另一种类型的集合.
     *     即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     * @param sourceCollection      源集合
     * @param targetCollectionElementType    目标集合元素类型
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      目标类型元素的集合
     */
    public static <S,T> List<T> copyCollection(List<S> sourceCollection, Class<T> targetCollectionElementType,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        EmptyUtil.assertObjNull(sourceCollection,"source collection cannot be null");
        EmptyUtil.assertObjNull(targetCollectionElementType,"target collection element type cannot be null");
        return sourceCollection.
                stream().
                map(element ->
                {
                    if(element != null)
                    {
                        try
                        {
                            return create(element,targetCollectionElementType,beanFieldValTypeConverter);
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
     *     将一种类型的集合转为另一种类型的集合.
     *     即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     * @param sourceCollection      源集合
     * @param targetCollectionElementType    目标集合元素类型
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      目标类型元素的集合
     */
    public static <S,T> Set<T> copyCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType)
    {
        return copyCollection(sourceCollection,targetCollectionElementType,null);
    }


    /**
     * <p>
     *     将一种类型的集合转为另一种类型的集合.
     *     即使转换失败,最终返回的集合也不会有null元素。
     * </p>
     * @param sourceCollection      源集合
     * @param targetCollectionElementType    目标集合元素类型
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      目标类型元素的集合
     */
    public static <S,T> Set<T> copyCollection(Set<S> sourceCollection, Class<T> targetCollectionElementType,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        EmptyUtil.assertObjNull(sourceCollection,"source collection cannot be null");
        EmptyUtil.assertObjNull(targetCollectionElementType,"target collection element type cannot be null");
        return sourceCollection.
                stream().
                map(element ->
                {
                    if(element != null)
                    {
                        try
                        {
                            return create(element,targetCollectionElementType,beanFieldValTypeConverter);
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
     *     创建目标对象，并将源对象的属性拷贝到目标对象。
     *     如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     * @param sourceObj             源对象
     * @param targetClass           目标Class
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      填充属性后的目标对象(浅拷贝)
     */
    public static <S,T> T createTargetObj(S sourceObj,Class<T> targetClass)
    {
        return createTargetObj(sourceObj,targetClass,null);
    }

    /**
     * <p>
     *     创建目标对象，并根据bean属性值转换器定义的转换规则将源对象的属性拷贝到目标对象。
     *     如果目标对象没有默认构造方法或者创建目标对象失败，都将返回null
     * </p>
     * @param sourceObj             源对象
     * @param targetClass           目标Class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>                   Source Class
     * @param <T>                   Target Class
     * @return                      填充属性后的目标对象(浅拷贝)
     */
    public static <S,T> T createTargetObj(S sourceObj,Class<T> targetClass,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        EmptyUtil.assertObjNull(sourceObj,"source object cannot be null.");
        EmptyUtil.assertObjNull(targetClass,"target class cannot be null.");
        try
        {
            return create(sourceObj,targetClass,beanFieldValTypeConverter);
        }
        catch (Throwable e)
        {
            //不做处理
        }
        return null;
    }


    /**
     * <p>以源对象创建目标Class对象</p>
     * @param sourceObj         源对象
     * @param targetClass       目标Class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @param <S>               Source Class
     * @param <T>               Target Class
     * @return                  填充属性后的目标对象(浅拷贝)
     * @throws Throwable        反射创建对象时的异常
     */
    private static <S,T> T create(S sourceObj,Class<T> targetClass,BeanFieldValTypeConverter beanFieldValTypeConverter)
            throws Throwable
    {
        //以默认构造器创建目标对象
        T target = ClassUtil.createInstanceWithDefaultConstructor(targetClass);
        copy(sourceObj,target,beanFieldValTypeConverter);
        return target;
    }


    /**
     * <p>浅拷贝对象</p>
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     * @param beanFieldValTypeConverter bean属性值转换器
     */
    private static void copy(Object sourceObj,Object targetObj,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        getCachedBeanCopier(sourceObj.getClass(),targetObj.getClass(),beanFieldValTypeConverter).
                copy(sourceObj,targetObj,
                        beanFieldValTypeConverter == null ? null :
                                (fieldVal,clazz,obj) -> beanFieldValTypeConverter.convertFieldValType(fieldVal));
    }

    /**
     * <p>获取已缓存的BeanCopier，如果没有就创建</p>
     * @param sourceClass       源对象class
     * @param targetClass       目标对象class
     * @param beanFieldValTypeConverter bean属性值转换器
     * @return                  BeanCopier
     */
    private static BeanCopier getCachedBeanCopier(Class<?> sourceClass,Class<?> targetClass,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        BeanCopier beanCopier = null;
        String beanCopierClassKey = generateBeanCopierClassKey(sourceClass,targetClass,beanFieldValTypeConverter);
        if((beanCopier = beanCopierCache.get(beanCopierClassKey)) == null)
        {
            beanCopierCache.put(beanCopierClassKey,beanCopier =
                    createBeanCopier(sourceClass,targetClass,beanFieldValTypeConverter));
        }
        return beanCopier;
    }

    /**
     * 创BeanCopier
     * @param sourceClass                   源对象Class
     * @param targetClass                   目标对象Class
     * @param beanFieldValTypeConverter        bean属性值转换器
     * @return                              BeanCopier
     */
    private static BeanCopier createBeanCopier(Class<?> sourceClass,Class<?> targetClass,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        if(beanFieldValTypeConverter == null)
        {
            return BeanCopier.create(sourceClass,targetClass,false);
        }
        else
        {
            return BeanCopier.create(sourceClass,targetClass,true);
        }
    }

    /**
     * <p>生成BeanCopier缓存的key</p>
     * @param clazz1      class1
     * @param clazz2      class2
     * @param beanFieldValTypeConverter bean属性值转换器
     * @return            string key
     */
    private static String generateBeanCopierClassKey(Class<?> clazz1,Class<?> clazz2,BeanFieldValTypeConverter beanFieldValTypeConverter)
    {
        if(beanFieldValTypeConverter == null)
        {
            return BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName();
        }
        else
        {
            return BEANCOPIER_CACHE_KEY_PREFIX + clazz1.getName() + CONNECTION_SYNBOL + clazz2.getName() + BEANCOPIER_CACHE_KEY_SUFFIX;
        }
    }
}
