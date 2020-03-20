package com.github.guang19.knife;

import lombok.*;
import org.junit.Test;

import java.lang.invoke.MethodHandle;

import java.util.Arrays;

/**
 * @author yangguang
 * @date 2020/3/19
 * @description <p></p>
 */
public class ReflectionUtilTest
{

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class T1
    {
        protected Integer id;

        protected String name;

        protected Integer age;

        public void publicNormalMethod()
        {
            System.out.println("publicNormalMethod");
        }

        protected void privateNormalMethod()
        {
            System.out.println("privateNormalMethod");
        }

        protected static void privateStaticMethod()
        {
            System.out.println("privateStaticMethod");
        }

        public static void publicStaticMethod()
        {
            System.out.println("publicStaticMethod");
        }
    }

    interface I1
    {


        public static void interfaceMethod1(){
            System.out.println();
        }

        default void interfaceMethod2()
        {

        }

        public abstract void interfaceMethod3();
    }

    @Setter
    @Getter
//    @NoArgsConstructor
    public static class T2 extends T1 implements I1
    {
        private T2()
        {

        }

        private void t2NormalMethod()
        {
            System.out.println("t2 normal method");
        }

        @Override
        public void interfaceMethod3()
        {

        }
    }

    @Test
    public void test01() throws Throwable
    {
        T1 instanceWithDefaultConstructor = ReflectionUtil.createInstanceWithDefaultConstructor(T1.class);
        System.out.println(instanceWithDefaultConstructor);
    }

    @Test
    public void test02() throws Throwable
    {
        MethodHandle constructor = ReflectionUtil.getPublicConstructor(T1.class);
        System.out.println(constructor.invoke());
    }

    @Test
    public void test03() throws Throwable
    {
        T1 t1 = new T1(1,"yxg",19);
        MethodHandle getId = ReflectionUtil.getPublicMethod(T1.class, "privateNormalMethod",void.class);
        getId.invoke(t1);

        T2 t2 = new T2();
        MethodHandle getId2 = ReflectionUtil.getPublicStaticMethod(T2.class, "privateNormalMethod",void.class);
        getId2.invoke(t2);
    }

    @Test
    public void test04() throws Throwable
    {
        MethodHandle publicStaticMethod = ReflectionUtil.getPublicStaticMethod(T1.class,"privateStaticMethod",void.class);
        publicStaticMethod.invoke();
    }


    @Test
    public void test05() throws Throwable
    {
        System.out.println(Arrays.toString(ReflectionUtil.getPublicMethods(T2.class)));
        System.out.println(Arrays.toString(ReflectionUtil.getDeclaredMethods(T2.class)));
        System.out.println(ReflectionUtil.getMethodByFindSuperClass(T2.class,"privateNormalMethod"));
    }

    static class M1
    {
        private  String field1;

        public   String field2;

        public   String field3;

        private static String field4 = "field4";

        private static String field5 = "field5";

        protected static String field6 = "field6";

        protected static String field7 = "field7";
    }

    interface IM1
    {
        static final String iField1 = "iField1";

        String iField2 = "iField2";
    }

    static class M1Sub extends M1 implements IM1
    {
        private  String subField1;

        public   String subField2;
    }

    @Test
    public void test06() throws Throwable
    {
        System.out.println(ReflectionUtil.getFieldByFindSuperClass(M1Sub.class,"subField1"));
    }

}
