package cn.wizzer.common.util;

import org.nutz.lang.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
public class StringUtils {
    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request){
        String remoteAddr = request.getHeader("X-Real-IP");
        if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (Strings.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 去掉URL中?后的路径
     * @param p
     * @return
     */
    public static String getPath(String p){
        if(Strings.sNull(p).contains("?")){
            return p.substring(0,p.indexOf("?"));
        }
        return Strings.sNull(p);
    }
}
