package cn.wizzer.common.mvc.filter;

import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

/**
 * 系统后台拦截器
 * Created by Wizzer.cn on 2015/7/6.
 */
public class PrivateFilter implements ActionFilter {
    private final Log log= Logs.get();

    public View match(ActionContext context) {
        //获取当前登陆用户的皮肤样式
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.getPrincipals() != null) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (user != null) {
                context.getRequest().setAttribute("app_theme", user.getLoginTheme());
                context.getRequest().setAttribute("firstMenus", user.getFirstMenus());
                context.getRequest().setAttribute("secondMenus", user.getSecondMenus());

            }
        }
        return null;
    }

}
