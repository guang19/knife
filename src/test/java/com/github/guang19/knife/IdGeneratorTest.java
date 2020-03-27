package com.github.guang19.knife;

import com.github.guang19.knife.idgenerator.IdGenerator;
import com.github.guang19.knife.idgenerator.impl.SnowFlakeIdGenerator54;
import com.github.guang19.knife.idgenerator.impl.SnowFlakeIdGenerator64;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yangguang
 * @date 2020/3/20
 * @description <p></p>
 */
public class IdGeneratorTest
{
    @Test
    public void test01() throws Exception
    {
        IdGenerator idGenerator1 = new SnowFlakeIdGenerator54(0,8);
        IdGenerator idGenerator2 = new SnowFlakeIdGenerator54(1,9);

        for (int i = 0 ; i < 100 ; ++i)
        {
            System.out.println("idGenerator1 " + idGenerator1.generateId());
            System.out.println("idGenerator2 " + idGenerator2.generateId());
        }


    }

    @Test
    public void test02() throws Exception
    {
        ConcurrentHashMap<Long,Long> map = new ConcurrentHashMap<>();
        IdGenerator idGenerator = new SnowFlakeIdGenerator54(0,8);
        long begin = System.currentTimeMillis();

        for (int j = 0 ; j < 100000; ++j)
        {
            long id = idGenerator.generateId();
            map.put(id,id);
        }

        System.out.println("current time : " + (System.currentTimeMillis() - begin));
        System.out.println(map.size());
    }


    @Test
    public void test03() throws Exception
    {
        /**
         * 经测试,如果 54 位的雪花ID生成器，如果START_EPOCH少于现在约4年左右，那么生成的ID就会溢出 2^53 - 1
         * 如果从现在开始，那么过4年，生成的ID才会溢出 2^53 - 1
         */
        SnowFlakeIdGenerator54 idGenerator54 = new SnowFlakeIdGenerator54(0,8);
        System.out.println((1L << 53) - 1);
        System.out.println(idGenerator54.generateId());

        System.out.println(" ---- ");


        /**
         * 经测试,如果 64 位的雪花ID生成器，如果START_EPOCH少于现在约34年左右，那么生成的ID就会溢出 2^63 - 1
         * 如果从现在开始，那么过34年，生成的ID才会溢出 2^63 - 1
         */
        SnowFlakeIdGenerator64 idGenerator64 = new SnowFlakeIdGenerator64(0,512);
        System.out.println((1L << 63) - 1);
        System.out.println(idGenerator64.generateId());

        //当然，时间戳的范围是可以改的，只不过我所优化过的就是以上这么大的范围了
    }

    @Test
    public void test04() throws Exception
    {
        SnowFlakeIdGenerator64 idGenerator54 = new SnowFlakeIdGenerator64(0,512);
        Map<String ,Long> map = new ConcurrentHashMap<>();
        for (int i = 0 ; i < 50; ++i)
        {
            new Thread(()->
            {
                for (int j = 0 ; j < 10000; ++j)
                {
                    long id = idGenerator54.generateId();
                    map.put(String.valueOf(id),id);

                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(3);

        System.out.println(map.size());

        System.out.println(" -------------- ");

    }

    @Test
    public void test05() throws Exception
    {
        System.out.println((1L << 41) / (1000L * 60 * 60 * 24 * 365));
        System.out.println((1L << 37) / (1000L * 60 * 60 * 24 * 365));
        System.out.println((1L << 38) / (1000L * 60 * 60 * 24 * 365));

        long l = System.currentTimeMillis();

        TimeUnit.SECONDS.sleep(1);
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void test06() throws Exception
    {
        Process clock_gettime = Runtime.getRuntime().exec("clock_gettime");
        System.out.println(clock_gettime.exitValue());
    }
}
