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
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.ShiroUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
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
import java.util.List;

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

    @At("/tree")
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        try {
            List<Sys_unit> list = new ArrayList<>();
            List<NutMap> treeList = new ArrayList<>();
            if (Strings.isBlank(pid)) {
                NutMap root = NutMap.NEW().addv("value", "root").addv("label", "不选择单位");
                treeList.add(root);

            }
            if (shiroUtil.hasRole("sysadmin")) {
                if (Strings.isBlank(pid)) {
                    NutMap sys = NutMap.NEW().addv("value", "system").addv("label", "系统角色");
                    treeList.add(sys);
                }
                Cnd cnd = Cnd.NEW();
                if (Strings.isBlank(pid)) {
                    cnd.and("parentId", "=", "").or("parentId", "is", null);
                } else {
                    cnd.and("parentId", "=", pid);
                }
                cnd.asc("location").asc("path");
                list = sysUnitService.query(cnd);
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
                    cnd.asc("location").asc("path");
                    list = sysUnitService.query(cnd);
                }
            }
            for (Sys_unit unit : list) {
                NutMap map = NutMap.NEW().addv("value", unit.getId()).addv("label", unit.getName());
                if (unit.isHasChildren()) {
                    map.addv("children", new ArrayList<>());
                }
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object data(@Param("searchUnit") String searchUnit, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (shiroUtil.hasRole("sysadmin")) {
                if ("system".equals(searchUnit)) {
                    cnd.and("unitid", "=", "");
                } else if (Strings.isNotBlank(searchUnit)) {
                    cnd.and("unitid", "=", searchUnit);
                }
            } else {
                Sys_user user = (Sys_user) shiroUtil.getPrincipal();
                if (Strings.isNotBlank(searchUnit)) {
                    Sys_unit unit = sysUnitService.fetch(searchUnit);
                    if (unit == null || !unit.getPath().startsWith(user.getUnit().getPath())) {
                        //防止有人越级访问
                        return Result.error("非法操作");
                    } else
                        cnd.and("unitid", "=", searchUnit);
                } else {
                    cnd.and("unitid", "=", user.getUnitid());
                }
            }
            if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysRoleService.listPageLinks(pageNumber, pageSize, cnd, "unit"));
        } catch (Exception e) {
            return Result.error();
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
            sysRoleService.clearCache();
            sysUserService.clearCache();
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
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
            sysRoleService.clearCache();
            sysUserService.clearCache();
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    //加载当前用户所拥有的所有权限菜单
    @At("/menuAll")
    @Ok("json")
    @RequiresPermissions("sys.manager.role")
    public Object menuAll(HttpServletRequest req) {
        try {
            List<Sys_menu> list;
            if (shiroUtil.hasRole("sysadmin")) {
                list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
            } else {
                list = sysUserService.getMenusAndButtons(StringUtil.getPlatformUid());
            }
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

    //加载角色下所有菜单,新建角色
    @At("/menuRole/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role")
    public Object menuRole(String roleId, HttpServletRequest req) {
        try {
            List<Sys_menu> userList = sysRoleService.getMenusAndButtons(roleId);
            List<Sys_menu> list;
            if (shiroUtil.hasRole("sysadmin")) {
                list = sysMenuService.query(Cnd.orderBy().asc("location").asc("path"));
            } else {
                list = sysRoleService.getMenusAndButtons(roleId);
            }
            NutMap menuMap = NutMap.NEW();
            for (Sys_menu unit : list) {
                List<Sys_menu> list1 = menuMap.getList(unit.getParentId(), Sys_menu.class);
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
                list1.add(unit);
                menuMap.put(unit.getParentId(), list1);
            }
            List<String> cmenu = new ArrayList<>();
            for (Sys_menu menu : userList) {
                cmenu.add(menu.getId());
            }
            return Result.success().addData(NutMap.NEW().addv("menu", getTree(menuMap, "")).addv("cmenu", cmenu));
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
    @RequiresPermissions("sys.manager.role.add")
    @SLog(tag = "添加角色", msg = "角色名称:${args[1].name}")
    public Object addDo(@Param("menuIds") String menuIds, @Param("..") Sys_role role, HttpServletRequest req) {
        try {
            int num = sysRoleService.count(Cnd.where("code", "=", role.getCode().trim()));
            if (num > 0) {
                return Result.error("sys.role.code");
            }
            String[] ids = StringUtils.split(menuIds, ",");
            if ("root".equals(role.getUnitid()) || "system".equals(role.getUnitid()))
                role.setUnitid("");
            role.setOpBy(StringUtil.getPlatformUid());
            Sys_role r = sysRoleService.insert(role);
            if (r != null) {
                sysRoleService.saveMenu(ids, r.getId());
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    //查看角色拥有的权限
    @At("/menu/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role")
    public Object menu(String id, @Param("pid") String pid) {
        try {
            List<Sys_menu> list = sysRoleService.getRoleMenus(id, pid);
            List<NutMap> treeList = new ArrayList<>();
            for (Sys_menu unit : list) {
                if (!unit.isHasChildren() && sysRoleService.hasChildren(id, unit.getId())) {
                    unit.setHasChildren(true);
                }
                NutMap map = Lang.obj2nutmap(unit);
                map.addv("expanded", false);
                map.addv("children", new ArrayList<>());
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
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
            sysRoleService.clearCache();
            sysUserService.clearCache();
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.role")
    public Object edit(String roleId, HttpServletRequest req) {
        try {
            return Result.success().addData(sysRoleService.fetch(roleId));
        } catch (Exception e) {
            return Result.error();
        }
    }

    //修改角色
    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.edit")
    @SLog(tag = "修改角色", msg = "角色名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_role role, HttpServletRequest req) {
        try {
            Sys_role oldRole = sysRoleService.fetch(role.getId());
            if (oldRole != null && !Strings.sBlank(oldRole.getCode()).equalsIgnoreCase(role.getCode())) {
                int num = sysRoleService.count(Cnd.where("code", "=", role.getCode().trim()));
                if (num > 0) {
                    return Result.error("sys.role.code");
                }
            }
            if ("root".equals(role.getUnitid()) || "system".equals(role.getUnitid()))
                role.setUnitid("");
            role.setOpBy(StringUtil.getPlatformUid());
            role.setOpAt(Times.getTS());
            sysRoleService.updateIgnoreNull(role);
            sysRoleService.clearCache();
            sysUserService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.menu")
    @SLog(tag = "分配角色菜单", msg = "角色名称:${args[2].getAttribute('name')}")
    public Object menuDo(@Param("menuIds") String menuIds, @Param("roleId") String roleId, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(menuIds, ",");
            sysRoleService.saveMenu(ids, roleId);
            Sys_role role = sysRoleService.fetch(roleId);
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object user(@Param("roleId") String roleId, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword,
                       @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Sql sql = Sqls.create("SELECT a.*,c.name as unitname FROM sys_user a,sys_user_role b,sys_unit c WHERE a.unitid=c.id and a.id=b.userId and b.roleId=@roleId $s $o");
            sql.params().set("roleId", roleId);
            if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
                sql.vars().set("s", " and a." + searchName + " like '%" + searchKeyword + "%'");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                sql.vars().set("o", " order by a." + pageOrderName + " " + PageUtil.getOrder(pageOrderBy));

            }
            return Result.success().addData(sysUserService.listPage(pageNumber, pageSize, sql));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.role")
    public Object userSearch(@Param("query") String keyword, @Param("roleId") String roleId) {
        try {
            Sys_user user = (Sys_user) shiroUtil.getPrincipal();
            return Result.success().addData(sysRoleService.userSearch(roleId, keyword, shiroUtil.hasRole("sysadmin"), user.getUnit()));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    @SLog(tag = "添加用户到角色", msg = "角色名称:${args[2].getAttribute('name')},用户ID:${args[0]}")
    public Object usersAdd(@Param("users") String users, @Param("roleId") String roleId, HttpServletRequest req) {
        try {
            String[] ids = StringUtils.split(users, ",");
            for (String s : ids) {
                sysRoleService.insert("sys_user_role", org.nutz.dao.Chain.make("roleId", roleId).add("userId", s));
            }
            Sys_role role = sysRoleService.fetch(roleId);
            sysRoleService.clearCache();
            sysUserService.clearCache();
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.user")
    @SLog(tag = "从角色中移除用户", msg = "角色名称:${args[2].getAttribute('name')},用户ID:${args[0]}")
    public Object usersDel(@Param("users") String users, @Param("roleId") String roleId, HttpServletRequest req) {
        try {
            String superadminId = sysUserService.fetch(Cnd.where("loginname", "=", "superadmin")).getId();
            String sysadminRoleid = sysRoleService.fetch(Cnd.where("code", "=", "sysadmin")).getId();
            String[] ids = StringUtils.split(users, ",");
            if (Lang.contains(ids, superadminId) && roleId.equals(sysadminRoleid)) {
                return Result.error("超级管理员不能从[系统管理员]角色里删除");
            }
            sysRoleService.clear("sys_user_role", Cnd.where("userId", "in", ids).and("roleId", "=", roleId));
            Sys_role role = sysRoleService.fetch(roleId);
            sysRoleService.clearCache();
            sysUserService.clearCache();
            req.setAttribute("name", role.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
