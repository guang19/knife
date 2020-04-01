package com.github.guang19.knife.sensitivefilter.sensitivewordfilter.impl;

import com.github.guang19.knife.regexutils.RegexUtils;
import com.github.guang19.knife.sensitivefilter.StopCharRepository;
import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.AbstractSensitiveWordFilter;
import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.SensitiveWordRepository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  默认的敏感词过滤器
 * </p>
 */
public class DefaultSensitiveWordFilter extends AbstractSensitiveWordFilter
{
    /**
     * 默认构造
     */
    public DefaultSensitiveWordFilter()
    {
        super();
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     *
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository      stop char库
     */
    public DefaultSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository)
    {
        super(sensitiveWordRepository, stopCharRepository);
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     *
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository      stop char库
     * @param greedy                  是否以贪婪模式过滤
     */
    public DefaultSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository, boolean greedy)
    {
        super(sensitiveWordRepository, stopCharRepository, greedy);
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     *
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository      stop char库
     * @param greedy                  是否以贪婪模式过滤
     * @param replaceStr              替换字符
     */
    protected DefaultSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository, boolean greedy, String replaceStr)
    {
        super(sensitiveWordRepository, stopCharRepository, greedy, replaceStr);
    }

    /**
     * 将指定内容中的敏感词过滤掉
     *
     * @param content 需要过滤的内容
     * @return 过滤以后的内容
     */
    @Override
    public String doFilter(String content)
    {
        Set<String> sensitiveWords = getSensitiveWords(content);
        if(!sensitiveWords.isEmpty())
        {
            for (String word : sensitiveWords)
            {
                content = RegexUtils.replaceAll(word,content, getSensitiveWordReplaceChar(word.length()));
            }
        }
        return content;
    }



    /**
     * 获取内容中的敏感词
     *
     * @param content 指定的内容
     * @return 内容中包含的敏感词集合
     */
    @Override
    public Set<String> getSensitiveWords(String content)
    {
        if(content == null || content.isEmpty() || content.trim().isEmpty())
        {
            //返回空的敏感词集合
            return new HashSet<>();
        }
        return checkSensitiveWords(content);
    }


    //获取内容中的敏感词
    @SuppressWarnings("rawtypes")
    private Set<String> checkSensitiveWords(String content)
    {
        Set<String> sensitiveWords = new HashSet<>();
        int length = content.length();
        char[] contentCharArr = content.toCharArray();
        boolean greedy = isGreedy();

        for (int i = 0,sensitiveWordLen = 0 ; i < length; ++i)
        {
            Map searchMap = getSensitiveWordRepository();
            sensitiveWordLen = getSensitiveWordLength(searchMap,contentCharArr,length,i,greedy);
            if(sensitiveWordLen > 0)
            {
                sensitiveWords.add(content.substring(i,sensitiveWordLen + i));
                //为了效率，直接从当前敏感词后开始匹配，就不再每个字符都匹配了
                i = sensitiveWordLen + i - 1;
            }
        }
        return sensitiveWords;
    }

    //获取敏感词长度
    //如果有敏感词，则返回敏感词的长度，否则返回0
    @SuppressWarnings("rawtypes")
    private int getSensitiveWordLength(Map searchMap,char[] content,int length,int begin,boolean greedy)
    {
        int start = 0;
        boolean flag = false;
        char keyChar;

        for (int i = begin ; i < length; ++i)
        {
            //如果是stop char，则直接跳过，但并不影响计数
            if(isStopChar(keyChar = content[i]))
            {
                ++start;
            }
            //没有获取到敏感词直接返回
            else if((searchMap = (Map)searchMap.get(keyChar)) == null)
            {
                {
                    //字符不在敏感词树中就跳过
                    break;
                }
            }
            else
            {
                //当前字符在敏感词树中
                ++start;
                if((boolean)searchMap.get(getEndFlag()))
                {
                    flag = true;
                    //如果是非贪婪模式，就不再继续查找，否则就继续查找
                    if(!greedy)
                    {
                        break;
                    }
                }
            }
        }
        return flag ? start : 0;
    }
}
