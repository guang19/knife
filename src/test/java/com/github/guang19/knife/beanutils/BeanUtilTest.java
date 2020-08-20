package com.github.guang19.knife.beanutils;

import com.github.guang19.knife.beanutils.BeanFieldValueConverter;
import com.github.guang19.knife.beanutils.BeanUtils;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangguang
 * @date 2020/3/18
 * @description <p></p>
 */
public class BeanUtilTest
{

    public static class Inner
    {

    }

    public static class Person1
    {
        private Long id;

        private String name;

        private Integer age;

        private Inner inner;

        public Person1()
        {
        }

        public Person1(Long id, String name, Integer age, Inner inner)
        {
            this.id = id;
            this.name = name;
            this.age = age;
            this.inner = inner;
        }

        public Long getId()
        {
            return id;
        }

        public void setId(Long id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Integer getAge()
        {
            return age;
        }

        public void setAge(Integer age)
        {
            this.age = age;
        }

        public Inner getInner()
        {
            return inner;
        }

        public void setInner(Inner inner)
        {
            this.inner = inner;
        }

        @Override
        public String toString()
        {
            return "Person1{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", inner=" + inner +
                    '}';
        }
    }


    public static class Person2
    {
        private Long id;

        private String name;

        private Integer age;

        private String inner;

        public Long getId()
        {
            return id;
        }

        public void setId(Long id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Integer getAge()
        {
            return age;
        }

        public void setAge(Integer age)
        {
            this.age = age;
        }

        public String getInner()
        {
            return inner;
        }

        public void setInner(String inner)
        {
            this.inner = inner;
        }

        @Override
        public String toString()
        {
            return "Person2{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", inner=" + inner +
                    '}';
        }
    }


    @Test
    public void test01() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        Person2 person2 = new Person2();

        BeanUtils.copy(person1,person2);

        System.out.println(person2);

        person2.setName("guang19");
        BeanUtils.copy(person1,person2,fieldVal ->
        {
            if (fieldVal.getClass() == Inner.class)
                return fieldVal.toString();
            else
                return fieldVal;
        });

        System.out.println(person2);

        System.out.println(BeanUtils.createNewTypeObj(person1,Person2.class));
    }

    @Test
    public void test02() throws Exception
    {

        BeanFieldValueConverter converter =
                fieldVal ->
                {
                    if (fieldVal.getClass() == Inner.class)
                        return fieldVal.toString();
                    else
                        return fieldVal;
                };

        List<Person1> person1List = new ArrayList<>();
        for (int i = 0; i < 5; ++i)
        {
            person1List.add(new Person1((long)i,UUID.randomUUID().toString(),i,new Inner()));
        }

        long begin = System.currentTimeMillis();
        List<Person2> newTypeCollection1 = BeanUtils.createNewTypeCollection(person1List, Person2.class);
        System.out.println("copy new type collection , spend time : " + (System.currentTimeMillis() - begin));

        List<Person2> newTypeCollection2 = BeanUtils.createNewTypeCollection(person1List, Person2.class,converter);

        System.out.println(newTypeCollection1);
        System.out.println(newTypeCollection2);
    }

    @Test
    public void test03() throws Exception
    {

        BeanFieldValueConverter converter =
                fieldVal ->
                {
                    if (fieldVal.getClass() == Inner.class)
                        return fieldVal.toString();
                    else
                        return fieldVal;
                };

        Set<Person1> person1Set = new HashSet<>();
        for (int i = 0; i < 5; ++i)
        {
            person1Set.add(new Person1((long)i,UUID.randomUUID().toString(),i,new Inner()));
        }

        long begin = System.currentTimeMillis();
        Set<Person2> newTypeCollection1 = BeanUtils.createNewTypeCollection(person1Set, Person2.class);
        System.out.println("copy new type collection , spend time : " + (System.currentTimeMillis() - begin));

        Set<Person2> newTypeCollection2 = BeanUtils.createNewTypeCollection(person1Set, Person2.class,converter);

        System.out.println(newTypeCollection1);
        System.out.println(newTypeCollection2);
    }
}
