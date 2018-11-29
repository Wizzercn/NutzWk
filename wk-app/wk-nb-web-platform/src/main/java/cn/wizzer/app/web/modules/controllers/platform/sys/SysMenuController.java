package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.services.SysMenuService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

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
public class SysMenuController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysMenuService sysMenuService;

    @At("")
    @Ok("beetl:/platform/sys/menu/index.html")
    @RequiresPermissions("sys.manager.menu")
    public void index(HttpServletRequest req) {
        req.setAttribute("list", sysMenuService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));
    }

    @At
    @Ok("beetl:/platform/sys/menu/add.html")
    @RequiresPermissions("sys.manager.menu")
    public Object add(@Param("pid") String pid, HttpServletRequest req) {
        return Strings.isBlank(pid) ? null : sysMenuService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu.add")
    @SLog(tag = "新建菜单", msg = "菜单名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_menu menu, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            int num = sysMenuService.count(Cnd.where("permission", "=", menu.getPermission().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            if ("data".equals(menu.getType())) {
                menu.setShowit(false);
            } else menu.setShowit(true);
            menu.setOpBy(StringUtil.getPlatformUid());
            sysMenuService.save(menu, parentId,null);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/menu/edit.html")
    @RequiresPermissions("sys.manager.menu")
    public Object edit(String id, HttpServletRequest req) {
        Sys_menu menu = sysMenuService.fetch(id);
        if (menu != null) {
            req.setAttribute("parentMenu", sysMenuService.fetch(menu.getParentId()));
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
                int num = sysMenuService.count(Cnd.where("permission", "=", menu.getPermission().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            menu.setOpBy(StringUtil.getPlatformUid());
            menu.setOpAt(Times.getTS());
            sysMenuService.updateIgnoreNull(menu);
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
            Sys_menu menu = sysMenuService.fetch(id);
            req.setAttribute("name", menu.getName());
            if (menu.getPath().startsWith("0001")) {
                return Result.error("system.not.allow");
            }
            sysMenuService.deleteAndChild(menu);
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
            req.setAttribute("name", sysMenuService.fetch(menuId).getName());
            sysMenuService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", menuId));
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
            req.setAttribute("name", sysMenuService.fetch(menuId).getName());
            sysMenuService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", menuId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.menu")
    public Object tree(@Param("pid") String pid) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.and("type", "=", "menu");
        cnd.asc("location").asc("path");
        List<Sys_menu> list = sysMenuService.query(cnd);
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
    @RequiresPermissions("sys.manager.menu")
    public Object child(String id) {
        Sys_menu m = sysMenuService.fetch(id);
        List<Sys_menu> list = new ArrayList<>();
        List<Sys_menu> menus = sysMenuService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
        List<Sys_menu> datas = sysMenuService.query(Cnd.where("path", "like", Strings.sBlank(m.getPath()) + "________").and("type", "=", "data").asc("location").asc("path"));
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
    @RequiresPermissions("sys.manager.menu")
    public void sort(HttpServletRequest req) {
        List<Sys_menu> list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
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
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            sysMenuService.execute(Sqls.create("update sys_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    sysMenuService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
