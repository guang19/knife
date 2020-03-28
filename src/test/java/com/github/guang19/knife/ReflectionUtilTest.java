package com.github.guang19.knife;

import com.github.guang19.knife.reflectionutils.MethodClassVisitor;
import com.github.guang19.knife.reflectionutils.ReflectionUtils;
import com.github.guang19.knife.testannotation.ClassAnnotation1;
import com.github.guang19.knife.testannotation.ClassAnnotation2;
import com.github.guang19.knife.testannotation.ClassAnnotations1;
import lombok.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        System.out.println(ReflectionUtils.getGetterMethod(C3.class, "f3").invoke(c3));
        ReflectionUtils.getSetterMethod(C3.class,"f3").invoke(c3,"2");
        System.out.println(ReflectionUtils.getGetterMethod(C3.class, "f3").invoke(c3));


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

        public void get(String s)
        {
        }

    }


    @Test
    public void test05() throws Throwable
    {
        final Method m = ReflectionUtils.getMethod(C4.class, "get",String.class);
        System.out.println(Arrays.toString(MethodClassVisitor.getMethodParameterNames(m)));
    }



    @Test
    public void test06() throws Throwable
    {
        System.out.println(ReflectionUtils.getMethodParameter(ReflectionUtils.getMethod(C4.class,"get",String.class),"s",String.class));
    }

    @ClassAnnotation2
    @ClassAnnotation1
    @ClassAnnotation1
    static abstract class Super3
    {

    }

    @Setter
    @Getter
    static class C5 extends Super3
    {

        @ClassAnnotation2
        @ClassAnnotation1
        @ClassAnnotation1
        public String f1 = "f1";

        @ClassAnnotation1
        @ClassAnnotation1
        public C5(){}

        @ClassAnnotation1
        public C5(@ClassAnnotation2 String s1)
        {

        }


        @ClassAnnotation1
        public C5(@ClassAnnotation1 @ClassAnnotation2 String s1,@ClassAnnotation1 String s2)
        {

        }

        @ClassAnnotation2
        @ClassAnnotation1
        @ClassAnnotation1
        public void get()
        {

        }

        @ClassAnnotation1
        @ClassAnnotation1
        public void get2( @ClassAnnotation2 @ClassAnnotation1 @ClassAnnotation1 String s)
        {

        }

    }

    @Test
    public void test07() throws Throwable
    {
        ClassAnnotation1 classAnnotation1 = ReflectionUtils.getClassAnnotation(C5.class, ClassAnnotation1.class);
        System.out.println(classAnnotation1);
        System.out.println(Arrays.toString(ReflectionUtils.getClassAnnotations(C5.class)));
        System.out.println(Arrays.toString(ReflectionUtils.getClassSameAnnotations(C5.class,ClassAnnotation1.class)));
    }


    @Test
    public void test08() throws Throwable
    {
        System.out.println(Arrays.toString(ReflectionUtils.getConstructorSameAnnotations(ReflectionUtils.getDeclaredConstructor(C5.class),ClassAnnotation1.class)));

        System.out.println(ReflectionUtils.getConstructorAnnotation(ReflectionUtils.getDeclaredConstructor(C5.class,String.class),ClassAnnotation1.class));
        System.out.println(Arrays.toString(ReflectionUtils.getConstructorAnnotations(ReflectionUtils.getDeclaredConstructor(C5.class,String.class))));
        Constructor<C5> declaredConstructor = ReflectionUtils.getDeclaredConstructor(C5.class, String.class,String.class);
        System.out.println(Arrays.toString(ReflectionUtils.getConstructorParameterAnnotations(declaredConstructor,1)));
    }

    @Test
    public void test09() throws Throwable
    {
        Method method = ReflectionUtils.getMethod(C5.class,"get2",String.class);
        System.out.println(Arrays.toString(ReflectionUtils.getMethodSameAnnotations(method,ClassAnnotation1.class)));
        Parameter parameter = ReflectionUtils.getMethodParameter(method,"s",String.class);
        System.out.println(ReflectionUtils.getParameterAnnotation(parameter, ClassAnnotation2.class));
        System.out.println(Arrays.toString(ReflectionUtils.getParameterSameAnnotations(parameter, ClassAnnotation1.class)));
    }

    @Test
    public void test10() throws Throwable
    {
        Field field = ReflectionUtils.getField(C5.class,"f1");
        System.out.println(Arrays.toString(ReflectionUtils.getFiledAnnotations(field)));
        System.out.println(ReflectionUtils.getFieldAnnotation(field,ClassAnnotation2.class));
        System.out.println(Arrays.toString(ReflectionUtils.getFieldSameAnnotations(field,ClassAnnotation1.class)));
    }

    @Setter
    @Getter
    @ClassAnnotation2
    static class C6
    {

        @ClassAnnotation2
        @ClassAnnotation1
        @ClassAnnotation1
        public String f1 = "f1";

        @ClassAnnotation1
        @ClassAnnotation1
        public C6(){}

        @ClassAnnotation1
        public C6(@ClassAnnotation2 String s1)
        {

        }


        @ClassAnnotation1
        public C6(@ClassAnnotation1 @ClassAnnotation2 String s1,@ClassAnnotation1 String s2)
        {

        }

        @ClassAnnotation2
        @ClassAnnotation1
        @ClassAnnotation1
        public void get()
        {

        }

        protected void get1(){}

        @ClassAnnotation1
        @ClassAnnotation1
        public void get2( @ClassAnnotation2 @ClassAnnotation1 @ClassAnnotation1 String s)
        {

        }
    }


    @Test
    public void test11() throws Throwable
    {
        System.out.println(ReflectionUtils.hasMethodParameterAnnotation(ReflectionUtils.getMethod(C6.class,"get2",String.class), Test.class));
        System.out.println(ReflectionUtils.hasClassAnnotation(C6.class,ClassAnnotation2.class));
    }

}
