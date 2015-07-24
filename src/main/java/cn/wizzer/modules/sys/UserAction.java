package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.page.Pagination;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.*;
import cn.wizzer.modules.sys.service.RoleService;
import cn.wizzer.modules.sys.service.UnitService;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.*;
import org.nutz.dao.Chain;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag = "用户管理", msg = "")
public class UserAction {
    @Inject
    UserService userService;
    @Inject
    UnitService unitService;
    @Inject
    RoleService roleService;

    @At("")
    @Ok("vm:template.private.sys.user.index")
    @RequiresPermissions("sys:user")
    @SLog(tag = "用户列表", msg = "访问用户列表")
    public Object index() {
        return "";
    }

    @At
    @Ok("vm:template.private.sys.user.list")
    @RequiresPermissions("sys:user")
    public Pagination list(@Param("curPage") int curPage, @Param("pageSize") int pageSize, @Param("unitid") String unitid, @Param("username") String username, @Param("nickname") String nickname, HttpServletRequest req) {
        String s = "";
        if (!Strings.isEmpty(username)) {
            s += " and a.username like '%" + username + "%' ";
        }
        if (!Strings.isEmpty(nickname)) {
            s += " and b.nickname like '%" + nickname + "%' ";
        }
        return userService.listPage(curPage, pageSize, Sqls.create("SELECT a.id,a.username,a.is_online,a.is_locked,b.email,b.nickname " +
                "FROM sys_user a,sys_user_profile b ,sys_user_unit c " +
                "WHERE a.id=b.user_id AND a.id=c.user_id AND " +
                "c.unit_id=@unitid $s ORDER BY a.create_time desc").setParam("unitid", unitid).setVar("s", s));
    }

    @At("/tree")
    @Ok("raw:json")
    @RequiresPermissions("sys:user")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list;
        if (!Strings.isEmpty(pid)) {
            list = userService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = @pid order by location asc,path asc").setParam("pid", pid));
        } else {
            list = userService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where length(path)=4 order by location asc,path asc"));
        }
        return list;
    }

    @At("/add")
    @Ok("vm:template.private.sys.user.add")
    @RequiresPermissions("sys:user")
    public void add(@Param("unitId") String unitId, HttpServletRequest req) {
        if (!Strings.isEmpty(unitId)) {
            req.setAttribute("parentUnit", unitService.fetch(unitId));
        }
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            if (user.isSystem()) {
                req.setAttribute("roleList", roleService.query(Cnd.where("unitid", "=", "").or("unitid", "is", null).asc("location"), null));
            }
        }
    }

    @At("/add/do")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "新建用户", msg = "用户名：${args[0].username}")
    public Object addDo(@Param("::u.") Sys_user user, @Param("::p.") Sys_user_profile profile, @Param("unitId") String unitId, @Param("roleIds") String[] roleIds, HttpServletRequest req) {
        try {
            int num = userService.count(Cnd.where("username", "=", user.getUsername().trim()));
            if (num > 0) {
                return Message.error("sys.user.username", req);
            }
            if (!Strings.isEmpty(profile.getEmail())) {
                if (userService.count("sys_user_profile", Cnd.where("email", "=", profile.getEmail().trim())) > 0) {
                    return Message.error("sys.user.email", req);
                }
            }
            userService.save(user, profile, unitId, roleIds);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("vm:template.private.sys.user.edit")
    @RequiresPermissions("sys:user")
    public Object edit(String userId, HttpServletRequest req) {
        Sys_user user = userService.info(userId);
        Sys_unit unit = userService.getUnit(userId);
        req.setAttribute("parentUnit", unit);
        Cnd cnd = Cnd.where("unitid", "=", unit.getId());
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Sys_user cuser = (Sys_user) currentUser.getPrincipal();
            if (cuser.isSystem()) {
                cnd = Cnd.where("unitid", "=", "").or("unitid", "is", null).or("unitid", "=", unit.getId());
            }
        }
        req.setAttribute("roleList", roleService.query(cnd.asc("location"), null));
        req.setAttribute("roleIds", userService.getRoleCodeList(user));
        return user;
    }

    @At("/edit/do")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "编辑用户", msg = "用户名：${args[0].username}")
    public Object editDo(@Param("::u.") Sys_user user, @Param("::p.") Sys_user_profile profile, @Param("unitId") String unitId, @Param("roleIds") String[] roleIds, HttpServletRequest req) {
        try {
            Sys_user u = userService.info(user.getId());
            if (!u.getUsername().equals(user.getUsername())) {
                int num = userService.count(Cnd.where("username", "=", user.getUsername().trim()));
                if (num > 0) {
                    return Message.error("sys.user.username", req);
                }
            }
            if (!Strings.isEmpty(profile.getEmail()) && !profile.getEmail().equals(u.getProfile().getEmail())) {
                if (userService.count("sys_user_profile", Cnd.where("email", "=", profile.getEmail().trim())) > 0) {
                    return Message.error("sys.user.email", req);
                }
            }
            userService.edit(user, profile, unitId, roleIds);
            return Message.success("system.success", req);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("system.error", req);
        }
    }

    @At("/info/?")
    @Ok("vm:template.private.sys.user.info")
    @RequiresPermissions("sys:user")
    public Object info(String userId, HttpServletRequest req) {
        Sys_unit unit = userService.getUnit(userId);
        if (unit != null) req.setAttribute("unitName", unit.getName());
        req.setAttribute("roles", userService.getReolnames(userId));
        return userService.info(userId);
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "删除用户", msg = "用户名：${args[1].getAttribute('username')}")
    public Object delete(String userId, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(userId);
            if ("superadmin".equals(user.getUsername())) {
                return Message.error("system.nodel", req);
            }
            userService.deleteById(userId);
            req.setAttribute("username", user.getUsername());
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "批量删除", msg = "用户ID：${args[1].getAttribute('ids')}")
    public Object deletes(@Param("ids") String[] userIds, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(Cnd.where("username", "=", "superadmin"));
            StringBuilder sb = new StringBuilder();
            for (String s : userIds) {
                if (s.equals(user.getId())) {
                    return Message.error("system.nodel", req);
                }
                sb.append(s).append(",");
            }
            userService.deleteByIds(userIds);
            req.setAttribute("ids", sb.toString());
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "启用用户", msg = "用户ID：${args[0]}")
    public Object enable(String userId, HttpServletRequest req) {
        try {
            userService.update(Chain.make("is_locked", false), Cnd.where("id", "=", userId));
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "禁用用户", msg = "用户ID：${args[0]}")
    public Object disable(String userId, HttpServletRequest req) {
        try {
            userService.update(Chain.make("is_locked", true), Cnd.where("id", "=", userId));
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/resetPwd/?")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "重置密码", msg = "用户ID：${args[0]}")
    public Object resetPwd(String userId, HttpServletRequest req) {
        try {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String password = StringUtils.getRndNumber(6);
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
            userService.update(Chain.make("password", hashedPasswordBase64).add("salt", salt), Cnd.where("id", "=", userId));
            return Message.success("system.success", password, req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/role/?")
    @Ok("vm:template.private.sys.user.role")
    @RequiresPermissions("sys:user")
    public Object role(String userId, HttpServletRequest req) {
        Sys_unit unit = userService.getUnit(userId);
        if (unit != null) req.setAttribute("unitName", unit.getName());
        req.setAttribute("user", userService.info(userId));
        List<Sys_menu> list = userService.getButtons(userId);
        Map<String, List<Sys_menu>> map=getMap(list);
        req.setAttribute("buttons", map);
        req.setAttribute("allBtns", list);
        return userService.getMenus(userId);
    }

    private Map<String, List<Sys_menu>> getMap(List<Sys_menu> list) {
        Map<String, List<Sys_menu>> map = new HashMap<>();
        for (Sys_menu menu : list) {
            List<Sys_menu> l = map.get(menu.getParentId());
            if (l == null) {
                l = new ArrayList<>();
            }else continue;
            for (Sys_menu m : list) {
                if (m.getParentId().equals(menu.getParentId())) {
                    l.add(m);
                }
            }
            map.put(menu.getParentId(), l);
        }
        return map;
    }
}
