package cn.wizzer.modules.controllers.platform.sys;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.util.StringUtil;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.services.sys.SysMenuService;
import cn.wizzer.modules.services.sys.SysUnitService;
import cn.wizzer.modules.services.sys.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.*;
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
@At("/platform/sys/menu")
@Filters({@By(type = PrivateFilter.class)})
public class SysMenuController {
    private static final Log log = Logs.get();
    @Inject
    SysUserService userService;
    @Inject
    SysMenuService menuService;
    @Inject
    SysUnitService unitService;

    @At("")
    @Ok("beetl:/platform/sys/menu/index.html")
    @RequiresAuthentication
    public void index(HttpServletRequest req) {
        req.setAttribute("list", menuService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
    }

    @At
    @Ok("beetl:/platform/sys/menu/add.html")
    @RequiresAuthentication
    public Object add(@Param("pid") String pid, HttpServletRequest req) {
        return Strings.isBlank(pid) ? null : menuService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.add")
    @SLog(tag = "新建菜单", msg = "菜单名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_menu menu, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            int num = menuService.count(Cnd.where("permission", "=", menu.getPermission().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            if ("data".equals(menu.getType())) {
                menu.setIsShow(false);
            } else menu.setIsShow(true);
            menuService.save(menu, parentId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/menu/edit.html")
    @RequiresAuthentication
    public Object edit(String id, HttpServletRequest req) {
        Sys_menu menu = menuService.fetch(id);
        if (menu != null) {
            req.setAttribute("parentMenu", menuService.fetch(menu.getParentId()));
        }
        return menu;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(tag = "编辑菜单", msg = "菜单名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_menu menu, @Param("oldPermission") String oldPermission, HttpServletRequest req) {
        try {
            if (!Strings.sBlank(oldPermission).equals(menu.getPermission())) {
                int num = menuService.count(Cnd.where("permission", "=", menu.getPermission().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            menu.setOpBy(Strings.sNull(req.getAttribute("uid")));
            menu.setOpAt((int) (System.currentTimeMillis() / 1000));
            menuService.updateIgnoreNull(menu);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.delete")
    @SLog(tag = "删除菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_menu menu = menuService.fetch(id);
            req.setAttribute("name", menu.getName());
            if (menu.getPath().startsWith("0001")) {
                return Result.error("system.not.allow");
            }
            menuService.deleteAndChild(menu);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(tag = "启用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object enable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", menuService.fetch(menuId).getName());
            menuService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", menuId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.edit")
    @SLog(tag = "禁用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object disable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", menuService.fetch(menuId).getName());
            menuService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", menuId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
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
    @Ok("beetl:/platform/sys/menu/child.html")
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

    @At
    @Ok("beetl:/platform/sys/menu/sort.html")
    @RequiresAuthentication
    public void sort(HttpServletRequest req) {
        List<Sys_menu> list = menuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_menu> firstMenus = new ArrayList<>();
        Map<String, List<Sys_menu>> secondMenus = new HashMap<>();
        for (Sys_menu menu : list) {
            if (menu.getPath().length() > 4) {
                List<Sys_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.edit")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds= StringUtils.split(ids,",");
            int i=0;
            menuService.dao().execute(Sqls.create("update sys_menu set location=0"));
            for(String s:menuIds){
                if(!Strings.isBlank(s)){
                    menuService.update(org.nutz.dao.Chain.make("location",i),Cnd.where("id","=",s));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
