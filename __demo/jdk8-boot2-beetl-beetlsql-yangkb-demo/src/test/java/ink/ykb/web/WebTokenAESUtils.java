package ink.ykb.web;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 网页生成对称加密解密AES算法
 * <br>
 *去官方下载JCE无限制权限策略文件。
 *jdk 5: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#jce_policy-1.5.0-oth-JPR
 *jdk6: http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
 *JDK7的下载地址: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
 *JDK8的下载地址: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html 
 *下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt
 *如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件
 *如果安装了JDK，还要将两个jar文件也放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件。
 */
public class WebTokenAESUtils {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "utf-8";
    /**
     * 建议为16位或32位
     */
    public static final String KEY = "A-16-Byte-WaertrA-16-Byte-Waertr";
    /**
     * 必须16位
     * 初始化向量IV不可以为32位，否则异常java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 16 bytes long
     */
    public static final String IV = "A-16-Byte-Waertr";

    /**
     * 加密
     *
     * @param context
     * @return
     */
    public static String encrypt(String context) {
        try {
            byte[] decode = context.getBytes(CHARSET);
            byte[] bytes = createKeyAndIv(decode, Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param context
     * @return
     */
    public static String decrypt(String context) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(context);
            byte[] bytes = createKeyAndIv(decode, Cipher.DECRYPT_MODE);
            return new String(bytes, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取key & iv
     *
     * @param context
     * @param opmode
     * @return
     * @throws Exception
     */
    public static byte[] createKeyAndIv(byte[] context, int opmode) throws Exception {
        byte[] key = KEY.getBytes(CHARSET);
        byte[] iv = IV.getBytes(CHARSET);
        return cipherFilter(context, opmode, key, iv);
    }

    /**
     * 执行操作
     *
     * @param context
     * @param opmode
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] cipherFilter(byte[] context, int opmode, byte[] key, byte[] iv) throws Exception {
        Key secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(opmode, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(context);
    }
    
}

