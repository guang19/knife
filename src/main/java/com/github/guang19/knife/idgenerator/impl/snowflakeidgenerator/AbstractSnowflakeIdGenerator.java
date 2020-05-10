package com.github.guang19.knife.idgenerator.impl.snowflakeidgenerator;

import com.github.guang19.knife.idgenerator.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author yangguang
 * @date 2020/3/20
 * @description <p>
 * 雪花ID生成器模板
 * </p>
 */
public abstract class AbstractSnowflakeIdGenerator implements IdGenerator
{
    //logger
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSnowflakeIdGenerator.class);

    //开始时间戳.
    protected final long START_EPOCH;

    /**
     * 开始时间构造
     *
     * @param startTime 开始时间
     */
    protected AbstractSnowflakeIdGenerator(LocalDateTime startTime)
    {
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("The snowflake id generator starting time is {}", startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        //从当前时间戳算起，这样尽可能最大化 ID 使用时间 (32位时间戳可以使用4年，40位时间戳可以使用34)
        this.START_EPOCH = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
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


    /**
     * 生成 long 类型的id
     *
     * @return id
     */
    @Override
    public long generateId()
    {
        return nextId();
    }

    /**
     * 子类只需要完成这个nextId的逻辑就行了
     *
     * @return 生成雪花Id
     */
    protected abstract long nextId();


    /**
     * 获取当前毫秒级的时间戳,此方法待扩展
     * 因为currentTimeMillis性能不是特别好
     * <p>
     * 我已经做出了一个使用clock_gettime函数来获取系统时间戳的JNI库
     * 但是我经过测试后发现:
     * 虽然clock_gettime函数确实比gettimeofday函数要快，但是通过
     * Java层面的JNI调用，反而没有gettimeofday函数快了，
     * 我想这应该是JVM对JNI的优化，再加之我只是普通的实现了功能，并没有那个能力优化，
     * 所以还是使用currentTimeMillis较好。
     * <p>
     * 如果你想尝试我写的JNI调用的话，这个是它的实现类:
     * {@link com.github.guang19.knife.timeutils.SystemTime}
     * <p>
     * JNI源码在: resources/jni/time下
     *
     * @return 当前时间戳
     */
    protected long currentMillisTimestamp()
    {
        return System.currentTimeMillis();
    }

    /**
     * 阻塞当前线程，直至获取下一毫秒，此方法的代价是while循环，会使cpu可能在某个时间点开销变大
     * 所以此方法也有待扩展
     *
     * @param lastTimestamp 上次获取到的时间戳
     * @return 下一秒的时间戳
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
