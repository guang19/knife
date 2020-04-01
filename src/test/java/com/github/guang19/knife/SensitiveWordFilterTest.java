package com.github.guang19.knife;

import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.SensitiveWordFilter;
import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.TextSensitiveWordFilterBuilder;
import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.impl.DefaultSensitiveWordFilter;
import org.junit.Test;

/**
 * @author yangguang
 * @date 2020/3/30
 *
 * <p>
 *
 * </p>
 */
public class SensitiveWordFilterTest
{
    @Test
    public void test01() throws Exception
    {
        //创建默认的敏感词过滤器
        SensitiveWordFilter sensitiveWordFilter = new TextSensitiveWordFilterBuilder(6).build();

        sensitiveWordFilter.addSensitiveWord("傻子");

        System.out.println(sensitiveWordFilter.getSensitiveWords("你真是个大傻子呀"));

        //设置非贪婪模式，匹配更短的字符串
        ((DefaultSensitiveWordFilter)sensitiveWordFilter).setGreedy(false);
        System.out.println(sensitiveWordFilter.getSensitiveWords("你真，是个大傻，子呀,你真是个大傻子呀"));

        //有 '，' 符号， '，' 会被当做stop char，所以这种程度的干扰是无效的
        System.out.println(sensitiveWordFilter.getSensitiveWords("你真，是个大傻，子呀,你真是个大傻子呀"));

        //替换
        System.out.println(sensitiveWordFilter.doFilter("你真是个大傻子呀哈哈哈哈哈哈哈"));
    }

    @Test
    public void test02() throws Exception
    {
        //创建默认的敏感词过滤器
        SensitiveWordFilter sensitiveWordFilter = new TextSensitiveWordFilterBuilder(6).build();
        sensitiveWordFilter.addSensitiveWord("你真是个大傻子");
        sensitiveWordFilter.addSensitiveWord("你真是个大傻子呀");
        sensitiveWordFilter.addSensitiveWord("你真是个大傻啊呀");
        sensitiveWordFilter.addSensitiveWord("呀真是个大傻子呀");


        sensitiveWordFilter.removeSensitiveWord("呀真是个大傻子呀");


        System.out.println(sensitiveWordFilter.getSensitiveWords("你真是个大傻子呀"));
    }
}
