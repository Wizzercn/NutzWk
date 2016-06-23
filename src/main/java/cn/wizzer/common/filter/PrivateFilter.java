package cn.wizzer.common.filter;

import cn.wizzer.common.base.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.UTF8JsonView;

/**
 * Created by wizzer on 2016/6/23.
 */
public class PrivateFilter implements ActionFilter {
    private static final Log log = Logs.get();
    public View match(ActionContext context) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            //判断AJAX请求
            if (!"XMLHttpRequest".equalsIgnoreCase(context.getRequest().getHeader("x-requested-with"))) {
                return new ServerRedirectView("/private/login");
            } else {
                context.getResponse().setHeader("loginStatus", "accessDenied");
                return new UTF8JsonView().setData(Result.error("登录失效", context.getRequest()));
            }
        }
        return null;
    }
}
