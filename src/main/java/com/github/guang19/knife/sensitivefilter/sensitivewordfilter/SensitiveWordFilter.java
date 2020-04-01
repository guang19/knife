package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.sensitivefilter.SensitiveFilter;
import com.github.guang19.knife.sensitivefilter.StopCharRepository;

import java.util.Set;


/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 * 敏感词过滤器
 * </p>
 */
public interface SensitiveWordFilter extends SensitiveFilter,StopCharRepository,SensitiveWordRepository
{
    /**
     * 当前敏感词过滤器是否为贪婪模式
     * @return  是否为贪婪模式
     */
    public abstract boolean isGreedy();

    /**
     * 获取替换符
     * @return  replaceChar   替换敏感词的字符
     */
    public abstract String getReplaceChar();

    /**
     * 获取内容中的敏感词
     *
     * @param content         指定的内容
     * @return              内容中包含的敏感词集合
     */
    public abstract Set<String> getSensitiveWords(String content);
}
