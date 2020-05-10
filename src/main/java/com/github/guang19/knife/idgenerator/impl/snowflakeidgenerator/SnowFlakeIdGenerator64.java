package com.github.guang19.knife.idgenerator.impl.snowflakeidgenerator;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author yangguang
 * @date 2020/3/20
 * @description 53位雪花ID生成器
 * <p>
 * 64位雪花ID组成如下:
 * <p>
 * 1位符号位不用                            41位时间戳                              10位工作机器id        12位自增序列
 * <p>
 * 41位时间戳:    ((1L << 41) / (1000L * 60 * 60 * 24 * 365)) 表示41位时间戳的ID能够用69年，如果用不到这么长，可以减少
 * 10位机器id:    (1L << 10)  记录机器的ID，可以表示1024个节点，如果没有那么多，可以减少
 * 12位自增序列:  (1L << 12)  代表生成的id单位为: 4096/ms, 如果并发量没有这么高，也可以减少
 * 0        -       00000000 00000000 00000000 00000000 00000000 0    -     00000000 00     -   00000000 0000
 * <p>
 * 64位雪花id最大能表示的数值为: 2^63 - 1
 *
 * <p>
 * 前端js最大安全整数的范围为 2^53 - 1
 * 请酌情使用本生成器
 * </p>
 * <p>
 * 我觉得原本的41位时间戳和机器ID还是太大，所以就减少了一位时间戳和一位机器ID，增加了2位自增序列，使得毫秒内生成的id大大增加
 * <p>
 * 改进后的生成器的位数如下:
 * <p>
 * 1位符号位不用                            40位时间戳                             9位工作机器id        14位自增序列
 * <p>
 * 40位时间戳:    ((1L << 40) / (1000L * 60 * 60 * 24 * 365)) 表示40位时间戳的ID能够用34年，如果用不到这么长，可以减少
 * 9位机器id:    (1L <<  9)  记录机器的ID，可以表示512个节点，如果没有那么多，可以减少
 * 14位自增序列:  (1L <<  14)  代表生成的id单位为: 16384/ms, 如果并发量没有这么高，也可以减少
 * 0        -       00000000 00000000 00000000 00000000 00000000   -     00000000 0     -   00000000 000000
 */
public class SnowFlakeIdGenerator64 extends AbstractSnowflakeIdGenerator
{

    //工作机器ID占9位 ，最大支持 (1L << 9) = 512 台机器 : 0 - 511
    private final long MACHINE_ID_BIT = 9L;

    /****************************************************************************/

    //当前机器ID,范围为 MIN_MACHINE_ID - MAX_MACHINE_ID : [0 - 511]
    //256台正常使用，256台做备份: 0 - 255 是正常使用的机器ID， 256 - 511 是备用机器ID
    private final long MACHINE_ID;

    //正常使用的最小机器ID
    private final long MIN_MACHINE_ID = 0L;

    //正常使用的最大机器ID : (1L << 9L) - 1 = 511 , 511 >> 1 = 255
    private final long MAX_MACHINE_ID = ((1L << MACHINE_ID_BIT) - 1) >> 1;

    /****************************************************************************/

    //当前机器ID的备用机器ID,范围为 MIN_BACKUP_MACHINE_ID - MAX_BACKUP_MACHINE_ID : [256 - 511]
    //256台正常使用，256台做备份: 0 - 255 是正常使用的机器ID， 256 - 511是备用机器ID
    private final long BACKUP_MACHINE_ID;

    //备用机器ID最小值: (1L << 9) = 512 , 512 >> 1 = 256
    private final long MIN_BACKUP_MACHINE_ID = (1L << MACHINE_ID_BIT) >> 1;

    //备用机器ID最大值 : (1L << 9) - 1 = 511
    private final long MAX_BACKUP_MACHINE_ID = ~(-1L << MACHINE_ID_BIT);

    /****************************************************************************/

    //自增序列 ，占14位， 每毫秒支持 1L << 14 = 16384 个id : 0 - 16383
    private final long INCR_SEQUENCE_BIT = 14L;

    //自增序列最大值 : (1L << 13) - 1 = 16383
    private final long MAX_INCR_SEQUENCE_BIT = ~(-1L << INCR_SEQUENCE_BIT);

    /****************************************************************************/

    //机器ID移位数, 机器ID的右边位段是当前毫秒的自增序列，所以需要左移自增序列的位数
    private final long MACHINE_ID_SHIFT = INCR_SEQUENCE_BIT;

    //时间戳移位数, 时间戳是最高位，所以需要左移 (机器ID的位数 +  自增序列的位数)
    private final long TIMESTAMP_SHIFT = MACHINE_ID_BIT + INCR_SEQUENCE_BIT;

    /*****************************************************************************/

    //当前机器上次生成的时间戳
    private long lastTimestamp = -1L;

    //备用机器上次生成的时间戳
    private long backupMachineLastTimestamp = -1L;

    //当前机器的毫秒内的自增序列(0 - 8191)
    private long curMillSequence = 0L;

    //备用机器的毫秒内的自增序列(0 - 8191)
    private long backupMachineCurMillSequence = 0L;

    //允许时钟回拨的最大值 1s
    private final long MAX_BACKWARD_TIME = 800L;

    /****************************************************************************/

    /**
     * 构造函数
     *
     * @param machineId       当前工作机器的ID
     * @param backupMachineId 当前工作机器的备用机器的ID
     */
    public SnowFlakeIdGenerator64(long machineId, long backupMachineId)
    {
        //默认以创建id生成器实例的时间开始计算
        super(LocalDateTime.now(Clock.systemDefaultZone()));
        //machine id 不能超出范围
        if (machineId < MIN_MACHINE_ID || machineId > MAX_MACHINE_ID)
        {
            throw new IllegalArgumentException(String.format("machine id must be between %d - %d", MIN_MACHINE_ID, MAX_MACHINE_ID));
        }
        //backup machine id 不能超出范围
        if (backupMachineId < MIN_BACKUP_MACHINE_ID || backupMachineId > MAX_BACKUP_MACHINE_ID)
        {
            throw new IllegalArgumentException(String.format("backup machine id must be between %d - %d", MIN_BACKUP_MACHINE_ID, MAX_BACKUP_MACHINE_ID));
        }
        this.MACHINE_ID = machineId;
        this.BACKUP_MACHINE_ID = backupMachineId;
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.info("The snowflake ID generator with machine id {} is ready , it's backup machine id is {} .", MACHINE_ID, BACKUP_MACHINE_ID);
        }
    }

    /**
     * 构造函数
     *
     * @param machineId       当前工作机器的ID
     * @param backupMachineId 当前工作机器的备用机器的ID
     * @param startTime       id生成器开始的时间
     */
    public SnowFlakeIdGenerator64(long machineId, long backupMachineId, LocalDateTime startTime)
    {
        //默认以创建id生成器实例的时间开始计算
        super(startTime);
        //machine id 不能超出范围
        if (machineId < MIN_MACHINE_ID || machineId > MAX_MACHINE_ID)
        {
            throw new IllegalArgumentException(String.format("machine id must be between %d - %d", MIN_MACHINE_ID, MAX_MACHINE_ID));
        }
        //backup machine id 不能超出范围
        if (backupMachineId < MIN_BACKUP_MACHINE_ID || backupMachineId > MAX_BACKUP_MACHINE_ID)
        {
            throw new IllegalArgumentException(String.format("backup machine id must be between %d - %d", MIN_BACKUP_MACHINE_ID, MAX_BACKUP_MACHINE_ID));
        }
        this.MACHINE_ID = machineId;
        this.BACKUP_MACHINE_ID = backupMachineId;
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.info("The snowflake ID generator with machine id {} is ready , it's backup machine id is {} .", MACHINE_ID, BACKUP_MACHINE_ID);
        }
    }

    /**
     * 生成64位的雪花Id
     *
     * @return ID
     */
    @Override
    protected synchronized long nextId()
    {
        long curTimestamp = currentMillisTimestamp();
        //如果时钟回拨
        if (curTimestamp < lastTimestamp)
        {
            LOGGER.warn("system clock moved backwards, last: {}, now: {} .", lastTimestamp, curTimestamp);
            //启用备用机器生成ID
            return nextBackId(curTimestamp);
        }
        //如果上次生成的时间戳与当前生成的时间戳相同，则自增毫秒内的序列
        else if (curTimestamp == lastTimestamp)
        {
            //如果当前毫秒内序列用尽,就阻塞到下一毫秒
            //(++curMillSequence) & MAX_INCR_SEQUENCE_BIT) : (16383 + 1) & 16383 = 0 证明当前毫秒序列用完了
            if (0L == (curMillSequence = ((++curMillSequence) & MAX_INCR_SEQUENCE_BIT)))
            {
                lastTimestamp = curTimestamp = untilNextMillis(lastTimestamp);
            }
        }
        //时间戳正常就更新 lastTimestamp 并重置当前毫秒内的自增序列
        else
        {
            lastTimestamp = curTimestamp;
            curMillSequence = 0L;
        }
        //(当前时间戳 - 开始时间戳) 左移 (机器ID位数+自增序列号位数)     |    (机器Id) 左移 (自增序列号位数)    |      自增序列号就不用左移了，因为本身就是最低位段
        return ((curTimestamp - START_EPOCH) << TIMESTAMP_SHIFT) | (MACHINE_ID << MACHINE_ID_SHIFT) | curMillSequence;
    }

    /***
     * 使用备用机器生成ID
     * @param curTimestamp  当前时间戳
     * @return 备用机器生成的ID
     */
    private long nextBackId(long curTimestamp)
    {
        //当前时间与备用机器的上次获取ID的时间比较
        if (curTimestamp < backupMachineLastTimestamp)
        {
            //如果当前回拨时间超过1s,直接抛出异常
            if (backupMachineLastTimestamp - curTimestamp > MAX_BACKWARD_TIME)
            {
                throw new IllegalStateException(String.format("system clock moved backwards more than %d ms , last: %d , now: %d .", MAX_BACKWARD_TIME, backupMachineLastTimestamp, curTimestamp));
            }
            else
            {
                backupMachineLastTimestamp = curTimestamp = (backupMachineLastTimestamp + (backupMachineLastTimestamp - curTimestamp));
                backupMachineCurMillSequence = 0L;
            }
        }
        //如果相同时间戳就自增毫秒序列
        else if (curTimestamp == backupMachineLastTimestamp)
        {
            //如果备份机器的当前自增序列用尽，就阻塞到下一毫秒
            if (0L == (backupMachineCurMillSequence = ((++backupMachineCurMillSequence) & MAX_INCR_SEQUENCE_BIT)))
            {
                backupMachineLastTimestamp = curTimestamp = untilNextMillis(backupMachineLastTimestamp);
            }
        }
        //如果正常，则更新备用机器的 lastTimestamp 并重置当前毫秒内的自增序列
        else
        {
            backupMachineLastTimestamp = curTimestamp;
            backupMachineCurMillSequence = 0L;
        }
        return ((curTimestamp - START_EPOCH) << TIMESTAMP_SHIFT) | (BACKUP_MACHINE_ID << MACHINE_ID_SHIFT) | backupMachineCurMillSequence;
    }

}
