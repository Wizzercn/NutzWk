package cn.xuetang.common.util;

import org.apache.commons.lang.StringUtils;
import org.nutz.lang.Strings;
import java.util.List;

/**
 * Created by Wizzer on 14-3-25.
 */
public class StringUtil {
    /**
     * 将字符串转为Hex
     * @param byteArray
     * @return
     */
    public String bytesToHex(byte[] byteArray) {
        StringBuffer StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return StrBuff.toString();
    }
    /**
     * 将字符串在指定的长度内显示，超出后以..代替
     *
     * @param str  in string
     * @param iLen specify length out string
     */
    public static String substr(String str, int iLen) {
        if (str == null)
            return "";
        if (iLen > 2) {
            if (str.length() > iLen - 2) {
                str = str.substring(0, iLen - 2) + "..";
            }

        }
        return str;
    }

    /**
     * Mysql 单引号处理
     *
     * @param strField
     * @param def
     * @return
     */
    static public String getMysqlSaveString(String strField, String def) {
        if (strField == null)
            return (def == null) ? "\'\'" : def;
        if (strField.indexOf('\'') != -1)
            return "'" + replaceCharacterWithString('\'', "\'\'", strField) + "'";
        return strField;
    }

    static public String replaceCharacterWithString(char character, String replacement, String source) {
        StringBuffer myStringBuffer = new StringBuffer(source);
        int length = myStringBuffer.length();
        int replacementLen = replacement.length();

        for (int indexOf = 0; indexOf < length; indexOf++) {
            if (myStringBuffer.charAt(indexOf) == character) {
                myStringBuffer.replace(indexOf, indexOf + 1, replacement);
                length = myStringBuffer.length();
                indexOf += replacementLen - 1;
            }
        }
        return myStringBuffer.toString();
    }

    /**
     * 获取字符串长度
     * @param str
     * @return
     */
    public static int getStrLength(String str) {
        if (Strings.isBlank(str))
            return 0;
        else
            return str.length();
    }

    /**
     * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
     *
     * @param names
     * @return String
     */
    public static String getStrsplit(List<String> names) {
        if (names == null || names.size() == 0)
            return "('')";
        String result = "(";
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1)
                result = result + "'" + names.get(i) + "'";
            else
                result = result + "'" + names.get(i) + "',";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将整型数组转换为（1，2）的格式后返回，来方便数据库的操作
     *
     * @param ids
     * @return String
     */
    public static String getIdsplit(Integer[] ids) {
        if (ids == null || ids.length == 0)
            return "()";
        String result = "(";
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1)
                result = result + ids[i];
            else
                result = result + ids[i] + ",";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将整型数组转换为（1，2）的格式后返回，来方便数据库的操作
     *
     * @param ids
     * @return String
     */
    public static String getIdsplit(String[] ids) {
        if (ids == null || ids.length == 0)
            return "('')";
        String result = "(";
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1)
                result = result + ids[i];
            else
                result = result + ids[i] + ",";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将向量转换为（1，2）的格式后返回，来方便数据库的操作
     *
     * @param ids
     * @return String
     */
    public static String getIdsplit(List<Integer> ids) {
        if (ids == null || ids.size() == 0)
            return "()";
        String result = "(";
        for (int i = 0; i < ids.size(); i++) {
            if (i == ids.size() - 1)
                result = result + ids.get(i);
            else
                result = result + ids.get(i) + ",";
        }
        result = result + ")";
        return result;
    }

    /**
     * 将字符串转为数组
     * @param str
     * @param e
     * @return
     */
    public static String[] getStrings(String str,String e){
        if (str == null || str.length() == 0)
            return null;
        return StringUtils.split(str,e);
    }
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    public static void main(String[] agrs) {
        try {
//            ComboIocLoader loader = new ComboIocLoader(
//                    new String[]{
//                            "*org.nutz.ioc.loader.json.JsonLoader", "config/"}
//            );
//            NutIoc ioc = new NutIoc(loader);
//            Dao dao = ioc.get(Dao.class);
            int pageCount = (int) Math.ceil((double) 0 / 15);
            System.out.println(pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
