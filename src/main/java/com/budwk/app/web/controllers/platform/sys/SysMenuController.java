package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.budwk.app.base.result.Result;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.services.SysMenuService;
import com.budwk.app.sys.services.SysRoleService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/sys/menu")
public class SysMenuController {
    private static final Log log = Logs.get();
    @Inject
    private SysMenuService sysMenuService;
    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysRoleService sysRoleService;

    @At("")
    @Ok("beetl:/platform/sys/menu/index.html")
    @SaCheckPermission("sys.manager.menu")
    public void index(HttpServletRequest req) {
    }

    @At("/child")
    @Ok("json")
    @SaCheckLogin
    public Object child(@Param("pid") String pid, HttpServletRequest req) {
        List<Sys_menu> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.asc("location").asc("path");
        list = sysMenuService.query(cnd);
        for (Sys_menu menu : list) {
            if (sysMenuService.count(Cnd.where("parentId", "=", menu.getId())) > 0) {
                menu.setHasChildren(true);
            }
            NutMap map = Lang.obj2nutmap(menu);
            map.addv("expanded", false);
            map.addv("children", new ArrayList<>());
            treeList.add(map);
        }
        return Result.success().addData(treeList);
    }

    @At("/tree")
    @Ok("json")
    @SaCheckLogin
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        try {
            List<NutMap> treeList = new ArrayList<>();
            if (Strings.isBlank(pid)) {
                NutMap root = NutMap.NEW().addv("value", "root").addv("label", "不选择菜单").addv("leaf", true);
                treeList.add(root);
            }
            Cnd cnd = Cnd.NEW();
            if (Strings.isBlank(pid)) {
                cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
            } else {
                cnd.and("parentId", "=", pid);
            }
            cnd.and("type", "=", "menu");
            cnd.asc("location").asc("path");
            List<Sys_menu> list = sysMenuService.query(cnd);
            for (Sys_menu menu : list) {
                NutMap map = NutMap.NEW().addv("value", menu.getId()).addv("label", menu.getName());
                if (menu.isHasChildren()) {
                    map.addv("children", new ArrayList<>());
                    map.addv("leaf", false);
                } else {
                    map.addv("leaf", true);
                }
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.add")
    @SLog(tag = "新建菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object addDo(@Param("..") NutMap nutMap, HttpServletRequest req) {
        try {
            Sys_menu sysMenu = nutMap.getAs("menu", Sys_menu.class);
            List<NutMap> buttons = Json.fromJsonAsList(NutMap.class, nutMap.getString("buttons"));
            int num = sysMenuService.count(Cnd.where("permission", "=", sysMenu.getPermission().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            for (NutMap map : buttons) {
                num = sysMenuService.count(Cnd.where("permission", "=", map.getString("permission", "").trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            String parentId = sysMenu.getParentId();
            if ("root".equals(sysMenu.getParentId())) {
                parentId = "";
            }
            sysMenu.setHasChildren(false);
            sysMenu.setShowit(true);
            sysMenu.setCreatedBy(SecurityUtil.getUserId());
            sysMenuService.save(sysMenu, Strings.sNull(parentId), buttons);
            req.setAttribute("name", sysMenu.getName());
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    //编辑菜单,组装一下js表单数据
    @At("/editMenu/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu")
    public Object editMenu(String id, HttpServletRequest req) {
        try {
            Sys_menu menu = sysMenuService.fetch(id);
            NutMap map = Lang.obj2nutmap(menu);
            map.put("parentName", "无");
            map.put("children", "false");
            if (Strings.isNotBlank(menu.getParentId())) {
                map.put("parentName", sysMenuService.fetch(menu.getParentId()).getName());
            }
            List<Sys_menu> list = sysMenuService.query(Cnd.where("parentId", "=", id).and("type", "=", "data").asc("location").asc("path"));
            List<NutMap> buttons = new ArrayList<>();
            if (list != null && list.size() > 0) {
                map.put("children", "true");
                for (Sys_menu m : list) {
                    buttons.add(NutMap.NEW().addv("key", m.getId()).addv("name", m.getName()).addv("permission", m.getPermission()));
                }
            }
            map.put("buttons", buttons);
            return Result.success().addData(map);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.edit")
    @SLog(tag = "修改菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object editMenuDo(@Param("..") NutMap nutMap, HttpServletRequest req) {
        try {
            Sys_menu sysMenu = nutMap.getAs("menu", Sys_menu.class);
            List<NutMap> buttons = Json.fromJsonAsList(NutMap.class, nutMap.getString("buttons"));
            //如果权限标识不是自己的,并且被其他记录占用
            int num = sysMenuService.count(Cnd.where("permission", "=", sysMenu.getPermission().trim()).and("id", "<>", sysMenu.getId()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            for (NutMap map : buttons) {
                num = sysMenuService.count(Cnd.where("permission", "=", map.getString("permission", "").trim()).and("id", "<>", map.getString("key", "")));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            sysMenu.setHasChildren(false);
            sysMenu.setShowit(true);
            sysMenu.setUpdatedBy(SecurityUtil.getUserId());
            sysMenuService.edit(sysMenu, sysMenu.getParentId(), buttons);
            req.setAttribute("name", sysMenu.getName());
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/editData/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu")
    public Object editData(String id, HttpServletRequest req) {
        try {
            return Result.success().addData(sysMenuService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.edit")
    @SLog(tag = "修改权限", msg = "权限名称:${args[0].name}")
    public Object editDataDo(@Param("..") Sys_menu menu, HttpServletRequest req) {
        try {
            int num = sysMenuService.count(Cnd.where("permission", "=", menu.getPermission().trim()).and("id", "<>", menu.getId()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            sysMenuService.updateIgnoreNull(menu);
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.delete")
    @SLog(tag = "删除菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_menu menu = sysMenuService.fetch(id);
            req.setAttribute("name", menu.getName());
            if (menu.getPath().startsWith("0001")) {
                return Result.error("system.not.allow");
            }
            sysMenuService.deleteAndChild(menu);
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.edit")
    @SLog(tag = "启用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object enable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", sysMenuService.fetch(menuId).getName());
            sysMenuService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", menuId));
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.edit")
    @SLog(tag = "禁用菜单", msg = "菜单名称:${args[1].getAttribute('name')}")
    public Object disable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", sysMenuService.fetch(menuId).getName());
            sysMenuService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", menuId));
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/menuAll")
    @Ok("json")
    @SaCheckPermission("sys.manager.menu")
    public Object menuAll(HttpServletRequest req) {
        try {
            List<Sys_menu> list = sysMenuService.query(Cnd.where("type", "=", "menu").asc("location").asc("path"));
            NutMap menuMap = NutMap.NEW();
            for (Sys_menu unit : list) {
                List<Sys_menu> list1 = menuMap.getList(unit.getParentId(), Sys_menu.class);
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
                list1.add(unit);
                menuMap.put(unit.getParentId(), list1);
            }
            return Result.success().addData(getTree(menuMap, ""));
        } catch (Exception e) {
            return Result.error();
        }
    }

    private List<NutMap> getTree(NutMap menuMap, String pid) {
        List<NutMap> treeList = new ArrayList<>();
        List<Sys_menu> subList = menuMap.getList(pid, Sys_menu.class);
        for (Sys_menu menu : subList) {
            NutMap map = Lang.obj2nutmap(menu);
            map.put("label", menu.getName());
            if (menu.isHasChildren() || (menuMap.get(menu.getId()) != null)) {
                map.put("children", getTree(menuMap, menu.getId()));
            }
            treeList.add(map);
        }
        return treeList;
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.menu.edit")
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
            sysMenuService.clearCache();
            sysUserService.clearCache();
            sysRoleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
