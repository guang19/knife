package com.github.guang19.knife.crypto.asymmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author guang19
 * @date 2020/8/19
 * @description RSA加密
 */
public class RSA
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);
    //加密算法，填充方式
    private RSAPaddingAlgorithm rsaPaddingAlgorithm;

    //KeyPairGenerator
    private KeyPairGenerator keyPairGenerator;

    //KeyFactory
    private KeyFactory keyFactory;

    //签名加密算法
    private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";
    //RSA算法
    private static final String RSA_ALGORITHM = "RSA";

    /**
     * Constructor
     * @param rsaPaddingAlgorithm   RSA填充算法
     */
    public RSA(RSAPaddingAlgorithm rsaPaddingAlgorithm)
    {
        this.rsaPaddingAlgorithm = rsaPaddingAlgorithm;
        try
        {
            this.keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            this.keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            this.keyPairGenerator.initialize(rsaPaddingAlgorithm.getKeySize(),new SecureRandom());
        }
        catch (NoSuchAlgorithmException e)
        {
            LOGGER.error("Wrong RSA algorithm : {} ",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //初始化key size为1024的RSA
    public static RSA keySize768()
    {
        return new RSA(RSAPaddingAlgorithm.RSA_PKCS1_PADDING_768);
    }

    //初始化key size为1024的RSA
    public static RSA keySize1024()
    {
        return new RSA(RSAPaddingAlgorithm.RSA_PKCS1_PADDING_1024);
    }

    //初始化key size为2048的RSA
    public static RSA keySize2048()
    {
        return new RSA(RSAPaddingAlgorithm.RSA_PKCS1_PADDING_2048);
    }

    /**
     * 生成密匙对
     * @return      RSAKeyPair
     */
    public RSAKeyPair generateKeyPair()
    {
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String pubKeyStr = base64EncodeToString(publicKey.getEncoded());
        String priKeyStr = base64EncodeToString(privateKey.getEncoded());
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("RSA generated public key : {} , private key : {}",pubKeyStr,priKeyStr);
        }
        return new RSAKeyPair(pubKeyStr,priKeyStr);
    }

    /**
     * 公匙加密
     * @param source        需要加密的数据
     * @param publicKey     公匙字符串
     * @return              加密后的密文
     */
    public String encrypt(String source,String publicKey)
    {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            Cipher cipher = Cipher.getInstance(rsaPaddingAlgorithm.getValue());
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(publicKey));
            int inputBlockSize = rsaPaddingAlgorithm.getInputBlockSize();
            byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
            int sourceBytesLen = sourceBytes.length;
            byte[] cache;
            //分段加密
            for (int i = 0 , offset = 0 ; sourceBytesLen - offset > 0 ; ++i, offset = i * inputBlockSize)
            {
                if (sourceBytesLen - offset > inputBlockSize)
                {
                    cache = cipher.doFinal(sourceBytes, offset, inputBlockSize);
                }
                else
                {
                    cache = cipher.doFinal(sourceBytes,offset,sourceBytesLen - offset);
                }
                outputStream.write(cache,0,cache.length);
            }
            return base64EncodeToString(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            LOGGER.error("RSA encrypt error : ",e);
            return null;
        }
    }

    /**
     * 私匙解密
     * @param encryptData          已加密的密文
     * @param privateKey        私匙字符串
     * @return                  解密后的原数据
     */
    public String decrypt(String encryptData,String privateKey)
    {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            Cipher cipher = Cipher.getInstance(rsaPaddingAlgorithm.getValue());
            cipher.init(Cipher.DECRYPT_MODE,getPrivateKey(privateKey));
            int outputBlockSize = rsaPaddingAlgorithm.getOutputBlockSize();
            byte[] encryptBytes = base64DecodeToBytes(encryptData);
            int encryptBytesLen = encryptBytes.length;
            byte[] cache;
            //分段解密
            for (int i = 0 , offset = 0 ; encryptBytesLen - offset > 0 ; ++i, offset = i * outputBlockSize)
            {
                if (encryptBytesLen - offset > outputBlockSize)
                {
                    cache = cipher.doFinal(encryptBytes,offset,outputBlockSize);
                }
                else
                {
                    cache = cipher.doFinal(encryptBytes,offset,encryptBytesLen - offset);
                }
                outputStream.write(cache,0,cache.length);
            }
            return new String(outputStream.toByteArray(),StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            LOGGER.error("RSA decrypt error : ",e);
            return null;
        }
    }


    /**
     * 使用私匙签名
     * @param encryptData    已加密的密文
     * @param privateKey    私匙字符串
     * @return              签名
     */
    public String sign(String encryptData , String privateKey)
    {
        try
        {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(getPrivateKey(privateKey));
            signature.update(encryptData.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = signature.sign();
            return base64EncodeToString(signBytes);
        }
        catch (Exception e)
        {
            LOGGER.error("RSA sign error : ",e);
            return null;
        }
    }


    /***
     * 使用公匙检验签名
     * @param encryptData   已加密的密文
     * @param sign          签名
     * @param publicKey     公匙字符串
     * @return              sign是否成功
     */
    public boolean checkSign(String encryptData , String sign , String publicKey)
    {
        try
        {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(getPublicKey(publicKey));
            signature.update(encryptData.getBytes(StandardCharsets.UTF_8));
            return signature.verify(base64DecodeToBytes(sign));
        }
        catch (Exception e)
        {
            LOGGER.error("RSA check sign error : ",e);
            return false;
        }
    }


    /**
     * 根据公匙字符串获取其公匙
     * @param publicKeyStr              公匙字符串
     * @return                          PublicKey
     * @throws InvalidKeySpecException  InvalidKeySpecException
     */
    private PublicKey getPublicKey(String publicKeyStr) throws InvalidKeySpecException
    {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64DecodeToBytes(publicKeyStr));
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 根据私匙字符串获取私匙
     * @param privateKeyStr 私匙字符串
     * @return              PrivateKey
     */
    private PrivateKey getPrivateKey(String privateKeyStr) throws InvalidKeySpecException
    {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64DecodeToBytes(privateKeyStr));
        return keyFactory.generatePrivate(keySpec);
    }

    //将bytes通过base64编码为String
    private String base64EncodeToString(byte[] bytes)
    {
        return new String(Base64.getEncoder().encode(bytes),StandardCharsets.UTF_8);
    }

    //将String通过base64解码为byte
    private byte[] base64DecodeToBytes(String str)
    {
        return Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * RSA KeyPair
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class RSAKeyPair
    {
        //公匙Base64字符串
        private String publicKey;
        //私匙Base64字符串
        private String privateKey;
    }
}
