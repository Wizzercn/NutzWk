package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysMenuService;
import cn.wizzer.app.sys.modules.services.SysUnitService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.ShiroUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
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
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/sys/user")
public class SysUserController {
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
    private ShiroUtil shiroUtil;

    @At("")
    @Ok("beetl:/platform/sys/user/index.html")
    @RequiresPermissions("sys.manager.user")
    public void index() {

    }

    @At
    @Ok("beetl:/platform/sys/user/add.html")
    @RequiresPermissions("sys.manager.user")
    public Object add(@Param("unitid") String unitid) {
        return Strings.isBlank(unitid) ? null : sysUnitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.user.add")
    @SLog(tag = "新建用户", msg = "用户名:${args[0].loginname}")
    public Object addDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            String salt = R.UU32();
            user.setSalt(salt);
            user.setPassword(new Sha256Hash(user.getPassword(), ByteSource.Util.bytes(salt), 1024).toHex());
            user.setLoginPjax(true);
            user.setLoginCount(0);
            user.setLoginAt(0L);
            sysUserService.insert(user);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/user/edit.html")
    @RequiresPermissions("sys.manager.user")
    public Object edit(String id) {
        return sysUserService.fetchLinks(sysUserService.fetch(id), "unit");
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "修改用户", msg = "用户名:${args[1]}->${args[0].loginname}")
    public Object editDo(@Param("..") Sys_user user, @Param("oldLoginname") String oldLoginname, HttpServletRequest req) {
        try {
            if (!Strings.sBlank(oldLoginname).equals(user.getLoginname())) {
                Sys_user u = sysUserService.fetch(Cnd.where("loginname", "=", user.getLoginname()));
                if (u != null)
                    return Result.error("用户名已存在");
            }
            user.setOpBy(StringUtil.getPlatformUid());
            user.setOpAt(Times.getTS());
            sysUserService.updateIgnoreNull(user);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/resetPwd/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "重置密码", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object resetPwd(String id, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.fetch(id);
            String salt = R.UU32();
            String pwd = R.captchaNumber(6);
            String hashedPasswordBase64 = new Sha256Hash(pwd, ByteSource.Util.bytes(salt), 1024).toHex();
            sysUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", id));
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success", pwd);
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.delete")
    @SLog(tag = "删除用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object delete(String userId, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.fetch(userId);
            if ("superadmin".equals(user.getLoginname())) {
                return Result.error("system.not.allow");
            }
            sysUserService.deleteById(userId);
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.delete")
    @SLog(tag = "批量删除用户", msg = "用户ID:${args[1].getAttribute('ids')}")
    public Object deletes(@Param("ids") String[] userIds, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.fetch(Cnd.where("loginname", "=", "superadmin"));
            StringBuilder sb = new StringBuilder();
            for (String s : userIds) {
                if (s.equals(user.getId())) {
                    return Result.error("system.not.allow");
                }
                sb.append(s).append(",");
            }
            sysUserService.deleteByIds(userIds);
            req.setAttribute("ids", sb.toString());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "启用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object enable(String userId, HttpServletRequest req) {
        try {
            req.setAttribute("loginname", sysUserService.fetch(userId).getLoginname());
            sysUserService.update(Chain.make("disabled", false), Cnd.where("id", "=", userId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.user.edit")
    @SLog(tag = "禁用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object disable(String userId, HttpServletRequest req) {
        try {
            String loginname = sysUserService.fetch(userId).getLoginname();
            if ("superadmin".equals(loginname)) {
                return Result.error("system.not.allow");
            }
            req.setAttribute("loginname", loginname);
            sysUserService.update(Chain.make("disabled", true), Cnd.where("id", "=", userId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/user/detail.html")
    @RequiresPermissions("sys.manager.user")
    public Object detail(String id) {
        if (!Strings.isBlank(id)) {
            Sys_user user = sysUserService.fetch(id);
            return sysUserService.fetchLinks(user, "roles");
        }
        return null;
    }

    @At("/menu/?")
    @Ok("beetl:/platform/sys/user/menu.html")
    @RequiresPermissions("sys.manager.user")
    public Object menu(String id, HttpServletRequest req) {
        Sys_user user = sysUserService.fetch(id);
        List<Sys_menu> menus = sysUserService.getMenusAndButtons(id);
        List<Sys_menu> datas = sysUserService.getDatas(id);
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
    @Ok("json:{locked:'password|salt',ignoreNull:false}")
    @RequiresPermissions("sys.manager.user")
    public Object data(@Param("searchUnit") String searchUnit, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (shiroUtil.hasRole("sysadmin")) {
            if (Strings.isNotBlank(searchUnit)) {
                cnd.and("unitid", "=", searchUnit);
            }
        } else {
            Sys_user user = (Sys_user) shiroUtil.getPrincipal();
            if (Strings.isNotBlank(searchUnit)) {
                Sys_unit unit = sysUnitService.fetch(searchUnit);
                if (unit == null || !searchUnit.startsWith(unit.getPath())) {
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
        return Result.success().addData(sysUserService.listPage(pageNumber, pageSize, cnd));
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.user")
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
                obj.put("text", "所有用户");
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

    @At
    @Ok("beetl:/platform/sys/user/pass.html")
    @RequiresAuthentication
    public void pass() {

    }

    @At
    @Ok("beetl:/platform/sys/user/custom.html")
    @RequiresAuthentication
    public void custom() {

    }

    @At
    @Ok("beetl:/platform/sys/user/mode.html")
    @RequiresAuthentication
    public void mode() {

    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object modeDo(@Param("mode") String mode, HttpServletRequest req) {
        try {
            sysUserService.update(Chain.make("loginPjax", "true".equals(mode)), Cnd.where("id", "=", req.getAttribute("uid")));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("true".equals(mode)) {
                user.setLoginPjax(true);
            } else {
                user.setLoginPjax(false);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @At
    @Ok("json")
    @RequiresAuthentication
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            sysUserService.update(Chain.make("customMenu", ids), Cnd.where("id", "=", StringUtil.getPlatformUid()));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (!Strings.isBlank(ids)) {
                user.setCustomMenu(ids);
                user.setCustomMenus(sysMenuService.query(Cnd.where("id", "in", ids.split(","))));
            } else {
                user.setCustomMenu("");
                user.setCustomMenus(new ArrayList<>());
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        String old = new Sha256Hash(oldPassword, user.getSalt(), 1024).toHex();
        if (old.equals(user.getPassword())) {
            String salt = R.UU32();
            String hashedPasswordBase64 = new Sha256Hash(newPassword, ByteSource.Util.bytes(salt), 1024).toHex();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            sysUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", user.getId()));
            return Result.success("修改成功");
        } else {
            return Result.error("原密码不正确");
        }
    }
}
