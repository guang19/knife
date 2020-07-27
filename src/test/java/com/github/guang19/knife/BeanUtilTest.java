package com.github.guang19.knife;

import com.github.guang19.knife.beanutils.BeanUtils;
import net.sf.cglib.beans.BeanCopier;
import org.junit.Test;

import java.util.*;

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

        private Inner inner;

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

        BeanUtils.copyProperties(person1, person2);

        //true, 浅拷贝
        System.out.println(person1.getInner() == person2.getInner());

        System.out.println(person2);
    }

    @Test
    public void test02() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        System.out.println(BeanUtils.createTargetObj(person1, Person2.class));
    }


    public static class Person3
    {
        private long id;

        private String name;

        private int age;

        private String inner;

        public long getId()
        {
            return id;
        }

        public void setId(long id)
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

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
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
            return "Person3{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", inner='" + inner + '\'' +
                    '}';
        }
    }

    @Test
    public void test03() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        Person3 person3 = new Person3();

        BeanCopier beanCopier1 = BeanCopier.create(Person1.class, Person3.class, false);

        beanCopier1.copy(person1, person3, null);
        System.out.println(person3);

        Person3 person32 = new Person3();

        BeanCopier beanCopier2 = BeanCopier.create(Person1.class, Person3.class, true);

        beanCopier2.copy(person1, person32, (arg, clazz, obj2) ->
        {
            if (arg instanceof Long)
            {
                return  arg;
            }
            if (arg instanceof Integer)
            {
                return  arg;
            }
            if (arg instanceof Inner)
            {
                return arg.toString();
            }
            return arg;
        });

        System.out.println(person32);
    }


    @Test
    public void test04() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        Person3 person3 = new Person3();
        BeanUtils.copyProperties(person1, person3, fieldVal ->
        {
            if (fieldVal instanceof Integer)
            {
                return (Integer) fieldVal;
            }
            if (fieldVal instanceof Long)
            {
                return (Long) fieldVal;
            }
            if (fieldVal instanceof Inner)
            {
                return fieldVal.toString();
            }
            return fieldVal;
        });

        System.out.println(person3);
    }


    @Test
    public void test05() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

//        System.out.println(BeanUtil.createTargetObj(person1,Person3.class));

        System.out.println(BeanUtils.createTargetObj(person1, Person3.class, fieldVal ->
        {
            if (fieldVal instanceof Integer)
            {
                return (Integer) fieldVal;
            }
            if (fieldVal instanceof Long)
            {
                return (Long) fieldVal;
            }
            if (fieldVal instanceof Inner)
            {
                return fieldVal.toString();
            }
            return fieldVal;
        }));
    }

    @Test
    public void test06() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        Person1 person2 = new Person1();
        person2.setId(1L);
        person2.setAge(19);
        person2.setName("yxg2");
        person2.setInner(new Inner());

        List<Person1> person1List = new ArrayList<>();
        person1List.add(person1);
        person1List.add(person2);

        List<Person2> person2List = BeanUtils.createTargetCollection(person1List, Person2.class);
        System.out.println(person2List);

        Set<Person1> person1Set = new HashSet<>();
        person1Set.add(person1);
        person1Set.add(person2);

        Set<Person2> person2Set = BeanUtils.createTargetCollection(person1Set, Person2.class);

        System.out.println(person2Set);
    }

    @Test
    public void test07() throws Exception
    {
        Person1 person1 = new Person1();
        person1.setId(1L);
        person1.setAge(19);
        person1.setName("yxg");
        person1.setInner(new Inner());

        Person1 person2 = new Person1();
        person2.setId(2L);
        person2.setAge(19);
        person2.setName("yxg2");
        person2.setInner(new Inner());

        List<Person1> person1List = new ArrayList<>(Arrays.asList(person1, person2));
        System.out.println("person1List : " + person1List);

        List<Person3> person3List = BeanUtils.createTargetCollection(person1List, Person3.class, (fieldVal ->
        {
            if (fieldVal instanceof Integer)
            {
                return (Integer) fieldVal;
            }
            if (fieldVal instanceof Long)
            {
                return (Long) fieldVal;
            }
            if (fieldVal instanceof Inner)
            {
                return fieldVal.toString();
            }
            return fieldVal;
        }));
        System.out.println("person3List " + person3List);

        Set<Person1> person1Set = new HashSet<>(Arrays.asList(person1, person2));
        System.out.println("person1Set : " + person1Set);

        Set<Person3> person3Set = BeanUtils.createTargetCollection(person1Set, Person3.class, (fieldVal ->
        {
            if (fieldVal instanceof Integer)
            {
                return (Integer) fieldVal;
            }
            if (fieldVal instanceof Long)
            {
                return (Long) fieldVal;
            }
            if (fieldVal instanceof Inner)
            {
                return fieldVal.toString();
            }
            return fieldVal;
        }));
        System.out.println("person3Set " + person3Set);
    }
}
