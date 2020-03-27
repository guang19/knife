package com.github.guang19.knife.reflectionutils;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author yangguang
 * @date 2020/3/27
 * @description <p>用于获取方法参数名的方法访问器</p>
 */
public class LocalVariableMethodVisitor extends MethodVisitor
{

    //方法参数名数组
    private String[] methodParameterNames;

    //要获取参数名的方法
    private Method method;

    /**
     * 构造本地变量访问器
     * @param methodVisitor     方法方法器
     * @param method            要获取参数的方法
     */
    public LocalVariableMethodVisitor(MethodVisitor methodVisitor, Method method)
    {
        super(Opcodes.ASM4,methodVisitor);
        this.method = method;
        this.methodParameterNames = new String[method.getParameterCount()];
    }

    /**
     * 访问当前局部变量
     * @param name          本地变量名
     * @param desc          本地变量名的描述
     * @param signature     本地变量名的签名
     * @param start         该局部变量范围的第一个指令
     * @param end           该局部变量范围的最后一个指令
     * @param index         局部变量的下标
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
     * @return  方法参数数组
     */
    public String[] getMethodParameterNames()
    {
        return methodParameterNames;
    }
}
