package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.sensitivefilter.SensitiveFilterBuilder;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 * 敏感词过滤器的构造器
 * </p>
 */
public interface SensitiveWordFilterBuilder extends SensitiveFilterBuilder
{

    /**
     * 构造敏感词库
     * @param sensitiveWordText 敏感词文本文件
     */
    public abstract void buildSensitiveWordRepository(String sensitiveWordText);


    /**
     * 构造stop char库
     * @param stopCharText stop char文本文件
     */
    public abstract void buildStopCharRepository(String stopCharText);
}
