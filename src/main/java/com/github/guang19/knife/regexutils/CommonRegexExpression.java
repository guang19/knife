package com.github.guang19.knife.regexutils;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>常用正则表达式</p>
 */
public enum CommonRegexExpression
{
    //固定电话正则，我国主要有: 3+8、4+8、3+7等类型的固话,所以为了兼容，此正则可能会损失一定准确性
    LANDLINE("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}"),

    //手机号，此正则只考虑国内. 网络号段根据我查询的资料,目前有: 13x,14x,15x,17x,18x,19x
    TELEPHONE("^1[345789]\\d{9}"),

    //固话和手机号的结合。有的应用可能需要支持两种电话类型，所以此处也提供
    LANDLINE_OR_TELEPHONE("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}|^1[345789]\\d{9}"),

    //邮箱正则
    EMAIL("^\\w+([+-.]\\w+)*@\\w+\\.\\w+(\\.\\w+)*$"),

    //域名正则
    DOMAIN("[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?"),

    //http或https协议的url
    HTTP_URL("^(http:|https:)//(\\w+\\.)+\\w+(/[\\w/.?&=%-]*)?$"),

    //纯数字正则
    NUMBERS_STR("^\\d+$"),

    //纯汉字正则
    CHINESE_STR("^[\\u4e00-\\u9fa5]+$"),

    //纯英文正则
    ENGLISH_STR("^[a-zA-Z]+$"),

    //纯空白字符,
    BLANK_STR("^\\s+$"),

    //html标签正则
    HTML_TAG("<(\\S*)[^<]*>[\\n\\s]*(.)*?|<(.)*?/>"),

    //性别正则
    GENDER("男|女"),

    //用户名正则。 允许包含大小写字母，汉字和下划线
    USERNAME("^[\\w\\u4e00-\\u9fa5]+$"),

    //强密码正则1。强密码必须包含大小写字母，数字，不允许特殊字符
    STRONG_PASSWORD("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]*$"),

    //强密码正则2。强密码必须包含大小写字母，数字，允许部分特殊字符:  ! ? _ - : . @ ^ &
    STRONG_PASSWORD_WITH_SPECIAL_CHAR("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[\\w!?\\-:.@\\^&]*$"),

    //特殊符号正则
    SPECIAL_CHAR("[+\\[\\]~`～！!@#￥$%……^&*×()（）_=\\-—【】{};'\":：；;\\t、|《》<>?/\\\\.,\"\"“”，。]*");


    //正则表达式pattern
    private final String pattern;

    /**
     * 构造函数
     *
     * @param pattern 正则表达式pattern
     */
    private CommonRegexExpression(String pattern)
    {
        this.pattern = pattern;
    }

    /**
     * getter
     *
     * @return 正则表达式pattern
     */
    public String getPattern()
    {
        return pattern;
    }
}
