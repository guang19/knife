package com.github.guang19.knife.mail;

/**
 * @author yangguang
 * @date 2020/3/29
 * @description <p>邮箱配置MailConfiguration解析器</p>
 */
public interface MailConfigurationParser
{
    /**
     * 解析并返回邮箱配置
     *
     * @return MailConfiguration
     * @throws Exception 解析配置过程中的异常
     */
    public MailConfiguration parse() throws Exception;
}
