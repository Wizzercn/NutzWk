package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.page.Pagination;
import cn.wizzer.common.util.DateUtils;
import cn.wizzer.modules.sys.bean.Sys_menu;
import cn.wizzer.modules.sys.bean.Sys_role;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.MenuService;
import cn.wizzer.modules.sys.service.RoleService;
import cn.wizzer.modules.sys.service.UnitService;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.*;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.annotation.Chain;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer.cn on 2015/7/25.
 */
@IocBean
@At("/private/sys/role")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag = "角色管理", msg = "")
public class RoleAction {
    private Log log = Logs.get();
    @Inject
    RoleService roleService;
    @Inject
    MenuService menuService;
    @Inject
    UserService userService;
    @Inject
    UnitService unitService;

    @At("")
    @Ok("vm:template.private.sys.role.index")
    @RequiresPermissions("sys:role")
    @SLog(tag = "角色列表", msg = "访问角色列表")
    public Object index() {
        return "";
    }

    @At("/info/?")
    @Ok("vm:template.private.sys.role.info")
    @RequiresPermissions("sys:role")
    public Object info(String unitId, HttpServletRequest req) {
        Cnd cnd;
        if ("_system".equals(unitId)) {
            cnd = Cnd.where("unitid", "=", "_system");
        } else cnd = Cnd.where("unitid", "=", unitId);
        return roleService.query(cnd.asc("location"), null);
    }

    @At("/tree")
    @Ok("raw:json")
    @RequiresPermissions("sys:role")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list1 = new ArrayList<>();
        list1.add(new Record().set("id", "_system").set("text", "系统角色").set("children", false));
        List<Record> list = new ArrayList<>();
        List<Record> listAll = new ArrayList<>();
        Subject currentUser = SecurityUtils.getSubject();
        boolean isSystem = false;
        String uid = "";
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            uid = user.getId();
            if (user.isSystem()) {
                isSystem = true;
            }
        }
        if (isSystem) {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where length(path)=4 order by location asc,path asc"));
                listAll.addAll(list1);
            }
        } else {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(
                        Sqls.create("select a.id,a.name as text,a.has_children as children from sys_unit a,sys_user_unit b " +
                                " where a.id=b.unit_id and b.user_id=@uid order by a.location asc,a.path asc").setParam("uid", uid));
            }
        }
        listAll.addAll(list);
        return listAll;
    }

    @At("/menu/?")
    @Ok("raw:json")
    @RequiresPermissions("sys:role")
    public Object menu(String unitId, @Param("pid") String pid, HttpServletRequest req) {
        List<Record> list = new ArrayList<>();
        Subject currentUser = SecurityUtils.getSubject();
        boolean isSystem = false;
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            if (user.isSystem()) {
                isSystem = true;
            }
        }
        if (isSystem) {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children,icon,description as data from sys_menu  where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children,icon,description as data from sys_menu where length(path)=4 order by location asc,path asc"));
            }
        } else {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children,icon,description as data from sys_menu where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(
                        Sqls.create("select DISTINCT a.id,a.name as text,a.has_children as children,a.icon,a.description as data from sys_menu a,sys_role_menu b " +
                                " where a.id=b.menu_id and b.role_id in(select c.id from sys_role c where c.unitid=@unitid) and length(a.path)=4 order by a.location asc,a.path asc").setParam("unitid", unitId));
            }
        }
        return list;
    }

    @At("/btn")
    @Ok("vm:template.private.sys.role.btn")
    @RequiresPermissions("sys:role")
    public Object btn(@Param("ids") String id, HttpServletRequest req) {
        String[] ids = StringUtils.split(id, ",");
        List<Sys_menu> list = menuService.query(Cnd.where("parentId", "in", ids).and("type", "=", "button").asc("location").asc("path"), null);
        Map<String, List<Sys_menu>> map = getMap(list);
        req.setAttribute("buttons", map);
        return menuService.query(Cnd.where("id", "in", ids).and("type", "=", "menu").asc("location").asc("path"), null);

    }

    @At("/user")
    @Ok("vm:template.private.sys.role.user")
    @RequiresPermissions("sys:role")
    public Object user(@Param("unitid") String unitId, @Param("keyword") String keyword, @Param("type") int type, @Param("curPage") int curPage, @Param("pageSize") int pageSize, HttpServletRequest req) {
        Sql sql;
        if ("_system".equals(unitId)) {
            if (type == 1) {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b where a.id=b.user_id and a.username like @a");
                sql.params().set("a", "%" + keyword + "%");
            } else if (type == 2) {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b where a.id=b.user_id and b.nickname like @a");
                sql.params().set("a", "%" + keyword + "%");
            } else {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b where a.id=b.user_id and a.username like @a and b.nickname like @b");
                sql.params().set("a", "%" + keyword + "%");
                sql.params().set("b", "%" + keyword + "%");
            }
        } else {
            if (type == 1) {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b,sys_user_unit c where a.id=b.user_id and a.username like @a and a.id=c.user_id and c.unit_id=@unitid");
                sql.params().set("a", "%" + keyword + "%");
                sql.params().set("unitid", unitId);
            } else if (type == 2) {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b,sys_user_unit c where a.id=b.user_id and b.nickname like @a and a.id=c.user_id and c.unit_id=@unitid");
                sql.params().set("a", "%" + keyword + "%");
                sql.params().set("unitid", unitId);
            } else {
                sql = Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b,sys_user_unit c where a.id=b.user_id and a.username like @a and b.nickname like @b and a.id=c.user_id and c.unit_id=@unitid");
                sql.params().set("a", "%" + keyword + "%");
                sql.params().set("b", "%" + keyword + "%");
                sql.params().set("unitid", unitId);

            }
        }
        return userService.listPage(curPage, pageSize, sql);

    }

    private Map<String, List<Sys_menu>> getMap(List<Sys_menu> list) {
        Map<String, List<Sys_menu>> map = new HashMap<>();
        for (Sys_menu menu : list) {
            List<Sys_menu> l = map.get(menu.getParentId());
            if (l == null) {
                l = new ArrayList<>();
            } else continue;
            for (Sys_menu m : list) {
                if (m.getParentId().equals(menu.getParentId())) {
                    l.add(m);
                }
            }
            map.put(menu.getParentId(), l);
        }
        return map;
    }

    @At("/add")
    @Ok("vm:template.private.sys.role.add")
    @RequiresPermissions("sys:role")
    public Object add() {
        return "";

    }

    @At("/add/do")
    @Ok("json")
    @RequiresPermissions("sys:role")
    public Object addDo(@Param("resourceIds") String resourceIds, @Param("uids") String uids, @Param("unitid") String unitId, @Param("..") Sys_role role, HttpServletRequest req) {
        try {
            int num = roleService.count(Cnd.where("code", "=", role.getCode().trim()));
            if (num > 0) {
                return Message.error("sys.role.code", req);
            }
            roleService.save(resourceIds, uids, role);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/showrole/?")
    @Ok("vm:template.private.sys.role.showrole")
    @RequiresPermissions("sys:role")
    public Object showrole(String roleId, HttpServletRequest req) {
        Sys_role role = roleService.fetch(roleId);
        if (!"_system".equals(role.getUnitid())) {
            req.setAttribute("unitName", unitService.fetch(role.getUnitid()).getName());
        } else {
            req.setAttribute("unitName", "系统角色");
        }
        req.setAttribute("role", role);
        List<Sys_menu> list = roleService.getButtonsByRoleid(roleId);
        Map<String, List<Sys_menu>> map = getMap(list);
        req.setAttribute("buttons", map);
        req.setAttribute("allBtns", list);
        return roleService.getMenusByRoleid(roleId);
    }

    @At("/showuser/?")
    @Ok("vm:template.private.sys.role.showuser")
    @RequiresPermissions("sys:role")
    public Object showuser(String roleId, HttpServletRequest req) {
        req.setAttribute("role", roleService.fetch(roleId));
        return userService.list(Sqls.create("SELECT a.id,a.username,a.is_online,a.is_locked,b.email,b.nickname " +
                "FROM sys_user a,sys_user_profile b ,sys_user_role c " +
                "WHERE a.id=b.user_id AND a.id=c.user_id AND " +
                "c.role_id=@roleId ORDER BY a.create_time desc").setParam("roleId", roleId));
    }

    @At("/edit/?")
    @Ok("vm:template.private.sys.role.edit")
    @RequiresPermissions("sys:role")
    public Object edit(String roleId, HttpServletRequest req) {
        Sys_role role = roleService.fetch(roleId);
        if (!"_system".equals(role.getUnitid())) {
            req.setAttribute("unitName", unitService.fetch(role.getUnitid()).getName());
        } else {
            role.setUnitid("_system");
            req.setAttribute("unitName", "系统角色");
        }
        return role;
    }

    @At("/edit/do")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "修改角色", msg = "角色名：${args[1].name}")
    public Object editDo(@Param("unitid") String unitid, @Param("..") Sys_role role, @Param("oldcode") String oldcode, HttpServletRequest req) {
        try {
            if (!oldcode.equals(role.getCode().trim())) {
                int num = roleService.count(Cnd.where("code", "=", role.getCode().trim()));
                if (num > 0) {
                    return Message.error("sys.role.code", req);
                }
            }
            roleService.edit(role);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/edit/role/?")
    @Ok("vm:template.private.sys.role.editrole")
    @RequiresPermissions("sys:role")
    public Object edit_role(String roleId) {
        return roleService.fetch(roleId);

    }

    @At("/edit/menu/?")
    @Ok("raw:json")
    @RequiresPermissions("sys:role")
    public Object edit_menu(String roleId, @Param("pid") String pid, HttpServletRequest req) {
        List<Record> list = new ArrayList<>();
        Subject currentUser = SecurityUtils.getSubject();
        boolean isSystem = false;
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            if (user.isSystem()) {
                isSystem = true;
            }
        }
        if (!Strings.isEmpty(pid)) {
            if (isSystem) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children,icon,description as data from sys_menu  where parentId = @pid order by location asc,path asc").setParam("pid", pid));

            } else {
                list = roleService.list(
                        Sqls.create("select DISTINCT a.id,a.name as text,a.has_children as children,a.icon,a.description as data from sys_menu a,sys_role_menu b " +
                                " where a.id=b.menu_id and b.role_id=@roleId and a.parentId = @pid order by a.location asc,a.path asc").setParam("roleId", roleId).setParam("pid", pid));
            }
        } else {
            if (isSystem) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children,icon,description as data from sys_menu where length(path)=4 order by location asc,path asc"));

            } else {
                list = roleService.list(
                        Sqls.create("select DISTINCT a.id,a.name as text,a.has_children as children,a.icon,a.description as data from sys_menu a,sys_role_menu b " +
                                " where a.id=b.menu_id and b.role_id=@roleId and length(path)=4 order by a.location asc,a.path asc").setParam("roleId", roleId));
            }
        }
        return list;
    }

    @At("/edit/role/do")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "分配权限", msg = "角色名：${args[1]}")
    public Object editRoleDo(@Param("resourceIds") String resourceIds, @Param("name") String name, @Param("id") String roleId, HttpServletRequest req) {
        try {
            roleService.saveRole(resourceIds, roleId);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/edit/user/?")
    @Ok("vm:template.private.sys.role.edituser")
    @RequiresPermissions("sys:role")
    public Object editunit(String roleId, HttpServletRequest req) {
        req.setAttribute("users", roleService.list(Sqls.create("select a.id,a.username,b.nickname,b.email from sys_user a,sys_user_profile b,sys_user_role c where " +
                " a.id=b.user_id and a.id=c.user_id and c.role_id=@roleId").setParam("roleId", roleId)));
        return roleService.fetch(roleId);

    }

    @At("/edit/user/do")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "分配用户", msg = "角色名：${args[1]}")
    public Object editUserDo(@Param("uids") String uids, @Param("name") String name, @Param("id") String roleId, HttpServletRequest req) {
        try {
            roleService.saveUser(uids, roleId);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "删除角色", msg = "角色名：${args[1].getAttribute('name')}")
    public Object delete(String roleId, HttpServletRequest req) {
        try {
            Sys_role user = roleService.fetch(roleId);
            if ("superadmin".equals(user.getCode())) {
                return Message.error("system.not.allow", req);
            }
            roleService.deleteById(roleId);
            req.setAttribute("name", user.getName());
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "启用角色", msg = "角色ID：${args[0]}")
    public Object enable(String roleId, HttpServletRequest req) {
        try {
            roleService.update(org.nutz.dao.Chain.make("is_enabled", true), Cnd.where("id", "=", roleId));
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys:role")
    @SLog(tag = "禁用角色", msg = "角色ID：${args[0]}")
    public Object disable(String roleId, HttpServletRequest req) {
        try {
            if ("superadmin".equals(roleService.fetch(roleId).getCode())) {
                return Message.error("system.not.allow", req);
            }
            roleService.update(org.nutz.dao.Chain.make("is_enabled", false), Cnd.where("id", "=", roleId));
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }
}
