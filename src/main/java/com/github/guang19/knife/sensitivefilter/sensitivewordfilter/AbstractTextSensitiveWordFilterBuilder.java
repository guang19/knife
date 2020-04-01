package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.sensitivefilter.DefaultStopCharRepository;
import com.github.guang19.knife.sensitivefilter.StopCharRepository;
import com.github.guang19.knife.sensitivefilter.sensitivewordfilter.impl.DefaultSensitiveWordFilter;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  敏感词过滤器的构造器
 *  此构造器以文本方式构造
 *
 *  默认的敏感词和stop char来源于这2个github仓库:
 *  https://github.com/fighting41love/funNLP/tree/master/data/%E6%95%8F%E6%84%9F%E8%AF%8D%E5%BA%93
 *  https://github.com/fwwdn/sensitive-stop-words
 * </p>
 */
public abstract class AbstractTextSensitiveWordFilterBuilder implements SensitiveWordFilterBuilder
{
    //敏感词库
    protected SensitiveWordRepository sensitiveWordRepository;

    //stop char库
    protected StopCharRepository stopCharRepository;

    //默认敏感词的平均长度
    private static final int DEFAULT_SENSITIVE_WORD_AVG_LENGTH = 5;

    //敏感词的平均长度
    private int sensitiveWordAvgLength = DEFAULT_SENSITIVE_WORD_AVG_LENGTH;

    //默认的敏感词库
    private static final String DEFAULT_SENSITIVE_WORD_TEXT = "sensitive_word.txt";

    //默认的stop chars库
    private static final String DEFAULT_STOP_CHAR_TEXT = "stopchar.txt";

    //空构造
    protected AbstractTextSensitiveWordFilterBuilder() {}

    /**
     * 敏感词的平均长度构造
     * 根据此参数来计算敏感词库需要的空间
     * @param sensitiveWordAvgLength    敏感词平均长度构造
     */
    protected AbstractTextSensitiveWordFilterBuilder(int sensitiveWordAvgLength)
    {
        this.sensitiveWordAvgLength = sensitiveWordAvgLength;
    }


    /**
     * 构造敏感词库
     * @param sensitiveWordText 敏感词文本文件
     */
    @Override
    public abstract void buildSensitiveWordRepository(String sensitiveWordText);


    /**
     * 构造stop char库
     * @param stopCharText stop char文本文件
     */
    @Override
    public abstract void buildStopCharRepository(String stopCharText);

    /**
     * 构造敏感词过滤器
     *
     * @return 敏感词过滤器
     */
    @Override
    public SensitiveWordFilter build()
    {
        if(sensitiveWordRepository != null && stopCharRepository != null)
        {
            return new DefaultSensitiveWordFilter(sensitiveWordRepository,stopCharRepository);
        }
        else
        {
            //创建默认的敏感词过滤器
            return buildDefaultRepositorySensitiveWordFilter();
        }
    }


    //创建默认敏感词过滤器
    protected final DefaultSensitiveWordFilter buildDefaultRepositorySensitiveWordFilter()
    {
        if(sensitiveWordRepository == null)
        {
            buildSensitiveWordRepository(DEFAULT_SENSITIVE_WORD_TEXT);
        }
        if(stopCharRepository == null)
        {
            buildStopCharRepository(DEFAULT_STOP_CHAR_TEXT);
        }
        return new DefaultSensitiveWordFilter(sensitiveWordRepository,stopCharRepository);
    }


    /**************************************************** 辅助逻辑 ******************************************************************/


    /**
     * 计算敏感词库大概需要多少空间
     * @param sensitiveWordTextBytes 敏感词库字节数
     * @return                       敏感词库大概所需的空间
     */
    protected int computeSensitiveWordRepositorySpace(int sensitiveWordTextBytes)
    {
        //假设敏感词全部为中文，那么每个字符占约3个字节
        //那么每个敏感词的大小就为: 3 * sensitiveWordAvgLength  (字节)
        //那么总共约有: sensitiveWordTextBytes / (3 * sensitiveWordAvgLength) 个敏感词
        //根据我的测试，1万6千多个敏感词，才耗费3千左右的hash桶，由此可见hash冲突之重。
        //考虑hash冲突，可能实际占用的hash桶并没有那么多，那就取总数的4分之一作为词库的容量
        return  (sensitiveWordTextBytes / (3 * sensitiveWordAvgLength)) >> 2;
    }

    /**
     * 计算stop char大概需要多少空间
     * @param stopCharTextBytes stop char字节数
     * @return                  敏感词库大概所需的空间
     */
    protected int computeStopCharRepositorySpace(int stopCharTextBytes)
    {
        //假设每个stop char字符占约3个字节
        //那么总共约有: stopCharTextBytes / 3 个stop char字符
        return stopCharTextBytes / 3;
    }
}