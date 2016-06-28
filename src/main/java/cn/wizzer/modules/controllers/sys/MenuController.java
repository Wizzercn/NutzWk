package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
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
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/sys/menu")
@Filters({@By(type = PrivateFilter.class)})
public class MenuController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    MenuService menuService;
    @Inject
    UnitService unitService;

    @At("")
    @Ok("beetl:/private/sys/menu/index.html")
    @RequiresAuthentication
    public void index(HttpServletRequest req) {
        req.setAttribute("list", menuService.query(Cnd.where("parentId", "=", "").asc("location").asc("path")));
    }

    @At
    @Ok("beetl:/private/sys/menu/add.html")
    @RequiresAuthentication
    public Object add(@Param("pid") String pid, HttpServletRequest req) {
        return Strings.isBlank(pid) ? null : menuService.fetch(pid);
    }

    @At("/child/?")
    @Ok("beetl:/private/sys/menu/child.html")
    @RequiresAuthentication
    public Object child(String id) {
        Sys_menu m = menuService.fetch(id);
        List<Sys_menu> list = new ArrayList<>();
        List<Sys_menu> menus = menuService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
        List<Sys_menu> datas = menuService.query(Cnd.where("path", "like", Strings.sBlank(m.getPath()) + "________").and("type", "=", "data").asc("location").asc("path"));
        for (Sys_menu menu : menus) {
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            list.add(menu);
        }
        return list;
    }

}
