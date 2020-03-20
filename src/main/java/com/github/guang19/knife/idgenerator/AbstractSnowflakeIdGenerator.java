package com.github.guang19.knife.idgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author yangguang
 * @date 2020/3/20
 * @description
 * <p>
 *    雪花ID生成器模板
 * </p>
 */
public abstract class AbstractSnowflakeIdGenerator implements IdGenerator
{
    //logger
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSnowflakeIdGenerator.class);

    //开始时间戳.
    protected final long START_EPOCH;

    {
        //从当前时间戳算起，这样尽可能最大化 ID 使用时间 (32位时间戳可以使用4年，40位时间戳可以使用(34年))
        START_EPOCH = LocalDateTime.now(Clock.systemDefaultZone()).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        try
        {
            //sleep for a short period of time , because [START_EPOCH] start at current time
            //睡眠一小段时间，避免刚创建生成器就生成ID，那样第一个ID可能会为0
            TimeUnit.NANOSECONDS.sleep(1);
        }
        catch (Throwable e)
        {
            //needn't do things
        }
    }

    @Override
    public abstract long generateId();

    /**
     * 获取当前毫秒级的时间戳,此方法待扩展
     * currentTimeMillis性能不是特别好
     * @return  当前时间戳
     */
    protected long currentMillisTimestamp()
    {
        return System.currentTimeMillis();
    }

    /**
     * 阻塞当前线程，直至获取下一毫秒
     * @param lastTimestamp 上次获取到的时间戳
     * @return              下一秒的时间戳
     */
    protected long untilNextMillis(long lastTimestamp)
    {
        long curTimestamp = currentMillisTimestamp();
        while (curTimestamp <= lastTimestamp)
        {
            curTimestamp = currentMillisTimestamp();
        }
        return curTimestamp;
    }
}
