package cn.wizzer.common.util;

import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.shiro.SecurityUtils;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
public class StringUtils {
    /**
     * 获取当前登陆用户ID
     *
     * @return
     */
    public static String getUid() {
        String uid = "";
        try {
            Object u = SecurityUtils.getSubject().getPrincipal();
            if (u != null) {
                if (u instanceof Sys_user) {
                    uid = ((Sys_user) u).getId();
                } else if (u instanceof String) {
                    uid = ((String) u);
                }
            }
        } catch (Exception e) {
            return "";
        }
        return uid;
    }

    /**
     * 获取当前登陆用户Username
     *
     * @return
     */
    public static String getUsername() {
        String uid = "";
        try {
            Object u = SecurityUtils.getSubject().getPrincipal();
            if (u != null) {
                if (u instanceof Sys_user) {
                    uid = ((Sys_user) u).getUsername();
                } else if (u instanceof String) {
                    uid = ((String) u);
                }
            }
        } catch (Exception e) {

            return "";
        }
        return uid;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr() {
        HttpServletRequest request = Mvcs.getReq();
        String remoteAddr = request.getHeader("X-Real-IP");
        if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
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
     * 得到六位随机数
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
}
