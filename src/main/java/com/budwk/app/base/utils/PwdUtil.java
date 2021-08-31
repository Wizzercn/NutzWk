package com.budwk.app.base.utils;

import org.nutz.lang.Lang;

import java.security.MessageDigest;

/**
 * 为老兼容老的密码加密方式
 *
 * @author wizzer@qq.com
 */
public class PwdUtil {

    public static String getPassword(String passowrd, String salt) {
        byte[] bytes = hash(passowrd.getBytes(), salt.getBytes(), 1024);
        if (bytes != null) {
            return Lang.fixedHexString(bytes);
        }
        return "";
    }

    public static byte[] hash(byte[] bytes, byte[] salt, int hashIterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            if (salt != null) {
                digest.reset();
                digest.update(salt);
            }

            byte[] hashed = digest.digest(bytes);
            int iterations = hashIterations - 1;

            for (int i = 0; i < iterations; ++i) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            return hashed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
