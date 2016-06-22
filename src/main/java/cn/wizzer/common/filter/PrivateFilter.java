package cn.wizzer.common.filter;

import cn.wizzer.common.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;

/**
 * 后台拦截器
 * Created by Wizzer.cn on 2015/7/10.
 */
public class PrivateFilter implements ActionFilter {
    public View match(ActionContext context) {
        //忽略AJAX请求
        if (!"XMLHttpRequest".equalsIgnoreCase(context.getRequest().getHeader("x-requested-with"))) {
//            Subject currentUser = SecurityUtils.getSubject();
//            if (currentUser != null) {
//                Sys_user user = (Sys_user) currentUser.getPrincipal();
//                if (user != null) {
//                    context.getRequest().setAttribute("app_path", getMenu(StringUtil.getPath(context.getPath())));
//                }
//            } else {
//                return new ServerRedirectView("/private/login");
//            }
        }
        return null;
    }
}
