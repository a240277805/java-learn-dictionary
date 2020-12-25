package com.zmk.github.utils;

/**
 * @Author zmk
 * @Date: 2020/12/21/ 10:00
 * @Description MD5加密工具类
 */

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class MyMD5Util {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    public static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str +slat;
        String md5 = DigestUtils.md5Hex(base.getBytes());
        return md5;
    }


    public static void main(String[] args) {
        String result = getMD5("张孟康666");
        String result1 = encrypt("张孟康666");
        System.out.println(result);
        System.out.println(result1);
    }

}
