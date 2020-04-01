package com.github.guang19.knife.sensitivefilter;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  敏感内容过滤器的构造器
 * </p>
 */
public interface SensitiveFilterBuilder
{
    /**
     * 构造敏感内容过滤器
     * @return  敏感内容过滤器
     */
    public abstract SensitiveFilter build();
}
