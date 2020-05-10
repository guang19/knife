package com.github.guang19.knife;

import com.github.guang19.knife.regexutils.CommonRegexExpression;
import com.github.guang19.knife.regexutils.RegexUtils;
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

    @Test
    public void test02() throws Exception
    {
        Pattern pattern = Pattern.compile("^(http:|https:)//(\\w+\\.)+\\w+(/[\\w/.?&=%-]*)?$");
        System.out.println(pattern.matcher("https://top.com.cn/user/a/123/").matches());
        System.out.println(pattern.matcher("https://icon.com.cn/user?a=123/").matches());
        System.out.println(pattern.matcher("https://www.icon.com.cn/").matches());
        System.out.println(pattern.matcher("https://www.icon.com.cn").matches());
        System.out.println(pattern.matcher("https://www.icon.com").matches());
        System.out.println(pattern.matcher("https://www.icon.com/").matches());
        System.out.println(pattern.matcher("https://top.com.cn/-a?").matches());

        System.out.println("------------------------------");

//        Pattern pattern2 = Pattern.compile("^\\s+$");
//        System.out.println(pattern2.matcher("       ").matches());

        Pattern pattern3 = Pattern.compile("<(\\S*)[^<]*>\\n*(.)*?\\n*|<(.)*?/>");
        Matcher matcher1 = pattern3.matcher("<div class=\"dadasda\" strong=\"asdasd\" style=\"asdasd\">asdasd</div>");
        Matcher matcher2 = pattern3.matcher("<div id=\"dadasda\">\n" +
                "asdasd\n" +
                "</div>");
        Matcher matcher3 = pattern3.matcher("<mapper adasd=\"123123\"/>\n");

        System.out.println(matcher1.find());
        System.out.println(matcher2.find());
        System.out.println(matcher3.find());

        System.out.println(matcher1.replaceAll(""));
        System.out.println(matcher2.replaceAll(""));
        System.out.println(matcher3.replaceAll(""));
    }

    @Test
    public void test03() throws Exception
    {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher("12323123asdasd");
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        System.out.println(matcher.find());
        System.out.println(matcher.group());
        System.out.println(matcher.find());
        System.out.println(matcher.group());

        System.out.println(matcher.replaceAll("a"));
    }

    @Test
    public void test04() throws Exception
    {
        System.out.println(RegexUtils.isTelephone("15071193445"));
        System.out.println(RegexUtils.isEmail("2196927727@qq.com"));
        System.out.println(RegexUtils.isGender("男"));
        System.out.println(RegexUtils.isHttpURL("http://123.com"));
        System.out.println(RegexUtils.isHttpURL("http://123.com/"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn/"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn/user/a=1"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn/user/a=1/"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn/user?name=yx"));
        System.out.println(RegexUtils.isHttpURL("https://1asdasd.com.cn/user?name=yx/"));
    }

    @Test
    public void test05() throws Exception
    {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "asdasdas\n" +
                "</body>\n" +
                "</html>";
        System.out.println(RegexUtils.replaceHtml(html));

        System.out.println(".）（a".matches("[+\\[\\]~`～！!@#￥$%……^&*×()（）_=\\-—【】{};'\":：；;\\t、|《》<>?/\\\\.,\"\"“”，。]*"));

        System.out.println("asdasd.?{}()aa".replaceAll("[+\\[\\]~`～！!@#￥$%……^&*×()（）_=\\-—【】{};'\":：；;\\t、|《》<>?/\\\\.,\"\"“”，。]*", ""));
    }

    @Test
    public void test06() throws Exception
    {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "asdasdas\n" +
                "</body>\n" +
                "</html>";
        System.out.println(RegexUtils.replaceHtml(html).trim());
        System.out.println(RegexUtils.replaceHtml(html).trim().length());
    }
}