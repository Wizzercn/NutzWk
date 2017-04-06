package cn.wizzer.framework.util;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.view.UTF8JsonView;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
public class StringUtil {

    private static final Pattern IPV4_PATTERN =
            Pattern.compile(
                    "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile(
                    "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile(
                    "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    /**
     * 从seesion获取uid
     *
     * @return
     */
    public static String getUid() {
        try {
            HttpServletRequest request = Mvcs.getReq();
            if (request != null) {
                return Strings.sNull(request.getSession(true).getAttribute("uid"));
            }
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 从seesion获取用户名
     *
     * @return
     */
    public static String getUsername() {
        try {
            HttpServletRequest request = Mvcs.getReq();
            if (request != null) {
                return Strings.sNull(request.getSession(true).getAttribute("username"));
            }
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 计算MD5密码
     *
     * @param loginname
     * @param password
     * @param createAt
     * @return
     */
    public static String getPassword(String loginname, String password, int createAt) {
        String p = Lang.md5(Lang.md5(password) + loginname + createAt);
        return 'w' + p.substring(0, p.length() - 1);
    }

    public static byte[] getBytesFromObject(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
        } finally {
            if (bo != null) {
                bo.close();
            }
            if (oo != null) {
                oo.close();
            }
        }
        return bytes;
    }

    public static Object getObjectFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            if (bytes == null) {
                return null;
            }
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            obj = oi.readObject();
        } finally {
            if (bi != null) {
                bi.close();
            }
            if (oi != null) {
                oi.close();
            }
        }
        return obj;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = Mvcs.getReq();
        if(request==null) return "";
        String remoteAddr = request.getHeader("X-Real-IP");
        if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        String ip = remoteAddr != null ? remoteAddr : Strings.sNull(request.getRemoteAddr());
        if (isIPv4Address(ip) || isIPv6Address(ip)) {
            return ip;
        }
        return "";
    }

    /**
     * 去掉URL中?后的路径
     *
     * @param p
     * @return
     */
    public static String getPath(String p) {
        if (Strings.sNull(p).contains("?")) {
            return p.substring(0, p.indexOf("?"));
        }
        return Strings.sNull(p);
    }

    /**
     * 获得父节点ID
     *
     * @param s
     * @return
     */
    public static String getParentId(String s) {
        if (!Strings.isEmpty(s) && s.length() > 4) {
            return s.substring(0, s.length() - 4);
        }
        return "";
    }

    /**
     * 得到n位随机数
     *
     * @param s
     * @return
     */
    public static String getRndNumber(int s) {
        Random ra = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s; i++) {
            sb.append(String.valueOf(ra.nextInt(8)));
        }
        return sb.toString();
    }

    /**
     * 判断是否以字符串开头
     *
     * @param str
     * @param s
     * @return
     */
    public boolean startWith(String str, String s) {
        return Strings.sNull(str).startsWith(Strings.sNull(s));
    }

    /**
     * 判断是否包含字符串
     *
     * @param str
     * @param s
     * @return
     */
    public boolean contains(String str, String s) {
        return Strings.sNull(str).contains(Strings.sNull(s));
    }

    /**
     * 将对象转为JSON字符串（页面上使用）
     *
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        return Json.toJson(obj, JsonFormat.compact());
    }
}
