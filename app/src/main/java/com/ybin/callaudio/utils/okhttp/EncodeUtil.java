package com.ybin.callaudio.utils.okhttp;

import android.text.TextUtils;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gaoyanbin on 2018/4/20.
 * 描述:
 */

public class EncodeUtil {
    //    private static final String RSA = "RSA";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";


    //公钥加密
    public static String RSAEncode(RSAPublicKey key, String str) {
        try {

            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] temp = cipher.doFinal(str.getBytes());
//            LogUtils.e("RSAEncode", "temp = " + new String(temp));
            //不换行的BASE64混淆(仅在此处使用该模式)
            String content = Base64.encodeToString(temp, Base64.NO_WRAP);
//            LogUtils.e("RSAEncode", "content = " + new String(content));
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return new String();
        }

    }


//    public static RSAPublicKey getRSAPublicKey(String des, String rsa) {
//        try {
//            Key serverDESKey = decodeTheKey(des);
//            Cipher cipher = Cipher.getInstance(DES);
//            cipher.init(Cipher.DECRYPT_MODE, serverDESKey);
//            byte[] temp = cipher.doFinal(base64DecodeToBytes(rsa));
//
//            byte[] RSAPublicKey = Base64.decode(temp, Base64.DEFAULT);
//
////            String str = new String(temp);
////            LogUtils.e("RSAKEY==>", str);
//
//            byte[] buffer = Base64.decode(new String(temp), Base64.DEFAULT);
//
//            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
//            X509EncodedKeySpec keySpec2 = new X509EncodedKeySpec(buffer);
//
//            return (RSAPublicKey) keyFactory2.generatePublic(keySpec2);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static RSAPublicKey getRSAPublicKey(String rsa) {
        try {
            byte[] buffer = base64DecodeToBytes(rsa);

            String str = new String(buffer);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

//            LogUtils.e("getRSAPublicKey", "服务器收到的publickKey(String格式)=" + rsa);
//
//            LogUtils.e("getRSAPublicKey", "Base64解密=" + str);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);


//            LogUtils.e("getRSAPublicKey", "RSAPublicKey=" + new String(key.getEncoded()));

            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将字符串用Base64解混淆
     *
     * @param str 字符串
     * @return
     */
    public static byte[] base64DecodeToBytes(String str) {
        return str == null ? null : Base64.decode(str, Base64.DEFAULT);
    }

    /**
     * 将字节数组用Base64混淆
     *
     * @param bytes 节数组
     * @return
     */
    public static String base64EncodeToString(byte[] bytes) {
        return bytes == null ? null : Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 生成密钥
     *
     * @return
     * @throws Exception
     */
    public static String initKey() throws Exception {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(AES);
        //初始化此密钥生成器，使其具有确定的密钥大小
        //AES 要求密钥长度为 128
        kg.init(128);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();

        return Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP);
    }

    private static SecretKey getKey(String key) {
        return new SecretKeySpec(Base64.decode(key, Base64.NO_WRAP), AES);
    }

    /*
     * AES加密
     */
    public static String encrypt(String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return content;
        }

        try {

            SecretKey secretKey = getKey(key);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] b = cipher.doFinal(content.getBytes());
            return Base64.encodeToString(b, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            SecretKey secretKey = getKey(key);
//            byte[] enc = base64DecodeToBytes(encrypted);
            byte[] temp = encrypted.getBytes("UTF-8");
            byte[] enc = Base64.decode(new String(temp,"UTF-8"), Base64.NO_WRAP);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(enc);
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

//    /**
//     * 解密
//     *
//     * @param data
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static byte[] decrypt(byte[] data, String key) throws Exception {
//        Key k = toKey(decryptBASE64(key));
//
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, k);
//
//        return cipher.doFinal(data);
//    }

}

