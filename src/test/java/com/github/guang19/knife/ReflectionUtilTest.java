package com.github.guang19.knife;

import com.github.guang19.knife.reflectionutils.ClassMethodVisitor;
import com.github.guang19.knife.reflectionutils.ReflectionUtils;
import lombok.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author yangguang
 * @date 2020/3/19
 * @description <p></p>
 */
public class ReflectionUtilTest
{
    interface I
    {
        public String I_f1 = "I_f1";
        public static String I_f2 = "I_f2";

        static String I_f3 = "I_f3";
    }

    static abstract class Super
    {
        public String s_f1 = "s_f1";
        public static String s_f2 = "s_f2";

        private String s_f3 = "s_f3";
        private static String s_f4 = "s_f4";

        protected String s_f5 = "s_f5";
        protected static String s_f6 = "s_f6";

        protected void superMethod1()
        {
            System.out.println("super method1");
        }
        public void superMethod2()
        {
            System.out.println("super method2");
        }

        private void superMethod3()
        {
            System.out.println("superMethod3");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class C extends Super implements I
    {
        public String f1 = "f1";

        protected  String f2 = "f2";

        private String f3 = "f3";

        public static String f4 = "f4";

        private static String f5 = "f5";

        protected static String f6 = "f6";

        private C(String f1,String f2 ,String f3 ,String f4){}
        protected C(String f1,String f2 ,String f3 ,String f4,String f5){}


        public void m1()
        {
            System.out.println("m1");
        }

        private void m2()
        {
            System.out.println("m2");
        }

        public static void m3()
        {
            System.out.println("m3");
        }

        private static void m4()
        {
            System.out.println("m4");
        }

        protected void m5()
        {
            System.out.println("m5");
        }

        protected static void m6()
        {
            System.out.println("m6");
        }
    }

    @Test
    public void test01() throws Throwable
    {
        C instanceWithDefaultConstructor = ReflectionUtils.createInstanceWithDefaultConstructor(C.class);
        //f1 f2 f3
        System.out.println(instanceWithDefaultConstructor);

        System.out.println(Arrays.toString(ReflectionUtils.getDeclaredConstructors(C.class)));

        System.out.println(ReflectionUtils.getDeclaredConstructor(C.class,String.class, String.class, String.class));

        System.out.println(ReflectionUtils.getPublicConstructor(C.class, String.class, String.class, String.class).invoke("a", "b", "c"));

        System.out.println(ReflectionUtils.getPublicConstructor(C.class, String.class, String.class, String.class,String.class,String.class).invoke("a", "b", "c","d","e"));

    }


    @Test
    public void test02() throws Throwable
    {
        C c1 = new C();
        ReflectionUtils.getPublicMethod(C.class,"m1",void.class).invoke(c1);

        ReflectionUtils.getPublicMethod(C.class,"m5",void.class).invoke(c1);

        ReflectionUtils.getPublicStaticMethod(C.class,"m3",void.class).invoke();

        ReflectionUtils.getPublicStaticMethod(C.class,"m6",void.class).invoke();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class C2
    {
        public String f1 = "f1";

        protected  String f2 = "f2";

        private String f3 = "f3";

        public static String f4 = "f4";

        private static String f5 = "f5";

        protected static String f6 = "f6";


        public void m1()
        {
            System.out.println("m1");
        }

        private void m2()
        {
            System.out.println("m2");
        }

        public static void m3()
        {
            System.out.println("m3");
        }

        private static void m4()
        {
            System.out.println("m4");
        }

        protected void m5()
        {
            System.out.println("m5");
        }

        protected static void m6()
        {
            System.out.println("m6");
        }
    }


    @Test
    public void test03() throws Throwable
    {

        System.out.println(Arrays.toString(ReflectionUtils.getPublicMethods(C.class)));

        System.out.println(Arrays.toString(ReflectionUtils.getDeclaredMethods(C.class)));

        System.out.println(Arrays.toString(ReflectionUtils.getPublicMethods(C2.class)));

        System.out.println(ReflectionUtils.getMethod(C2.class,"m6"));

    }

    interface I2
    {
        public String I_f1 = "I_f1";

        public static String I_f2 = "I_f2";

        static String I_f3 = "I_f3";
    }

    static abstract class Super2
    {
        public String s_f1 = "s_f1";
        public static String s_f2 = "s_f2";

        private String s_f3 = "s_f3";
        private static String s_f4 = "s_f4";

        protected String s_f5 = "s_f5";
        protected static String s_f6 = "s_f6";
    }

    @Setter
    @Getter
    static class C3 extends Super2 implements I2
    {
        public String f1 = "f1";

        protected  String f2 = "f2";

        private String f3 = "f3";

        public static String f4 = "f4";

        private static String f5 = "f5";

        protected static String f6 = "f6";

        public String getSF5()
        {
            return s_f5;
        }
    }

    @Test
    public void test04() throws Throwable
    {
        C3 c3 = new C3();
        System.out.println(ReflectionUtils.getGetter(C3.class, "f3").invoke(c3));
        ReflectionUtils.getSetter(C3.class,"f3").invoke(c3,"2");
        System.out.println(ReflectionUtils.getGetter(C3.class, "f3").invoke(c3));


    }

    @Setter
    @Getter
    static class C4 extends Super2 implements I2
    {
        public String f1 = "f1";

        protected  String f2 = "f2";

        private String f3 = "f3";

        public static String f4 = "f4";

        private static String f5 = "f5";

        protected static String f6 = "f6";

        public static void get(String a,Object obj)
        {
        }

    }


    @Test
    public void test05() throws Throwable
    {
        final Method m = ReflectionUtils.getMethod(C4.class, "get", String.class, Object.class);
        System.out.println(Arrays.toString(ClassMethodVisitor.getMethodParameterNames(m)));
    }



    @Test
    public void test06() throws Throwable
    {
        System.out.println(new String[10].length);

    }

}
