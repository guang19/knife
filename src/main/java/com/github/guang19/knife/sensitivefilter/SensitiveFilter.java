package com.github.guang19.knife.sensitivefilter;

/**
 * @author yangguang
 * @date 2020/3/30
 *
 * <p>
 *  敏感内容过滤器
 * </p>
 */
public interface SensitiveFilter
{
    /**
     * 将指定内容中的敏感内容过滤掉
     * @param content   需要过滤的内容
     * @return          过滤以后的内容
     */
    public abstract String doFilter(String content);
}
