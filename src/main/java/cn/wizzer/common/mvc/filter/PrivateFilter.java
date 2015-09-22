package cn.wizzer.common.mvc.filter;

import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.RawView;
import org.nutz.mvc.view.ServerRedirectView;

import java.util.Map;

/**
 * 后台拦截器
 * Created by Wizzer.cn on 2015/7/10.
 */
public class PrivateFilter implements ActionFilter {
    private MenuService menuService = Mvcs.ctx().getDefaultIoc().get(MenuService.class);
    public View match(ActionContext context) {
        //忽略AJAX请求
        if (!"XMLHttpRequest".equalsIgnoreCase(context.getRequest().getHeader("x-requested-with"))) {
            Subject currentUser = SecurityUtils.getSubject();
            if (currentUser != null) {
                Sys_user user = (Sys_user) currentUser.getPrincipal();
                if (user != null) {
                    context.getRequest().setAttribute("app_path", getMenu(StringUtils.getPath(context.getPath())));
                }
            } else {
                return new ServerRedirectView("/private/login");
            }
        }
        return null;
    }

    /**
     * 得到当前路径或上级路径的菜单path
     *
     * @param path
     * @return
     */
    private String getMenu(String path) {
        String thisPath=path;
        String[] p=Strings.splitIgnoreBlank(path, "/");
        if(p.length>3){
            thisPath="/"+p[0]+"/"+p[1]+"/"+p[2];
        }
        return menuService.getPath(thisPath);
    }
}
