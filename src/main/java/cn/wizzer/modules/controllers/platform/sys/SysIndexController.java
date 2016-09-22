package cn.wizzer.modules.controllers.platform.sys;

import cn.wizzer.common.base.Globals;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by wizzer on 2016/9/22.
 */
@IocBean
@Filters
public class SysIndexController {
    private static final Log log = Logs.get();

    @At(value = {"/", "/index"}, top = true)
    @Ok(">>:/sysadmin")
    public void index() {
    }

    @At(value = {"/platform/home"}, top = true)
    @Ok("re")
    public Object home() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "beetl:/platform/index.html";
        } else {
            return "redirect:/platform/login";
        }
    }

    @At(value = {"/error/403"}, top = true)
    @Ok("re")
    public Object error403(HttpServletRequest req, HttpSession session) {
        String url = Strings.sNull(req.getAttribute("javax.servlet.error.request_uri"));
        if (url.contains(Globals.AppBase + "/platform")) {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                return "beetl:/platform/index.html";
            } else {
                return "redirect:/platform/login";
            }
        }
        return "->:/WEB-INF/error/403.html";
    }

    @At(value = {"/error/404"}, top = true)
    @Ok("re")
    public Object error404(HttpServletRequest req, HttpSession session) {
        String url = Strings.sNull(req.getAttribute("javax.servlet.error.request_uri"));
        if (url.contains(Globals.AppBase + "/platform")) {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                return "beetl:/platform/index.html";
            } else {
                return "redirect:/platform/login";
            }
        }
        return "->:/WEB-INF/error/404.html";
    }

}
