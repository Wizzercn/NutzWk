package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysMenuService;
import cn.wizzer.app.sys.modules.services.SysRoleService;
import cn.wizzer.app.sys.modules.services.SysUnitService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.ShiroUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
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
@At("/platform/sys/role")
public class SysRoleController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysUserService sysUserService;
    @Inject
    @Reference
    private SysMenuService sysMenuService;
    @Inject
    @Reference
    private SysUnitService sysUnitService;
    @Inject
    @Reference
    private SysRoleService sysRoleService;
    @Inject
    private ShiroUtil shiroUtil;

    @At("")
    @Ok("beetl:/platform/sys/role/index.html")
    @RequiresPermissions("sys.manager.role")
    public void index() {

    }

    @At
    @Ok("beetl:/platform/sys/role/add.html")
    @RequiresPermissions("sys.manager.role")
    public Object add(@Param("unitid") String unitid, HttpServletRequest req) {
        List<Sys_menu> list = new ArrayList<>();
        if (shiroUtil.hasRole("sysadmin")) {
            list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
        } else {
            Sql sql = Sqls.create("SELECT roleId FROM sys_user_role WHERE userId=@userId");
            sql.setParam("userId", StringUtil.getPlatformUid());
            sql.setCallback(Sqls.callback.strs());
            sysMenuService.execute(sql);
            List<String> ids = sql.getList(String.class);
            Sql sql1 = Sqls.create("SELECT a.* FROM sys_menu a,sys_role_menu b WHERE a.id=b.menuId AND b.roleId in (@ids)");
            sql1.setParam("ids", ids.toArray());
            sql1.setEntity(sysMenuService.getEntity(Sys_menu.class));
            sql1.setCallback(Sqls.callback.entities());
            list = sysMenuService.execute(sql1).getList(Sys_menu.class);
        }
        List<Sys_menu> datas = sysRoleService.getDatas();
        List<NutMap> menus = new ArrayList<>();
        for (Sys_menu menu : list) {
            NutMap map = new NutMap();
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            map.put("id", menu.getId());
            map.put("text", menu.getName());
            map.put("icon", Strings.sBlank(menu.getIcon()));
            map.put("parent", "".equals(Strings.sNull(menu.getParentId())) ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            menus.add(map);
        }
        req.setAttribute("menus", Json.toJson(menus));
        return Strings.isBlank(unitid) ? null : sysMenuService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.add")
    @SLog(tag = "添加角色", msg = "角色名称:${args[1].name}")
    public Object addDo(@Param("menuIds") String menuIds, @Param("..") Sys_role role, HttpServletRequest req) {
        try {
            int num = sysRoleService.count(Cnd.where("code", "=", role.getCode().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            String[] ids = StringUtils.split(menuIds, ",");
            if ("root".equals(role.getUnitid()))
                role.setUnitid("");
            Sys_role r = sysRoleService.insert(role);
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", r.getId()).add("menuId", s));
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/menu/?")
    @Ok("beetl:/platform/sys/role/menu.html")
    @RequiresPermissions("sys.manager.role")
    public Object menu(String id, HttpServletRequest req) {
        Sys_role role = sysRoleService.fetch(id);
        List<Sys_menu> menus = sysRoleService.getMenusAndButtons(id);
        List<Sys_menu> datas = sysRoleService.getDatas(role.getId());
        List<Sys_menu> firstMenus = new ArrayList<>();
        List<Sys_menu> secondMenus = new ArrayList<>();
        for (Sys_menu menu : menus) {
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            } else {
                secondMenus.add(menu);
            }
        }
        req.setAttribute("userFirstMenus", firstMenus);
        req.setAttribute("userSecondMenus", secondMenus);
        req.setAttribute("jsonSecondMenus", Json.toJson(secondMenus));
        return role;
    }

    @At("/editMenu/?")
    @Ok("beetl:/platform/sys/role/editMenu.html")
    @RequiresPermissions("sys.manager.role")
    public Object editMenu(String roleId, HttpServletRequest req) {
        StringBuilder roleMenuIds = new StringBuilder();
        List<Sys_menu> list = new ArrayList<>();
        if (shiroUtil.hasRole("sysadmin")) {
            list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
        } else {
            Sql sql = Sqls.create("SELECT roleId FROM sys_user_role WHERE userId=@userId");
            sql.setParam("userId", StringUtil.getPlatformUid());
            sql.setCallback(Sqls.callback.strs());
            sysMenuService.execute(sql);
            List<String> ids = sql.getList(String.class);
            Sql sql1 = Sqls.create("SELECT a.* FROM sys_menu a,sys_role_menu b WHERE a.id=b.menuId AND b.roleId in (@ids)");
            sql1.setParam("ids", ids.toArray());
            sql1.setEntity(sysMenuService.getEntity(Sys_menu.class));
            sql1.setCallback(Sqls.callback.entities());
            list = sysMenuService.execute(sql1).getList(Sys_menu.class);
        }
        List<Sys_menu> datas = sysRoleService.getDatas();
        List<Sys_menu> roleMenu = sysRoleService.getMenusAndButtons(roleId);
        for (Sys_menu m : roleMenu) {
            roleMenuIds.append(m.getId() + "#");
        }
        String roleMenuId = roleMenuIds.toString();
        log.debug(roleMenuId);
        List<NutMap> menus = new ArrayList<>();
        for (Sys_menu menu : list) {
            NutMap map = new NutMap();
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            map.put("id", menu.getId());
            map.put("text", menu.getName());
            map.put("icon", Strings.sBlank(menu.getIcon()));
            map.put("parent", "".equals(Strings.sNull(menu.getParentId())) ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            if ((menu.getPath().length() >= 16 || !menu.isHasChildren()) && roleMenuId.contains(menu.getId() + "#")) {
                map.put("state", NutMap.NEW().addv("selected", true));
            } else {
                map.put("state", NutMap.NEW().addv("selected", false));
            }
            menus.add(map);
        }
        req.setAttribute("menus", Json.toJson(menus));
        return Strings.isBlank(roleId) ? null : sysRoleService.fetch(roleId);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.menu")
    @SLog(tag = "修改角色菜单", msg = "角色名称:${args[2].getAttribute('name')}")
    public Object editMenuDo(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            sysRoleService.clear("sys_role_menu", Cnd.where("roleid", "=", roleid));
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", roleid).add("menuId", s));
                }
            }
            Sys_role role = sysRoleService.fetch(roleid);
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/editUser/?")
    @Ok("beetl:/platform/sys/role/editUser.html")
    @RequiresPermissions("sys.manager.role")
    public Object editUser(String roleId, HttpServletRequest req) {
        return sysRoleService.fetch(roleId);
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object userData(@Param("roleid") String roleid, @Param("loginname") String loginname, @Param("username") String username, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a,sys_user_role b WHERE a.id=b.userId ";
        if (!Strings.isBlank(roleid)) {
            sql += " and b.roleId='" + roleid + "'";
        }
        if (!Strings.isBlank(loginname)) {
            sql += " and a.loginname like '%" + loginname + "%'";
        }
        if (!Strings.isBlank(username)) {
            sql += " and a.username like '%" + username + "%'";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return sysRoleService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @At
    @Ok("beetl:/platform/sys/role/selectUser.html")
    @RequiresPermissions("sys.manager.role")
    public void selectUser(HttpServletRequest req) {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object selectData(@Param("roleid") String roleid, @Param("name") String name, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a WHERE 1=1 ";
        if (!Strings.isBlank(roleid)) {
            sql += " and a.id NOT IN(SELECT b.userId FROM sys_user_role b WHERE b.roleId='" + roleid + "')";
        }
        if (!Strings.isBlank(name)) {
            sql += " and (a.loginname like '%" + name + "%' or a.username like '%" + name + "%') ";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return sysRoleService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    @SLog(tag = "从角色中删除用户", msg = "角色名称:${args[2].getAttribute('name')},用户ID:${args[0]}")
    public Object delUser(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            sysRoleService.clear("sys_user_role", Cnd.where("userId", "in", ids).and("roleId", "=", roleid));
            Sys_role role = sysRoleService.fetch(roleid);
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    @SLog(tag = "添加用户到角色", msg = "角色名称:${args[2].getAttribute('name')},用户ID:${args[0]}")
    public Object pushUser(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    sysRoleService.insert("sys_user_role", org.nutz.dao.Chain.make("roleId", roleid).add("userId", s));
                }
            }
            Sys_role role = sysRoleService.fetch(roleid);
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object data(@Param("unitid") String unitid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        return sysRoleService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role")
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = new ArrayList<>();
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (shiroUtil.hasRole("sysadmin")) {
            Cnd cnd = Cnd.NEW();
            if (Strings.isBlank(pid)) {
                cnd.and("parentId", "=", "").or("parentId", "is", null);
            } else {
                cnd.and("parentId", "=", pid);
            }
            cnd.asc("path");
            list = sysUnitService.query(cnd);
            if (Strings.isBlank(pid)) {
                obj.put("id", "root");
                obj.put("text", "系统角色");
                obj.put("children", false);
                tree.add(obj);
            }
        } else {
            Sys_user user = (Sys_user) shiroUtil.getPrincipal();
            if (user != null && Strings.isBlank(pid)) {
                list = sysUnitService.query(Cnd.where("id", "=", user.getUnitid()).asc("path"));
            } else {
                Cnd cnd = Cnd.NEW();
                if (Strings.isBlank(pid)) {
                    cnd.and("parentId", "=", "").or("parentId", "is", null);
                } else {
                    cnd.and("parentId", "=", pid);
                }
                cnd.asc("path");
                list = sysUnitService.query(cnd);
            }
        }
        for (Sys_unit unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/role/edit.html")
    @RequiresPermissions("sys.manager.role")
    public Object edit(String roleId, HttpServletRequest req) {
        Sys_role role = sysRoleService.fetch(roleId);
        req.setAttribute("unit", sysUnitService.fetch(role.getUnitid()));
        return role;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.edit")
    @SLog(tag = "修改角色", msg = "角色名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_role role, @Param("oldCode") String oldCode, HttpServletRequest req) {
        try {
            if (!Strings.sBlank(oldCode).equals(role.getCode())) {
                int num = sysRoleService.count(Cnd.where("code", "=", role.getCode().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            if ("root".equals(role.getUnitid()))
                role.setUnitid("");
            role.setOpBy(StringUtil.getPlatformUid());
            role.setOpAt(Times.getTS());
            sysRoleService.updateIgnoreNull(role);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role.delete")
    @SLog(tag = "删除角色", msg = "角色名称:${args[1].getAttribute('name')}")
    public Object delete(String roleId, HttpServletRequest req) {
        try {
            Sys_role role = sysRoleService.fetch(roleId);
            if ("sysadmin".equals(role.getCode()) || "public".equals(role.getCode())) {
                return Result.error("system.not.allow");
            }
            sysRoleService.del(roleId);
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("sys.manager.role.delete")
    @SLog(tag = "批量删除角色", msg = "角色ID：${args[1].getAttribute('ids')}")
    public Object deletes(@Param("roleIds") String[] roleIds, HttpServletRequest req) {
        try {
            Sys_role role = sysRoleService.fetch(Cnd.where("code", "=", "sysadmin"));
            Sys_role role1 = sysRoleService.fetch(Cnd.where("code", "=", "public"));
            StringBuilder sb = new StringBuilder();
            for (String s : roleIds) {
                if (s.equals(role.getId()) || s.equals(role1.getId())) {
                    return Result.error("system.not.allow");
                }
                sb.append(s).append(",");
            }
            sysRoleService.del(roleIds);
            req.setAttribute("ids", sb.toString());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role.edit")
    @SLog(tag = "启用角色", msg = "角色名称:${args[1].getAttribute('name')}")
    public Object enable(String roleId, HttpServletRequest req) {
        try {
            Sys_role role = sysRoleService.fetch(roleId);
            sysRoleService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", roleId));
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role.edit")
    @SLog(tag = "禁用角色", msg = "角色名称:${args[1].getAttribute('name')}")
    public Object disable(String roleId, HttpServletRequest req) {
        try {
            Sys_role role = sysRoleService.fetch(roleId);
            sysRoleService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", roleId));
            req.setAttribute("name", role.getName());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
