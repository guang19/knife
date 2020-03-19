package com.github.guang19.knife;

import lombok.*;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * @author yangguang
 * @date 2020/3/19
 * @description <p></p>
 */
public class ClassUtilTest
{

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class T1
    {
        private Integer id;

        private String name;

        private Integer age;

        public void publicNormalMethod()
        {
            System.out.println("publicNormalMethod");
        }

        private void privateNormalMethod()
        {
            System.out.println("privateNormalMethod");
        }

        private static void privateStaticMethod()
        {
            System.out.println("privateStaticMethod");
        }

        public static void publicStaticMethod()
        {
            System.out.println("publicStaticMethod");
        }
    }

    @Test
    public void test01() throws Throwable
    {
        T1 instanceWithDefaultConstructor = ClassUtil.createInstanceWithDefaultConstructor(T1.class);
        System.out.println(instanceWithDefaultConstructor);
    }

    @Test
    public void test02() throws Throwable
    {
        MethodHandle constructor = ClassUtil.getConstructor(T1.class);
        System.out.println(constructor.invoke());
    }

    @Test
    public void test03() throws Throwable
    {
        T1 t1 = new T1(1,"yxg",19);
        MethodHandle getId = ClassUtil.getPublicMethod(T1.class, "publicNormalMethod",void.class);
        getId.invoke(t1);
    }

    @Test
    public void test04() throws Throwable
    {
        MethodHandle publicStaticMethod = ClassUtil.getPublicStaticMethod(T1.class,"publicStaticMethod",void.class);
        publicStaticMethod.invoke();
    }


}
