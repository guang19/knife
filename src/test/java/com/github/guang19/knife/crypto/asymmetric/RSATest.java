package com.github.guang19.knife.crypto.asymmetric;

import com.github.guang19.knife.crypto.asymmetric.RSA;
import com.github.guang19.knife.crypto.asymmetric.RSA.RSAKeyPair;
import com.github.guang19.knife.crypto.asymmetric.RSAPaddingAlgorithm;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.UUID;

/**
 * @author guang19
 * @date 2020/8/19
 * @description
 */
public class RSATest
{
    private KeyPair keyPair;

    @Before
    public void before() throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(1024,new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        String publicKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()), StandardCharsets.UTF_8);

        System.out.println("publicKey : " + publicKey);
        System.out.println("public exponent : " + publicKey.getPublicExponent());
        System.out.println("public module : " + publicKey.getModulus());
        System.out.println("public format : " + publicKey.getFormat());
        System.out.println("publicKey : " + publicKeyStr);
        System.out.println("publicKey : " + publicKeyStr.length());

        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        String privateKeyStr = new String(Base64.getEncoder().encode(privateKey.getEncoded()),StandardCharsets.UTF_8);
        System.out.println("privateKey : " + privateKey);
        System.out.println("private exponent : " + privateKey.getPrivateExponent());
        System.out.println("private module : " + privateKey.getModulus());
        System.out.println("private format : " + privateKey.getFormat());
        System.out.println("privateKey : " + privateKeyStr);
        System.out.println("privateKey : " + privateKeyStr.length());
        this.keyPair = keyPair;
    }

    @Test
    public void test01() throws Exception
    {
        Cipher enCipher =  Cipher.getInstance(RSAPaddingAlgorithm.RSA_PKCS1_PADDING_1024.getValue());
        enCipher.init(Cipher.ENCRYPT_MODE,keyPair.getPublic());
        byte[] bytes = enCipher.doFinal("guang19".getBytes(StandardCharsets.UTF_8));
        String enStr = new String(Base64.getEncoder().encode(bytes),StandardCharsets.UTF_8);
        System.out.println("enStr : " + enStr);

        Cipher deCipher = Cipher.getInstance(RSAPaddingAlgorithm.RSA_PKCS1_PADDING_1024.getValue());
        deCipher.init(Cipher.DECRYPT_MODE,keyPair.getPrivate());
        byte[] bytes2 = Base64.getDecoder().decode(enStr.getBytes(StandardCharsets.UTF_8));
        String deStr = new String(deCipher.doFinal(bytes2),StandardCharsets.UTF_8);
        System.out.println("deStr : " + deStr);
    }

    @Test
    public void test02() throws Exception
    {
        String str = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        System.out.println(str);
        System.out.println(str.length());

        RSA rsa = RSA.keySize1024();
        RSAKeyPair rsaKeyPair = rsa.generateKeyPair();
        String encrypt = rsa.encrypt(str, rsaKeyPair.getPublicKey());
        System.out.println(encrypt);
        System.out.println(encrypt.length());
        String decrypt = rsa.decrypt(encrypt, rsaKeyPair.getPrivateKey());
        System.out.println(decrypt.length());

        System.out.println(str.equals(decrypt));
    }

    @Test
    public void test03() throws Exception
    {
        RSA rsa = RSA.keySize1024();
        RSAKeyPair rsaKeyPair = rsa.generateKeyPair();
        final String encrypt = rsa.encrypt("guang19", rsaKeyPair.getPublicKey());
        System.out.println("encrypt : " + encrypt);

        final String sign = rsa.sign(encrypt, rsaKeyPair.getPrivateKey());
        System.out.println("sign : " + sign);
        System.out.println("check sign " + rsa.checkSign(encrypt,sign,rsaKeyPair.getPublicKey()));

        String decrypt = rsa.decrypt(encrypt, rsaKeyPair.getPrivateKey());
        System.out.println("decrypt : " + decrypt);
    }
}
