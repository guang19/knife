package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.sensitivefilter.DefaultStopCharRepository;
import com.github.guang19.knife.sensitivefilter.StopCharRepository;

import java.util.Map;
import java.util.Set;

/**
 * @author yangguang
 * @date 2020/3/30
 *
 * <p>
 *  敏感词过滤器模板
 * </p>
 */
public abstract class AbstractSensitiveWordFilter implements SensitiveWordFilter
{

    //敏感词库
    private final SensitiveWordRepository sensitiveWordRepository;

    //是否为贪婪模式
    private boolean greedy = true;

    //在进行过滤时，默认使用此字符进行替换敏感词
    private static final String DEFAULT_REPLACE_CHAR = "*";

    //默认替换敏感词的字符
    private String replaceChar = DEFAULT_REPLACE_CHAR;

    //stop char库
    private final StopCharRepository stopCharRepository;

    /**
     * 默认构造
     */
    protected AbstractSensitiveWordFilter()
    {
        this.sensitiveWordRepository = new DefaultSensitiveWordRepository();
        this.stopCharRepository = new DefaultStopCharRepository();
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository      stop char库
     */
    protected AbstractSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository)
    {
        this.sensitiveWordRepository = sensitiveWordRepository;
        this.stopCharRepository = stopCharRepository;
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository       stop char库
     * @param greedy                  是否以贪婪模式过滤
     */
    protected AbstractSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository, boolean greedy)
    {
        this.sensitiveWordRepository = sensitiveWordRepository;
        this.stopCharRepository = stopCharRepository;
        this.greedy = greedy;
    }

    /**
     * 使用敏感词库创建敏感词过滤器
     * @param sensitiveWordRepository 敏感词库
     * @param stopCharRepository       stop char库
     * @param greedy                  是否以贪婪模式过滤
     * @param replaceChar              替换字符
     */
    protected AbstractSensitiveWordFilter(SensitiveWordRepository sensitiveWordRepository, StopCharRepository stopCharRepository, boolean greedy, String replaceChar)
    {
        this.sensitiveWordRepository = sensitiveWordRepository;
        this.stopCharRepository = stopCharRepository;
        this.greedy = greedy;
        this.replaceChar = replaceChar;
    }

    /**
     * 将指定内容中的敏感词过滤掉
     * @param content   需要过滤的内容
     * @return          过滤以后的内容
     */
    @Override
    public abstract String doFilter(String content);


    /**
     * 获取内容中的敏感词
     *
     * @param content             指定的内容
     * @return 内容中包含的敏感词集合
     */
    public abstract Set<String> getSensitiveWords(String content);

    /**
     * 获取与敏感词相同长度的替换符
     * @param length    敏感词的长度
     * @return          敏感词的替换符
     */
    protected String getSensitiveWordReplaceChar(int length)
    {
        String sensitiveReplaceChar = replaceChar;
        while (sensitiveReplaceChar.length() < length)
        {
            sensitiveReplaceChar += sensitiveReplaceChar;
        }
        return sensitiveReplaceChar;
    }

    /**
     * 向敏感词库里添加敏感词
     *
     * @param sensitiveWord 要添加的敏感词
     */
    @Override
    public void addSensitiveWord(String sensitiveWord)
    {
        sensitiveWordRepository.addSensitiveWord(sensitiveWord);
    }


    /**
     * 从敏感词库中移除敏感词
     *
     * @param sensitiveWord 要移除的敏感词
     */
    @Override
    public void removeSensitiveWord(String sensitiveWord)
    {
        sensitiveWordRepository.removeSensitiveWord(sensitiveWord);
    }

    /**
     * 获取敏感词库
     *
     * @return 敏感词库
     */
    @Override
    public Map<Character, Object> getSensitiveWordRepository()
    {
        return sensitiveWordRepository.getSensitiveWordRepository();
    }

    /**
     * 获取敏感词树结尾标志
     *
     * @return 敏感词树结尾标志
     */
    @Override
    public String getEndFlag()
    {
        return sensitiveWordRepository.getEndFlag();
    }

    /**
     * 想stop char库里添加stop char
     *
     * @param stopChar stop char
     */
    @Override
    public void addStopChar(char stopChar)
    {
        stopCharRepository.addStopChar(stopChar);
    }


    /**
     * 从stop char库里移除stop char
     *
     * @param stopChar stop char
     */
    @Override
    public void removeStopChar(char stopChar)
    {
        stopCharRepository.removeStopChar(stopChar);
    }

    /**
     * 返回敏感词库中敏感词的数量
     *
     * @return 敏感词数量
     */
    @Override
    public int sensitiveWordRepositorySize()
    {
        return sensitiveWordRepository.sensitiveWordRepositorySize();
    }

    /**
     * 获取stop chars的数量
     *
     * @return stop chars的数量
     */
    @Override
    public int stopCharRepositorySize()
    {
        return stopCharRepository.stopCharRepositorySize();
    }


    /**
     * 判断指定字符是否为stop char
     *
     * @param ch 指定的字符
     * @return 如果字符是stop char，返回true
     */
    @Override
    public boolean isStopChar(char ch)
    {
        return stopCharRepository.isStopChar(ch);
    }

    /**
     * 设置贪婪或非贪婪模式
     * @param greedy    true为贪婪模式,false为非贪婪模式
     */
    public void setGreedy(boolean greedy)
    {
        this.greedy = greedy;
    }


    /**
     * 当前敏感词过滤器是否为贪婪模式
     * @return  是否为贪婪模式
     */
    @Override
    public boolean isGreedy()
    {
        return greedy;
    }


    /**
     * 设置默认的替换符
     * @param replaceChar    替换字符
     */
    public void setReplaceChar(String replaceChar)
    {
        this.replaceChar = replaceChar;
    }


    /**
     * 获取替换符
     * @return  replaceChar   替换敏感词的字符
     */
    @Override
    public String getReplaceChar()
    {
        return replaceChar;
    }


    @Override
    public String toString()
    {
        return "AbstractSensitiveWordFilter{" +
                "sensitiveWordRepository=" + sensitiveWordRepository +
                ", greedy=" + greedy +
                ", replaceChar='" + replaceChar + '\'' +
                ", stopCharRepository=" + stopCharRepository +
                '}';
    }
}
