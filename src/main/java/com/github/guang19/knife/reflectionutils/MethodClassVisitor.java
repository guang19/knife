package com.github.guang19.knife.reflectionutils;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author yangguang
 * @date 2020/3/27
 * @description <p>类方法访问器</p>
 */
public class MethodClassVisitor extends ClassVisitor
{

    //类字节码生成器
    private static final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

    //asm类字节码读取器
    private ClassReader classReader;

    //需要获取参数的方法
    private Method method;

    //用于获取方法参数名的方法访问器实例
    private LocalVariableTableMethodVisitor localVariableTableMethodVisitor;

    /**
     * 构造类方法访问器
     *
     * @param method 要获取参数的方法
     */
    private MethodClassVisitor(Method method)
    {
        super(Opcodes.ASM4, classWriter);
        this.method = method;
    }

    /**
     * 获取方法的参数名
     *
     * @param method 方法
     * @return 参数名数组
     */
    public static String[] getMethodParameterNames(Method method)
    {
        try
        {
            return new MethodClassVisitor(method).accept();
        }
        catch (IOException e)
        {
            //读取类的信息过程中的IO异常
            throw new RuntimeException(String.format("Cannot read bytecode information for class : %s .", method.getDeclaringClass()), e);
        }
        catch (Throwable e)
        {
            //其他异常返回空数组
            return new String[0];
        }
    }


    //生成方法类的字节码树
    private String[] accept() throws IOException
    {
        classReader = new ClassReader(method.getDeclaringClass().getName());
        classReader.accept(this, 0);
        return localVariableTableMethodVisitor.getMethodParameterNames();
    }

    /**
     * 访问当前类的某个方法
     *
     * @param access     访问权限
     * @param name       方法名
     * @param descriptor 方法描述
     * @param signature  方法签名
     * @param exceptions 方法异常信息
     * @return MethodVisitor
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
    {
        System.out.println(name);
        if (name.equals("<init>"))
        {
            System.out.println(1);
            System.out.println(descriptor);
        }
        //如果要查找的方法名和当前方法名相同就进行查询
        if (method.getName().equals(name))
        {
            Type[] argumentTypes = Type.getArgumentTypes(descriptor);
            //如果方法参数的数量和类型都相同就继续查询
            if (sameTypes(argumentTypes, method.getParameterTypes()))
            {
                MethodVisitor methodVisitor = classWriter.visitMethod(access, name, descriptor, signature, exceptions);
                return (localVariableTableMethodVisitor = new LocalVariableTableMethodVisitor(methodVisitor, method));
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }


    //判断2个类型数组的值是否相等
    private boolean sameTypes(Type[] types1, Class<?>[] types2)
    {
        if (types1.length != types2.length)
        {
            return false;
        }
        for (int i = 0; i < types1.length; ++i)
        {
            if (!types1[i].equals(Type.getType(types2[i])))
            {
                return false;
            }
        }
        return true;
    }


    /**
     * 用于访问本地变量的方法访问器
     */
    private static class LocalVariableTableMethodVisitor extends MethodVisitor
    {
        //方法参数名数组
        private String[] methodParameterNames;

        //要获取参数名的方法
        private Method method;

        /**
         * 构造本地变量访问器
         *
         * @param methodVisitor 方法方法器
         * @param method        要获取参数的方法
         */
        public LocalVariableTableMethodVisitor(MethodVisitor methodVisitor, Method method)
        {
            super(Opcodes.ASM4, methodVisitor);
            this.method = method;
            this.methodParameterNames = new String[method.getParameterCount()];
        }

        /**
         * 访问当前局部变量
         *
         * @param name      本地变量名
         * @param desc      本地变量名的描述
         * @param signature 本地变量名的签名
         * @param start     该局部变量范围的第一个指令
         * @param end       该局部变量范围的最后一个指令
         * @param index     局部变量的下标
         */
        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
        {
            //如果方法是实例方法，那么方法的第一个参数为this，静态方法的第一个参数就是参数
            int i = index - 1;
            if (Modifier.isStatic(method.getModifiers()))
            {
                i = index;
            }
            //将当前局部变量的名称赋予参数数组
            if (i >= 0 && i < methodParameterNames.length)
            {
                methodParameterNames[i] = name;
            }
            super.visitLocalVariable(name, desc, signature, start, end, index);
        }


        /**
         * 返回方法参数数组
         *
         * @return 方法参数数组
         */
        public String[] getMethodParameterNames()
        {
            return methodParameterNames;
        }
    }
}
