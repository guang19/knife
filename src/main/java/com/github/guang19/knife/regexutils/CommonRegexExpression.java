package com.github.guang19.knife.regexutils;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>常用正则表达式</p>
 */
public enum  CommonRegexExpression
{
    //固定电话正则，我国主要有: 3+8、4+8、3+7等类型的固话,所以为了兼容，此正则可能会损失一定准确性
    LANDLINE("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}"),

    //手机号，此正则只考虑国内. 网络号段根据我查询的资料,目前有: 13x,14x,15x,17x,18x,19x
    TELEPHONE("^1[345789]\\d{9}"),

    //固话和手机号的结合。有的应用可能需要支持两种电话类型，所以此处也提供
    LANDLINE_OR_TELEPHONE("\\d{3}-\\d{8}|\\d{3}-\\d{7}|\\d{4}-\\d{8}|^1[345789]\\d{9}"),

    //邮箱正则
    EMAIL("^\\w+([+-.]\\w+)*@\\w+\\.\\w+(\\.\\w+)*$");




    //正则表达式pattern
    private final String regexExpression;

    /**
     * 构造函数
     * @param regexExpression 正则表达式pattern
     */
    private CommonRegexExpression(String regexExpression)
    {
        this.regexExpression = regexExpression;
    }

    /**
     * getter
     * @return 正则表达式pattern
     */
    public String getRegexExpression()
    {
        return regexExpression;
    }
}
