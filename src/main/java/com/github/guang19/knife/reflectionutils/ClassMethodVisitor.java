package com.github.guang19.knife.reflectionutils;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author yangguang
 * @date 2020/3/27
 * @description <p>类方法访问器</p>
 */
public class ClassMethodVisitor extends ClassVisitor
{

    //类字节码生成器
    private static final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

    //asm类字节码读取器
    private ClassReader classReader;

    //需要获取参数的方法
    private Method method;

    //用于获取方法参数名的方法访问器实例
    private LocalVariableMethodVisitor localVariableMethodVisitor;

    /**
     * 构造类方法访问器
     * @param method    要获取参数的方法
     */
    private ClassMethodVisitor(Method method)
    {
        super(Opcodes.ASM4,classWriter);
        this.method = method;
    }

    /**
     * 获取方法的参数名
     * @param method    方法
     * @return          参数名数组
     */
    public static String[] getMethodParameterNames(Method method)
    {
        try
        {
            return new ClassMethodVisitor(method).accept();
        }
        catch (IOException e)
        {
            //读取类的信息过程中的IO异常
            throw new RuntimeException("Cannot read bytecode information for class : " + method.getDeclaringClass());
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
        classReader.accept(this,0);
        return localVariableMethodVisitor.getMethodParameterNames();
    }

    /**
     * 访问当前类的某个方法
     * @param access            访问权限
     * @param name              方法名
     * @param descriptor        方法描述
     * @param signature         方法签名
     * @param exceptions        方法异常信息
     * @return                  MethodVisitor
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
    {
        //如果要查找的方法名和当前方法名相同就进行查询
        if(method.getName().equals(name))
        {
            Type[] argumentTypes = Type.getArgumentTypes(descriptor);
            //如果方法参数的数量和类型都相同就继续查询
            if(sameType(argumentTypes,method.getParameterTypes()))
            {
                MethodVisitor methodVisitor = classWriter.visitMethod(access, name, descriptor, signature, exceptions);
                return (localVariableMethodVisitor =  new LocalVariableMethodVisitor(methodVisitor,method));
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    //判断2个类型数组的值是否相等
    private boolean sameType(Type[] types1 , Class<?>[] types2)
    {
        if(types1.length != types2.length)
        {
            return false;
        }
        for (int i = 0 ; i < types1.length ; ++i)
        {
            if(!types1[i].equals(Type.getType(types2[i])))
            {
                return false;
            }
        }
        return true;
    }


}
