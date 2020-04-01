package com.github.guang19.knife.sensitivefilter;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  在进行敏感内容过滤时，
 *  有些恶意内容在敏感词中间添加了无意义的符号，使得过滤困难。
 *  如恶意内容为:  你这个傻大个 其中敏感词为: 傻大个
 *  恶意内容被替换后为: 你这个傻,大个 。   这样就无法过滤其中的敏感词，所以就有了StopChar(被忽略，无需处理的字符)。
 *  在遇到StopChar的内容时，直接无视它，或者先将要过滤的内容的所有StopChar全部替换掉，这样就能正确过滤了。
 *
 *  不过普通符号还行，如果遇到: 你这个sha大个  这种中英文混合的敏感内容时，又显得有些无力了。
 * </p>
 */
public interface StopCharRepository
{
    /**
     * 向stop char库里添加stop char
     * @param stopChar stop char
     */
    public abstract void addStopChar(char stopChar);


    /**
     * 移除stop char
     * @param stopChar stop char
     */
    public abstract void removeStopChar(char stopChar);

    /**
     * 判断指定字符是否为stop char
     * @param ch    指定的字符
     * @return      如果字符是stop char，返回true
     */
    public abstract boolean isStopChar(char ch);

    /**
     * 获取stop chars的数量
     * @return  stop chars的数量
     */
    public abstract int stopCharRepositorySize();
}
