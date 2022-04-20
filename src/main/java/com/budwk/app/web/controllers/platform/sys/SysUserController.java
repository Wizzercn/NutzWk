package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.base.utils.PwdUtil;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.models.Sys_unit;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysMenuService;
import com.budwk.app.sys.services.SysUnitService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.json4excel.J4E;
import org.nutz.integration.json4excel.J4EColumn;
import org.nutz.integration.json4excel.J4EConf;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/sys/user")
public class SysUserController {
    private static final Log log = Logs.get();
    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysMenuService sysMenuService;
    @Inject
    private SysUnitService sysUnitService;

    @At("")
    @Ok("beetl:/platform/sys/user/index.html")
    @SaCheckPermission("sys.manager.user")
    public void index() {

    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.user.add")
    @SLog(tag = "新建用户", msg = "用户名:${user.loginname}")
    public Object addDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            if (Strings.isNotBlank(user.getLoginname())) {
                int num = sysUserService.count(Cnd.where("loginname", "=", Strings.trim(user.getLoginname())));
                if (num > 0) {
                    return Result.error("用户名已存在!");
                }
            }
            if (Strings.isNotBlank(user.getMobile())) {
                int num = sysUserService.count(Cnd.where("mobile", "=", Strings.trim(user.getMobile())));
                if (num > 0) {
                    return Result.error("手机号已存在!");
                }
            }
            if (Strings.isNotBlank(user.getEmail())) {
                int num = sysUserService.count(Cnd.where("email", "=", Strings.trim(user.getEmail())));
                if (num > 0) {
                    return Result.error("邮箱已存在!");
                }
            }
            String salt = R.UU32();
            user.setSalt(salt);
            user.setPassword(PwdUtil.getPassword(user.getPassword(), salt));
            user.setLoginPjax(true);
            user.setLoginCount(0);
            user.setUnitPath(sysUnitService.fetch(user.getUnitId()).getPath());
            sysUserService.insert(user);
            sysUserService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user")
    public Object edit(String id) {
        try {
            return Result.success().addData(sysUserService.fetchLinks(sysUserService.fetch(id), "unit"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.user.edit")
    @SLog(tag = "修改用户", msg = "用户名:${user.loginname}")
    public Object editDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            sysUserService.updateIgnoreNull(user);
            sysUserService.deleteCache(user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/resetPwd/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user.edit")
    @SLog(tag = "重置密码", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object resetPwd(String id, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.fetch(id);
            String salt = R.UU32();
            String pwd = R.captchaNumber(6);
            String hashedPasswordBase64 = PwdUtil.getPassword(pwd, salt);
            sysUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", id));
            sysUserService.deleteCache(user.getId());
            req.setAttribute("loginname", user.getLoginname());
            return Result.success().addData(pwd);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user.delete")
    @SLog(tag = "删除用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object delete(String userId, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.fetch(userId);
            if ("superadmin".equals(user.getLoginname())) {
                return Result.error("system.not.allow");
            }
            sysUserService.deleteById(userId);
            sysUserService.deleteCache(user.getId());
            req.setAttribute("loginname", user.getLoginname());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete")
    @Ok("json")
    @SaCheckPermission("sys.manager.user.delete")
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
            sysUserService.clearCache();
            req.setAttribute("ids", sb.toString());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user.edit")
    @SLog(tag = "启用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object enable(String userId, HttpServletRequest req) {
        try {
            req.setAttribute("loginname", sysUserService.fetch(userId).getLoginname());
            sysUserService.update(Chain.make("disabled", false), Cnd.where("id", "=", userId));
            sysUserService.deleteCache(userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user.edit")
    @SLog(tag = "禁用用户", msg = "用户名:${req.getAttribute('loginname')}")
    public Object disable(String userId, HttpServletRequest req) {
        try {
            String loginname = sysUserService.fetch(userId).getLoginname();
            if ("superadmin".equals(loginname)) {
                return Result.error("system.not.allow");
            }
            req.setAttribute("loginname", loginname);
            sysUserService.update(Chain.make("disabled", true), Cnd.where("id", "=", userId));
            sysUserService.deleteCache(userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/menu/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.user")
    public Object menu(String id, @Param("pid") String pid, HttpServletRequest req) {
        try {
            List<Sys_menu> list = sysUserService.getRoleMenus(id, pid);
            List<NutMap> treeList = new ArrayList<>();
            for (Sys_menu unit : list) {
                if (!unit.isHasChildren() && sysUserService.hasChildren(id, unit.getId())) {
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

    @At
    @Ok("json:{locked:'password|salt',ignoreNull:false}")
    @SaCheckPermission("sys.manager.user")
    public Object data(@Param("searchUnit") String searchUnit, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (StpUtil.hasRole("sysadmin")) {
                if (Strings.isNotBlank(searchUnit)) {
                    cnd.and("unitid", "=", searchUnit);
                }
            } else {
                Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
                if (Strings.isNotBlank(searchUnit)) {
                    Sys_unit unit = sysUnitService.fetch(searchUnit);
                    if (unit == null || !unit.getPath().startsWith(user.getUnit().getPath())) {
                        //防止有人越级访问
                        return Result.error("非法操作");
                    } else
                        cnd.and("unitId", "=", searchUnit);
                } else {
                    cnd.and("unitId", "=", user.getUnitId());
                }
            }
            if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysUserService.listPageLinks(pageNumber, pageSize, cnd, "unit"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("void")
    @SaCheckPermission("sys.manager.user")
    public void export(@Param("searchUnit") String searchUnit, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy, HttpServletResponse response) {
        try {
            J4EConf j4eConf = J4EConf.from(Sys_user.class);
            List<J4EColumn> jcols = j4eConf.getColumns();
            for (J4EColumn j4eColumn : jcols) {
                if ("createdBy".equals(j4eColumn.getFieldName()) || "createdAt".equals(j4eColumn.getFieldName()) || "delFlag".equals(j4eColumn.getFieldName())) {
                    j4eColumn.setIgnore(true);
                }
            }
            Cnd cnd = Cnd.NEW();
            if (StpUtil.hasRole("sysadmin")) {
                if (Strings.isNotBlank(searchUnit)) {
                    cnd.and("unitid", "=", searchUnit);
                }
            } else {
                Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
                if (Strings.isNotBlank(searchUnit)) {
                    Sys_unit unit = sysUnitService.fetch(searchUnit);
                    if (unit == null || !unit.getPath().startsWith(user.getUnit().getPath())) {
                        //防止有人越级访问
                        throw Lang.makeThrow("非法操作");
                    } else
                        cnd.and("unitId", "=", searchUnit);
                } else {
                    cnd.and("unitId", "=", user.getUnitId());
                }
            }
            if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            OutputStream out = response.getOutputStream();
            response.setHeader("content-type", "application/shlnd.ms-excel;charset=utf-8");
            response.setHeader("content-disposition", "attachment; filename=" + new String("用户信息".getBytes(), "ISO-8859-1") + ".xls");
            J4E.toExcel(out, sysUserService.query(cnd, "unit"), j4eConf);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @At
    @Ok("beetl:/platform/sys/user/pass.html")
    @SaCheckLogin
    public void pass() {

    }

    @At
    @Ok("beetl:/platform/sys/user/info.html")
    @SaCheckLogin
    public void info(HttpServletRequest req) {
        Sys_user user = sysUserService.fetch(SecurityUtil.getUserId());
        req.setAttribute("user", user);
    }

    @At
    @Ok("beetl:/platform/sys/user/custom.html")
    @SaCheckLogin
    public void custom() {

    }

    @At
    @Ok("beetl:/platform/sys/user/mode.html")
    @SaCheckLogin
    public void mode() {

    }

    @At
    @Ok("json")
    @SaCheckLogin
    public Object modeDo(@Param("mode") String mode, HttpServletRequest req) {
        try {
            if ("superadmin".equals(SecurityUtil.getUserUsername()) && Globals.MyConfig.getBoolean("AppDemoEnv")) {
                return Result.error("演示环境，不予操作");
            }
            sysUserService.update(Chain.make("loginPjax", "true".equals(mode)), Cnd.where("id", "=", SecurityUtil.getUserId()));
            Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
            if ("true".equals(mode)) {
                user.setLoginPjax(true);
            } else {
                user.setLoginPjax(false);
            }
            sysUserService.deleteCache(user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At
    @Ok("json")
    @SaCheckLogin
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            if ("superadmin".equals(SecurityUtil.getUserLoginname()) && Globals.MyConfig.getBoolean("AppDemoEnv")) {
                return Result.error("演示环境，不予操作");
            }
            sysUserService.update(Chain.make("customMenu", ids), Cnd.where("id", "=", SecurityUtil.getUserId()));
            Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
            if (Strings.isNotBlank(ids)) {
                user.setCustomMenu(ids);
                user.setCustomMenus(sysMenuService.query(Cnd.where("id", "in", ids.split(","))));
            } else {
                user.setCustomMenu("");
                user.setCustomMenus(new ArrayList<>());
            }
            sysUserService.deleteCache(user.getId());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckLogin
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        if ("superadmin".equals(SecurityUtil.getUserLoginname()) && Globals.MyConfig.getBoolean("AppDemoEnv")) {
            return Result.error("演示环境，不予操作");
        }
        Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
        String old = PwdUtil.getPassword(oldPassword, user.getSalt());
        if (old.equals(user.getPassword())) {
            String salt = R.UU32();
            String hashedPasswordBase64 = PwdUtil.getPassword(newPassword, salt);
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            sysUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", user.getId()));
            sysUserService.deleteCache(user.getId());
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckLogin
    public Object doChangeInfo(@Param("username") String username, @Param("mobile") String mobile, @Param("email") String email, HttpServletRequest req) {
        if ("superadmin".equals(SecurityUtil.getUserLoginname()) && Globals.MyConfig.getBoolean("AppDemoEnv")) {
            return Result.error("演示环境，不予操作");
        }
        sysUserService.update(Chain.make("username", username).add("mobile", mobile).add("email", email), Cnd.where("id", "=", SecurityUtil.getUserId()));
        sysUserService.deleteCache(SecurityUtil.getUserId());
        return Result.success();
    }
}
