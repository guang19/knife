package com.github.guang19.knife.crypto.asymmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author guang19
 * @date 2020/8/19
 * @description 非对称加密算法填充方案
 */
@Setter
@Getter
@AllArgsConstructor
public class RSAPaddingAlgorithm
{
    //算法名
    private final String value;
    //KeySize
    private final int keySize;
    //加密分段大小
    private final int inputBlockSize;
    //解密分段大小
    private final int outputBlockSize;

    //PKCS1Padding填充
    public static RSAPaddingAlgorithm RSA_PKCS1_PADDING_768 = new RSAPaddingAlgorithm("RSA/ECB/PKCS1Padding",768,85,96);
    public static RSAPaddingAlgorithm RSA_PKCS1_PADDING_1024 = new RSAPaddingAlgorithm("RSA/ECB/PKCS1Padding",1024,117,128);
    public static RSAPaddingAlgorithm RSA_PKCS1_PADDING_2048 = new RSAPaddingAlgorithm("RSA/ECB/PKCS1Padding",2048,245,256);
    //OAEPPadding填充
    public static RSAPaddingAlgorithm RSA_PKCS1_OAEP_PADDING_768 = new RSAPaddingAlgorithm("RSA/ECB/OAEPPadding",768,54,96);
    public static RSAPaddingAlgorithm RSA_PKCS1_OAEP_PADDING_1024 = new RSAPaddingAlgorithm("RSA/ECB/OAEPPadding",1024,86,128);
    public static RSAPaddingAlgorithm RSA_PKCS1_OAEP_PADDING_2048 = new RSAPaddingAlgorithm("RSA/ECB/OAEPPadding",2048,214,256);
    //不填充，KeySize默认为1024
    public static RSAPaddingAlgorithm RSA_NO_PADDING = new RSAPaddingAlgorithm("RSA/ECB/NoPadding",1024,0,0);
}
