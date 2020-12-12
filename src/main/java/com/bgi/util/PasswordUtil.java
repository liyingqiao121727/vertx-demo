package com.bgi.util;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
/**
 * MD5加盐加密
 */
public class PasswordUtil {

    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code.append('0');
        }
        return md5code.toString();
    }

    public static String randomNumber() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /**
     * 生成含有随机盐的密码
     */
    public static String generate(String password, String salt) {
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }
    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String salt, String md5) {
        char[] cs1 = new char[32];
        //char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            //cs2[i / 3] = md5.charAt(i + 1);
        }
        //String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }
    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new String(encodeHex(bs)).getBytes());
        } catch (Exception e) {
            return null;
        }
    }

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for(int var4 = 0; i < l; ++i) {
            out[var4++] = DIGITS[(240 & data[i]) >>> 4];
            out[var4++] = DIGITS[15 & data[i]];
        }

        return out;
    }

    public static void main(String[] args) {
        // 加密+加盐
        String admin1 = md5("admin");
        String randomNumber1 = randomNumber();
        String password1 = generate(admin1, "3888345339289675");
        System.out.println("admin1:" + admin1 + "随机数" + randomNumber1 + "结果：" + password1 + "   长度："+ password1.length());
        // 解码
        String admin2 = md5("admin");
        System.out.println(verify(admin2, "3888345339289675", password1));
        // 加密+加盐
        String admin3 = md5("admin");
        String randomNumber2 = randomNumber();
        String password2= generate(admin3, "1454183115633457");
        System.out.println("admin2:" + admin2 + "随机数" + randomNumber2 + "结果：" + password2 + "   长度："+ password2.length());
        // 解码
        String admin4 = md5("admin");
        System.out.println(verify(admin4, "1454183115633457", password2));
    }
}