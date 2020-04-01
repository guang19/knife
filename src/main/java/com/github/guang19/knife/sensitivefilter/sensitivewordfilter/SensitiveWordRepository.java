package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.sensitivefilter.StopCharRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *   敏感词库
 * </p>
 */
public interface SensitiveWordRepository
{
    /**
     * 向敏感词库里添加敏感词
     * @param sensitiveWord 敏感词
     */
    public abstract void addSensitiveWord(String sensitiveWord);

    /**
     * 从敏感词库中删除敏感词
     * @param sensitiveWord 要删除的敏感词
     */
    public abstract void removeSensitiveWord(String sensitiveWord);

    /**
     * 获取敏感词库
     * @return  敏感词库
     */
    public abstract Map<Character,Object> getSensitiveWordRepository();

    /**
     * 获取敏感词树结尾标志
     * @return      敏感词树结尾标志
     */
    public abstract String getEndFlag();

    /**
     * 返回敏感词库中敏感词的数量
     * @return  敏感词数量
     */
    public abstract int sensitiveWordRepositorySize();
}
