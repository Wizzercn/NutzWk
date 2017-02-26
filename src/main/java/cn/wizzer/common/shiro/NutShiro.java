package cn.wizzer.common.shiro;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Encoding;
import org.nutz.mvc.view.UTF8JsonView;
import org.nutz.resource.Scans;

/**
 * Nutz与Shiro集成所需要的一些辅助方法
 * @author wendal<wendal1985@gmail.com>
 *
 */
public class NutShiro {

    public static String DefaultLoginURL = "/user/login";
    public static String DefaultNoAuthURL; // 默认与DefaultLoginURL一致
    
    public static String SessionKey = "me";
    
    public static String AjaxEncode = Encoding.UTF8;
    
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	
	public static boolean isAjax(ServletRequest req) {
	    String value = ((HttpServletRequest)req).getHeader("X-Requested-With");
        if (value != null && "XMLHttpRequest".equalsIgnoreCase(value.trim())) {
            return true;
        }
		return false;
	}
	
	public static void rendAjaxResp(ServletRequest req, ServletResponse resp, Object re) {
		try {
			if (AjaxEncode != null)
				((HttpServletResponse)resp).setCharacterEncoding(AjaxEncode);
			new UTF8JsonView(JsonFormat.compact()).render((HttpServletRequest)req, (HttpServletResponse)resp, re);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static boolean match(Method method) {
        if (method.getAnnotation(RequiresRoles.class) != null 
                || method.getAnnotation(RequiresAuthentication.class) != null
                || method.getAnnotation(RequiresGuest.class) != null
                || method.getAnnotation(RequiresPermissions.class) != null
                || method.getAnnotation(RequiresUser.class) != null) {
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	public static Set<String>[] scanRolePermissionInPackage(String pkg, boolean publicOnly) {
    	Set<String> roles = new HashSet<String>();
    	Set<String> permissions = new HashSet<String>();
    	for (Class<?> klass : Scans.me().scanPackage(pkg)) {
			Method[] methods = publicOnly ? klass.getMethods() : klass.getDeclaredMethods();
			for (Method method : methods) {
				RequiresRoles rr = method.getAnnotation(RequiresRoles.class);
				if (rr != null && rr.value().length > 0) {
					for (String role : rr.value()) {
						roles.add(role);
					}
				}
                RequiresPermissions pr = method.getAnnotation(RequiresPermissions.class);
                if (pr != null && pr.value().length > 0) {
                    for (String permission : pr.value()) {
                        permissions.add(permission);
                    }
                }
			}
		}
    	return new Set[]{roles, permissions};
    }
}
