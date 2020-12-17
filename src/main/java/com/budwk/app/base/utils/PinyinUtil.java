package com.budwk.app.base.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2017/5/24.
 */
@IocBean
public class PinyinUtil {
    /**
     * 将汉字转换为全拼
     */
    public static String getPingYin(String name) {
        char[] charArray = name.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                pinyin.append(PinyinHelper.toHanyuPinyinStringArray(charArray[i])[0]);
            } else {
                pinyin.append(charArray[i]);
            }
        }
        return pinyin.toString();
    }

    /**
     * 返回中文的首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(getPingYin("綦江qq县"));
//        System.out.println(getPinYinHeadChar("綦江县"));
//        System.out.println(getCnASCII("綦江县"));
//    }
}
