package cn.wizzer.common.mvc.filter;

import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ServerRedirectView;

import java.util.Map;

/**
 * 后台拦截器
 * Created by Wizzer.cn on 2015/7/10.
 */
public class PrivateFilter implements ActionFilter {

    public View match(ActionContext context) {
        //忽略AJAX请求
        if (!"XMLHttpRequest".equalsIgnoreCase(context.getRequest().getHeader("x-requested-with"))) {
            Subject currentUser = SecurityUtils.getSubject();
            if (currentUser != null) {
                Sys_user user = (Sys_user) currentUser.getPrincipal();
                if (user != null) {
                    context.getRequest().setAttribute("app_path", getMenu(StringUtils.getPath(context.getPath()), user.getIdMenus()));
                }
            } else {
                return new ServerRedirectView("/private/login");
            }
        }
        return null;
    }

    /**
     * 得到当前路径或上级路径的栏目ID
     *
     * @param path
     * @param map
     * @return
     */
    private String getMenu(String path, Map map) {
        String p = Strings.sNull(map.get(path));
        if (Strings.isEmpty(p) && path.lastIndexOf("/") > 0) {
            return getMenu(path.substring(0, path.lastIndexOf("/")), map);
        } else if (!Strings.isEmpty(p)) {
            return path;
        }
        return "";
    }
}
