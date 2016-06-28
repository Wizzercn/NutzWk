package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_role;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.RoleService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
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
@At("/private/sys/role")
@Filters({@By(type = PrivateFilter.class)})
public class RoleController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    MenuService menuService;
    @Inject
    UnitService unitService;
    @Inject
    RoleService roleService;

    @At("")
    @Ok("beetl:/private/sys/role/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/sys/role/add.html")
    @RequiresAuthentication
    public Object add(@Param("unitid") String unitid, HttpServletRequest req) {
        List<Sys_menu> list = menuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_menu> datas = roleService.getDatas();
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
            map.put("parent", menu.getParentId().equals("") ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            menus.add(map);
        }
        req.setAttribute("menus", Json.toJson(menus));
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.add")
    public Object addDo(@Param("menuIds") String menuIds, @Param("..") Sys_role role, HttpServletRequest req) {
        try {
            int num = roleService.count(Cnd.where("code", "=", role.getCode().trim()));
            if (num > 0) {
                return Result.error("sys.role.code", req);
            }
            String[] ids = StringUtils.split(menuIds, ",");
            if("root".equals(role.getUnitid()))
                role.setUnitid("");
            Sys_role r = roleService.insert(role);
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    roleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", r.getId()).add("menuId", s));
                }
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/menu/?")
    @Ok("beetl:/private/sys/role/menu.html")
    @RequiresAuthentication
    public Object menu(String id, HttpServletRequest req) {
        Sys_role role = roleService.fetch(id);
        List<Sys_menu> menus = roleService.getMenusAndButtons(id);
        List<Sys_menu> datas = roleService.getDatas(role.getId());
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
    @Ok("beetl:/private/sys/role/editMenu.html")
    @RequiresAuthentication
    public Object editMenu(String roleId, HttpServletRequest req) {
        List<Sys_menu> list = menuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_menu> datas = roleService.getDatas();
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
            map.put("parent", menu.getParentId().equals("") ? "#" : menu.getParentId());
            map.put("data", menu.getHref());
            menus.add(map);
        }
        req.setAttribute("menus", Json.toJson(menus));
        return Strings.isBlank(roleId) ? null : roleService.fetch(roleId);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.menu")
    public Object editMenuDo(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            roleService.dao().clear("sys_role_menu", Cnd.where("roleid", "=", roleid));
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    roleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", roleid).add("menuId", s));
                }
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/editUser/?")
    @Ok("beetl:/private/sys/role/editUser.html")
    @RequiresAuthentication
    public Object editUser(String roleId, HttpServletRequest req) {
        return roleService.fetch(roleId);
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object userData(@Param("roleid") String roleid, @Param("loginname") String loginname, @Param("nickname") String nickname, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a,sys_user_role b WHERE a.id=b.userId ";
        if (!Strings.isBlank(roleid)) {
            sql += " and b.roleId='" + roleid + "'";
        }
        if (!Strings.isBlank(loginname)) {
            sql += " and a.loginname like '%" + loginname + "%'";
        }
        if (!Strings.isBlank(nickname)) {
            sql += " and a.nickname like '%" + nickname + "%'";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return roleService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @At
    @Ok("beetl:/private/sys/role/selectUser.html")
    @RequiresAuthentication
    public void selectUser(HttpServletRequest req) {

    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object selectData(@Param("roleid") String roleid, @Param("name") String name, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.* FROM sys_user a WHERE 1=1 ";
        if (!Strings.isBlank(roleid)) {
            sql += " and a.id NOT IN(SELECT b.userId FROM sys_user_role b WHERE b.roleId='" + roleid + "')";
        }
        if (!Strings.isBlank(name)) {
            sql += " and (a.loginname like '%" + name + "%' or a.nickname like '%" + name + "%') ";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return roleService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    public Object delUser(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            roleService.dao().clear("sys_user_role", Cnd.where("userId", "in", ids).and("roleId", "=", roleid));
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    public Object pushUser(@Param("menuIds") String menuIds, @Param("roleid") String roleid, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    roleService.insert("sys_user_role", org.nutz.dao.Chain.make("roleId", roleid).add("userId", s));
                }
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("unitid") String unitid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        return roleService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "root");
        obj.put("text", "系统角色");
        obj.put("children", false);
        tree.add(obj);
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
    @Ok("beetl:/private/sys/role/edit.html")
    @RequiresAuthentication
    public Object edit(String roleId, HttpServletRequest req) {
        Sys_role role = roleService.fetch(roleId);
        req.setAttribute("unit", unitService.fetch(role.getUnitid()));
        return role;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.edit")
    public Object editDo(@Param("..") Sys_role role, @Param("oldCode") String oldCode, HttpServletRequest req) {
        try {
            if(!Strings.sBlank(oldCode).equals(role.getCode())){
                int num = roleService.count(Cnd.where("code", "=", role.getCode().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code", req);
                }
            }
            if("root".equals(role.getUnitid()))
                role.setUnitid("");
            role.setUpdateAt((int)(System.currentTimeMillis()/1000));
            roleService.updateIgnoreNull(role);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }
}
