package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.services.sys.MenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/private/home")
@Filters({@By(type = PrivateFilter.class)})
public class HomeController {
    private static final Log log = Logs.get();
    @Inject
    MenuService menuService;

    @At("")
    @Ok("beetl:/private/home.html")
    @RequiresAuthentication
    public void home() {
        //SecurityUtils.getSubject().getSession().setTimeout(1000);
    }

    @At
    @Ok("beetl:/private/left.html")
    @RequiresAuthentication
    public void left(@Param("url") String url, HttpServletRequest req) {
        String path = "";
        String perpath = "";
        if(Strings.sBlank(url).indexOf("?")>0)
            url=url.substring(0,url.indexOf("?"));
        Sys_menu menu = menuService.fetch(Cnd.where("href", "=", url));
        if (menu != null) {
            if (menu.getPath().length() > 8) {
                path = menu.getPath().substring(0, 8);
                perpath = menu.getPath().substring(0, 4);
            } else if (menu.getPath().length() == 8) {
                perpath = menu.getPath().substring(0, 4);
            }
            req.setAttribute("mpath", menu.getPath());
        }
        req.setAttribute("path", path);
        req.setAttribute("perpath", perpath);
    }

    @At
    @Ok("beetl:/private/left.html")
    @RequiresAuthentication
    public void path(@Param("url") String url, HttpServletRequest req) {
        if (Strings.sBlank(url).indexOf("//") > 0) {
            if(Strings.sBlank(url).indexOf("?")>0)
                url=url.substring(0,url.indexOf("?"));
            String[] u = url.split("//");
            String[] urls = u[1].split("/");
            List<String> list = new ArrayList<>();
            if (urls.length >= 5) {
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
            Sys_menu menu = menuService.fetch(Cnd.where("href", "in", list).desc("href").desc("path"));
            if (menu != null) {
                if (menu.getPath().length() > 8) {
                    path = menu.getPath().substring(0, 8);
                    perpath = menu.getPath().substring(0, 4);
                } else if (menu.getPath().length() == 8) {
                    perpath = menu.getPath().substring(0, 4);
                }
                req.setAttribute("mpath", menu.getPath());
            }
            req.setAttribute("path", path);
            req.setAttribute("perpath", perpath);
        }
    }
}
