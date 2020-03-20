package com.github.guang19.knife.idgenerator;

import java.util.UUID;

/**
 * @author yangguang
 * @date 2020/3/20
 * @description <p>id生成器，各位同学如果有自己的好的方案，也可以提供实现</p>
 */
public interface IdGenerator
{
    /**
     * 生成 long 类型的id
     * @return  id
     */
    public abstract long generateId();

    /**
     * 生成UUID,UUID虽然几乎不会重复，但是不适合作为主键，它的损耗太大了。
     * @return      UUID
     */
    public default String generateStrId()
    {
        return UUID.randomUUID().toString();
    }
}
