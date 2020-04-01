package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import com.github.guang19.knife.ResourceUtils;
import com.github.guang19.knife.sensitivefilter.DefaultStopCharRepository;

import java.io.*;

/**
 * @author yangguang
 * @date 2020/3/31
 *
 * <p>
 *  以文本的方式构造敏感词过滤器
 * </p>
 */
public class TextSensitiveWordFilterBuilder extends AbstractTextSensitiveWordFilterBuilder
{
    /**
     * 空构造
     */
    public TextSensitiveWordFilterBuilder()
    {
        super();
    }

    /**
     * 敏感词的平均长度构造
     *
     * @param sensitiveWordAvgLength 敏感词平均长度构造
     */
    public TextSensitiveWordFilterBuilder(int sensitiveWordAvgLength)
    {
        super(sensitiveWordAvgLength);
    }

    /**
     * 构造敏感词库
     *
     * @param sensitiveWordText 敏感词文本文件
     */
    @Override
    public void buildSensitiveWordRepository(String sensitiveWordText)
    {
        this.sensitiveWordRepository = createSensitiveWordRepository(sensitiveWordText);
    }

    /**
     * 创建敏感词库
     * @param sensitiveWordText 敏感词文本文件
     * @return                  敏感词库
     */
    private DefaultSensitiveWordRepository createSensitiveWordRepository(String sensitiveWordText)
    {
        try(InputStream sensitiveWordTextStream = ResourceUtils.loadClasspathResource(sensitiveWordText))
        {
            if(sensitiveWordTextStream == null)
            {
                throw new FileNotFoundException("cannot found sensitive word text file : " + sensitiveWordText);
            }
            DefaultSensitiveWordRepository sensitiveWordRepository = new DefaultSensitiveWordRepository(computeSensitiveWordRepositorySpace(sensitiveWordTextStream.available()));
            loadSensitiveRepository(sensitiveWordTextStream,sensitiveWordRepository);
            return sensitiveWordRepository;
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception occurred while creating sensitive word repository.",e);
        }
    }

    //将敏感词加载进敏感词库
    private void loadSensitiveRepository(InputStream sensitiveWordTextStream , DefaultSensitiveWordRepository sensitiveWordRepository) throws IOException
    {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sensitiveWordTextStream)))
        {
            String sensitiveWord = null;
            while ((sensitiveWord = bufferedReader.readLine()) != null)
            {
                sensitiveWordRepository.addSensitiveWord(sensitiveWord);
            }
        }
    }


    /**
     * 构造stop char库
     *
     * @param stopCharText stop char文本文件
     */
    @Override
    public void buildStopCharRepository(String stopCharText)
    {
        this.stopCharRepository = createStopCharRepository(stopCharText);
    }

    /**
     * 构造stop char
     * @param stopCharText stop char文本文件
     */
    private DefaultStopCharRepository createStopCharRepository(String stopCharText)
    {
        try(InputStream stopCharTextStream = ResourceUtils.loadClasspathResource(stopCharText))
        {
            if(stopCharTextStream == null)
            {
                throw new FileNotFoundException("cannot found stop char text file : " + stopCharText);
            }
            DefaultStopCharRepository stopCharRepository = new DefaultStopCharRepository(computeStopCharRepositorySpace(stopCharTextStream.available()));
            loadStopChar(stopCharTextStream,stopCharRepository);
            return stopCharRepository;
        }
        catch (Exception e)
        {
            throw new RuntimeException("an exception occurred while creating stop char repository.",e);
        }
    }

    //将stop char加载进内存
    private void loadStopChar(InputStream stopCharTextStream , DefaultStopCharRepository stopCharRepository) throws IOException
    {
        try( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stopCharTextStream)))
        {
            String stopChar = null;
            while ((stopChar = bufferedReader.readLine()) != null)
            {
                //stop char文件必须每行一个字符
                stopCharRepository.addStopChar(stopChar.charAt(0));
            }
        }
    }

}
