package com.github.guang19.knife;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p></p>
 */
public class RegexUtilsTest
{
    @Test
    public void test01() throws Exception
    {
        Pattern pattern1 = Pattern.compile("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}");
        System.out.println(pattern1.matcher("010-12461723").matches());

        Pattern pattern2 = Pattern.compile("^1[345789]\\d{9}");
        System.out.println(pattern2.matcher("15071193445").matches());

        Pattern pattern3 = Pattern.compile("^\\w+([+-.]\\w+)*@\\w+\\.\\w+(\\.\\w+)*$");
        System.out.println(pattern3.matcher("2196927727@qq.com").matches());
        System.out.println(pattern3.matcher("asdasd@163.com").matches());
        System.out.println(pattern3.matcher("asd1G3@gmail.com").matches());
        System.out.println(pattern3.matcher("asd1G3@outlook.com").matches());
        System.out.println(pattern3.matcher("123asd-1dca@outlook.com").matches());
        System.out.println(pattern3.matcher("123asd+1dca@outlook.com").matches());
        System.out.println(pattern3.matcher("123asd.1dca@outlook.com").matches());
        System.out.println(pattern3.matcher("123asd1dca@outlook.com.cn").matches());
    }
}