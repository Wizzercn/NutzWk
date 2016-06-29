package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.*;
import org.nutz.dao.Chain;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
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
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
public class UserController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    MenuService menuService;
    @Inject
    UnitService unitService;

    @At("")
    @Ok("beetl:/private/sys/user/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/sys/user/add.html")
    @RequiresAuthentication
    public Object add(@Param("unitid") String unitid) {
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.user.add")
    @SLog(tag = "新建用户", msg = "用户名:${args[0].loginname}")
    public Object addDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(user.getPassword(), salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            user.setCreateBy(Strings.sBlank(req.getAttribute("uid")));
            user.setLoginCount(0);
            user.setLoginAt(0);
            userService.insert(user);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/sys/user/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return userService.fetchLinks(userService.fetch(id), "unit");
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "修改用户", msg = "用户名:${args[1]}->${args[0].loginname}")
    public Object editDo(@Param("..") Sys_user user, @Param("oldLoginname") String oldLoginname, HttpServletRequest req) {
        try {
            if (!Strings.sBlank(oldLoginname).equals(user.getLoginname())) {
                Sys_user u = userService.fetch(Cnd.where("loginname", "=", user.getLoginname()));
                if (u != null)
                    return Result.error("用户名已存在", req);
            }
            user.setUpdateAt((int) (System.currentTimeMillis() / 1000));
            userService.updateIgnoreNull(user);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/resetPwd/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "重置密码", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object resetPwd(String id, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(id);
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("ET922", salt, 1024).toBase64();
            userService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", id));
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success", "ET922", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.delete")
    @SLog(tag = "删除用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object delete(String userId, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(userId);
            if ("superadmin".equals(user.getLoginname())) {
                return Result.error("system.not.allow", req);
            }
            userService.deleteById(userId);
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.delete")
    @SLog(tag = "批量删除用户", msg = "用户ID:${args[1].getAttribute('ids')}")
    public Object deletes(@Param("ids") String[] userIds, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(Cnd.where("loginname", "=", "superadmin"));
            StringBuilder sb = new StringBuilder();
            for (String s : userIds) {
                if (s.equals(user.getId())) {
                    return Result.error("system.not.allow", req);
                }
                sb.append(s).append(",");
            }
            userService.deleteByIds(userIds);
            req.setAttribute("ids", sb.toString());
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "启用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object enable(String userId, HttpServletRequest req) {
        try {
            req.setAttribute("loginname", userService.fetch(userId).getLoginname());
            userService.update(Chain.make("disabled", false), Cnd.where("id", "=", userId));
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "禁用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object disable(String userId, HttpServletRequest req) {
        try {
            String loginname = userService.fetch(userId).getLoginname();
            if ("superadmin".equals(loginname)) {
                return Result.error("system.not.allow", req);
            }
            req.setAttribute("loginname", loginname);
            userService.update(Chain.make("disabled", true), Cnd.where("id", "=", userId));
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/detail/?")
    @Ok("beetl:/private/sys/user/detail.html")
    @RequiresAuthentication
    public Object detail(String id) {
        if (!Strings.isBlank(id)) {
            Sys_user user = userService.fetch(id);
            return userService.fetchLinks(user, "roles");
        }
        return null;
    }

    @At("/menu/?")
    @Ok("beetl:/private/sys/user/menu.html")
    @RequiresAuthentication
    public Object menu(String id, HttpServletRequest req) {
        Sys_user user = userService.fetch(id);
        List<Sys_menu> menus = userService.getMenusAndButtons(id);
        List<Sys_menu> datas = userService.getDatas(id);
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
        return user;
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("unitid") String unitid, @Param("loginname") String loginname, @Param("nickname") String nickname, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        if (!Strings.isBlank(loginname))
            cnd.and("loginname", "like", "%" + loginname + "%");
        if (!Strings.isBlank(nickname))
            cnd.and("nickname", "like", "%" + nickname + "%");
        return userService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "root");
        obj.put("text", "所有用户");
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

    @At
    @Ok("beetl:/private/sys/user/pass.html")
    @RequiresAuthentication
    public void pass() {

    }

    @At
    @Ok("beetl:/private/sys/user/custom.html")
    @RequiresAuthentication
    public void custom() {

    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            userService.update(Chain.make("customMenu", ids), Cnd.where("id", "=", req.getAttribute("uid")));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (!Strings.isBlank(ids)) {
                user.setCustomMenu(ids);
                user.setCustomMenus(menuService.query(Cnd.where("id", "in", ids.split(","))));
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        String old = new Sha256Hash(oldPassword, user.getSalt(), 1024).toBase64();
        if (old.equals(user.getPassword())) {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(newPassword, salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            userService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", user.getId()));
            return Result.success("修改成功", req);
        } else {
            return Result.error("原密码不正确", req);
        }
    }
}
