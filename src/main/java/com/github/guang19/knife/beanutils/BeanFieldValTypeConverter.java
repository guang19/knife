package com.github.guang19.knife.beanutils;

/**
 * @author yangguang
 * @date 2020/3/30
 * @description <p>函数式接口，简单化BeanCopier的Converter,只对bean的属性值的类型做转换</p>
 */

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
     *   BeanFieldValTypeConverter converter = fieldVal ->
     *   {
     *       //如果当前拷贝的属性值是 age 属性
     *       if(fieldVal instanceof Integer)
     *       {
     *           return (Integer)field;
     *       }
     *
     *       //如果当前拷贝的属性值是 id 属性
     *       if(fieldVal instanceof Long)
     *       {
     *           return (Long) fieldVal;
     *       }
     *
     *       //如果当前拷贝的属性值是 Inner 属性
     *       if(fieldVal instanceof Inner)
     *       {
     *           return fieldVal.toString();
     *       }
     *       return fieldVal;
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