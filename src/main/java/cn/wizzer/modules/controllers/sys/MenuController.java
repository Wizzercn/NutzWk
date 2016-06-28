package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.add")
    @SLog(tag = "新建单位", msg = "单位名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_menu menu, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            int num = menuService.count(Cnd.where("permission", "=", menu.getPermission().trim()));
            if (num > 0) {
                return Result.error("sys.role.code", req);
            }
            if("data".equals(menu.getType())){
                menu.setIsShow(false);
            }else menu.setIsShow(true);
            menuService.save(menu, parentId);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_menu> list = menuService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).and("type", "=", "menu").asc("location").asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_menu menu : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", menu.getId());
            obj.put("text", menu.getName());
            obj.put("children", menu.isHasChildren());
            tree.add(obj);
        }
        return tree;
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
