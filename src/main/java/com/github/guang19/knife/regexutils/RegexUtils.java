package com.github.guang19.knife.regexutils;

import java.util.regex.Pattern;

/**
 * @author yangguang
 * @date 2020/3/30
 * @description <p>正则工具</p>
 */
public class RegexUtils
{
    /**
     * 判断指定字符串是否完全匹配指定的真这个
     * @param pattern   正则
     * @param str       字符串
     * @return          是否匹配
     */
    public static boolean match(String pattern,String str)
    {
        return Pattern.compile(pattern).matcher(str).matches();
    }

    /**
     * 判断指定字符串是否包含了指定正则的内容
     * @param pattern       正则
     * @param str           字符串
     * @return              是否包含
     */
    public static boolean find(String pattern,String str)
    {
        return Pattern.compile(pattern).matcher(str).find();
    }


    /**
     * 将字符串中匹配正则的部分全部替换掉
     * @param pattern   正则
     * @param str       字符串
     * @return          替换后的字符串
     */
    public static String replaceAll(String pattern, String str)
    {
        return Pattern.compile(pattern).matcher(str).replaceAll("");
    }

    /**
     * 将字符串中匹配正则的部分全部替换为目标字符串
     * @param pattern           正则
     * @param str               字符串
     * @param targetChar        目标字符串
     * @return                   替换后的字符串
     */
    public static String replaceAll(String pattern,String str,String targetChar)
    {
        return Pattern.compile(pattern).matcher(str).replaceAll(targetChar);
    }

    /**
     * 判断字符串是否为手机号
     * @param str       字符串
     * @return          如果字符串是手机号
     */
    public static boolean isTelephone(String str)
    {
        return Pattern.compile(CommonRegexExpression.TELEPHONE.getPattern()).matcher(str).matches();
    }


    /**
     * 判断字符串是否为邮箱
     * @param str       字符串
     * @return          如果字符串是邮箱
     */
    public static boolean isEmail(String str)
    {
        return Pattern.compile(CommonRegexExpression.EMAIL.getPattern()).matcher(str).matches();
    }

    /**
     * 判断字符串是否为性别
     * @param str       字符串
     * @return          如果字符串是性别
     */
    public static boolean isGender(String str)
    {
        return Pattern.compile(CommonRegexExpression.GENDER.getPattern()).matcher(str).matches();
    }

    /**
     * 判断字符串是否为http url
     * @param str   字符串
     * @return      如果字符串是http url
     */
    public static boolean isHttpURL(String str)
    {
        return Pattern.compile(CommonRegexExpression.HTTP_URL.getPattern()).matcher(str).matches();
    }

    /**
     * 将带有html的内容全部替换为空串
     * @param content 带有html标签的字符串
     * @return        替换后的字符串
     */
    public static String replaceHtml(String content)
    {
        return Pattern.compile(CommonRegexExpression.HTML_TAG.getPattern()).matcher(content).replaceAll("").trim();
    }

    /**
     * 将字符串中的特殊字符串全部替换为空串
     * @param content    带有特殊字符的字符串
     * @return            替换后的字符串
     */
    public static String replaceSpecialCharacter(String content)
    {
        return Pattern.compile(CommonRegexExpression.SPECIAL_CHAR.getPattern()).matcher(content).replaceAll("");
    }
}
