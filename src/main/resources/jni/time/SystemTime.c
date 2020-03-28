/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include "SystemTime.h"


/*
 * 以clock_gettime函数实现获取当前时间戳
 *
 * Class:     com_github_guang19_knife_timeutils_SystemTime
 * Method:    currentTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_github_guang19_knife_timeutils_SystemTime_currentTimeMillis(JNIEnv *env, jclass jclazz)
{
    _timespec timespec;
     clock_gettime(CLOCK_REALTIME,&timespec);
     //因为clock_gettime函数精确到纳秒，所以需要 除以1000000
     return (timespec.tv_sec * 1000L) + (timespec.tv_nsec / 1000000L);
}