package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.budwk.app.base.result.Result;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysMenuService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.base.Globals;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/home")
public class SysHomeController {
    private static final Log log = Logs.get();
    @Inject
    private SysMenuService sysMenuService;
    @Inject
    private SysUserService sysUserService;

    @At("")
    @Ok("re")
    @SaCheckLogin
    public String home(HttpSession session, HttpServletRequest req) {
        if (StpUtil.hasRole("sysadmin")) {
            return "beetl:/platform/sys/dashboard.html";
        }
        return "beetl:/platform/sys/home.html";

    }

    @At("/needInfo")
    @Ok("json")
    public Object needInfo() {
        Sys_user user = sysUserService.fetch(SecurityUtil.getUserId());
        if (Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getMobile()) || Strings.isBlank(user.getEmail())) {
            return Result.success().addData(true);
        } else {
            return Result.success().addData(false);
        }
    }

    @At
    @Ok("beetl:/platform/sys/left.html")
    @SaCheckLogin
    public void left(@Param("url") String url, HttpServletRequest req) {
        String path = "";
        String perpath = "";
        url = Strings.sNull(url).trim();
        if (!Strings.isBlank(Globals.AppBase)) {
            url = Strings.sBlank(url).substring(Globals.AppBase.length());
        }
        if (Strings.sBlank(url).indexOf("?") > 0)
            url = url.substring(0, url.indexOf("?"));
        Sys_menu menu = sysMenuService.getLeftMenu(url);
        if (menu != null) {
            if (menu.getPath().length() >= 8) {
                path = menu.getPath().substring(0, 8);
                perpath = menu.getPath().substring(0, 4);
            }
            req.setAttribute("mpath", menu.getPath());
        }
        req.setAttribute("path", path);
        req.setAttribute("perpath", perpath);
    }

    @At
    @Ok("beetl:/platform/sys/left.html")
    @SaCheckLogin
    public void path(@Param("url") String url, HttpServletRequest req) {
        url = Strings.sNull(url).trim();
        if (Strings.sBlank(url).indexOf("//") > 0) {
            String[] u = url.split("//");
            String s = u[1].substring(u[1].indexOf("/"));
            if (Strings.sBlank(s).indexOf("?") > 0)
                s = s.substring(0, s.indexOf("?"));
            if (!Strings.isBlank(Globals.AppBase)) {
                s = s.substring(Globals.AppBase.length());
            }
            String[] urls = s.split("/");
            List<String> list = new ArrayList<>();
            if (urls.length > 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4] + "/" + urls[5]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 5) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3] + "/" + urls[4]);
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 4) {
                list.add("/" + urls[1] + "/" + urls[2] + "/" + urls[3]);
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 3) {
                list.add("/" + urls[1] + "/" + urls[2]);
                list.add("/" + urls[1]);
            } else if (urls.length == 2) {
                list.add("/" + urls[1]);
            } else list.add(url);
            String path = "";
            String perpath = "";
            Sys_menu menu = sysMenuService.getLeftPathMenu(list);
            if (menu != null) {
                if (menu.getPath().length() >= 8) {
                    path = menu.getPath().substring(0, 8);
                    perpath = menu.getPath().substring(0, 4);
                }
                req.setAttribute("mpath", menu.getPath());
            }
            req.setAttribute("path", path);
            req.setAttribute("perpath", perpath);
        }
    }

    @At("/403")
    @Ok("re")
    public Object error403(HttpServletRequest req) {
        StpUtil.checkLogin();
        if (Strings.sNull(req.getAttribute("original_request_uri")).startsWith("/platform")) {
            return "beetl:/platform/sys/403.html";
        } else {
            return ">>:/error/404.html";
        }
    }

    @At("/404")
    @Ok("re")
    public Object error404(HttpServletRequest req) {
        StpUtil.checkLogin();
        if (Strings.sNull(req.getAttribute("original_request_uri")).startsWith("/platform")) {
            return "beetl:/platform/sys/404.html";
        } else {
            return ">>:/error/404.html";
        }
    }

    @At("/500")
    @Ok("re")
    public Object error500(HttpServletRequest req) {
        StpUtil.checkLogin();
        if (Strings.sNull(req.getAttribute("original_request_uri")).startsWith("/platform")) {
            return "beetl:/platform/sys/500.html";
        } else {
            return ">>:/error/500.html";
        }
    }

    @At(value = {"/", "/index"}, top = true)
    @Ok(">>:/sysadmin")
    public void index() {
    }
}
