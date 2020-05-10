package com.github.guang19.knife;

import com.github.guang19.knife.timeutils.TimeUtils;
import org.junit.Test;


/**
 * @author yangguang
 * @date 2020/3/26
 * @description <p>
 * clock_gettime和jdk自带的currentTimeMillis方法(gettimeofday函数)做一个比拼
 * </p>
 */
public class TimeUtilsTest
{
    @Test
    public void test01() throws Exception
    {
        long t = TimeUtils.currentTimeMillis();
        System.out.println(t);
    }

}
