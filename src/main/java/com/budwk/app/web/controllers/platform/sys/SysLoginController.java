package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.budwk.app.base.exception.BaseException;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PwdUtil;
import com.budwk.app.sys.models.Sys_log;
import com.budwk.app.sys.models.Sys_role;
import com.budwk.app.sys.models.Sys_unit;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysMsgService;
import com.budwk.app.sys.services.SysRoleService;
import com.budwk.app.sys.services.SysUnitService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.service.ValidateService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.slog.SLogService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/platform/login") // 整个模块的路径前缀
@Ok("json:{locked:'password|createAt',ignoreNull:true}") // 忽略password和createAt属性,忽略空属性的json输出
public class SysLoginController {
    private static final Log log = Logs.get();
    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysRoleService sysRoleService;
    @Inject
    private SysUnitService sysUnitService;
    @Inject
    private SLogService sLogService;
    @Inject
    private RedisService redisService;
    @Inject
    private SysMsgService sysMsgService;
    @Inject
    private ValidateService validateService;

    @At("")
    @Ok("re")
    @Filters
    public String login(HttpServletRequest req, HttpSession session) {
        return "beetl:/platform/sys/login.html";
    }

    @At("/noPermission")
    @Ok("beetl:/platform/sys/noPermission.html")
    @Filters
    public void noPermission() {

    }

    /**
     * 切换样式，对登陆用户有效
     *
     * @param theme
     * @param req
     */
    @At("/theme")
    @SaCheckLogin
    public void theme(@Param("loginTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme) && !Globals.MyConfig.getBoolean("AppDemoEnv")) {
            sysUserService.update(Chain.make("loginTheme", theme), Cnd.where("id", "=", StpUtil.getLoginIdAsString()));
            sysUserService.deleteCache(StpUtil.getLoginIdAsString());

        }
    }

    /**
     * 切换菜单位置，对登陆用户有效
     *
     * @param theme
     * @param req
     */
    @At("/menuTheme")
    @SaCheckLogin
    public void menuTheme(@Param("menuTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme) && !Globals.MyConfig.getBoolean("AppDemoEnv")) {
            sysUserService.update(Chain.make("menuTheme", theme), Cnd.where("id", "=", StpUtil.getLoginIdAsString()));
            sysUserService.deleteCache(StpUtil.getLoginIdAsString());

        }
    }

    /**
     * 切换布局，对登陆用户有效
     *
     * @param p
     * @param v
     * @param req
     */
    @At("/layout")
    @SaCheckLogin
    public void layout(@Param("p") String p, @Param("v") boolean v, HttpServletRequest req) {
        if (!Globals.MyConfig.getBoolean("AppDemoEnv")) {
            if ("sidebar".equals(p)) {
                sysUserService.update(Chain.make("loginSidebar", v), Cnd.where("id", "=", StpUtil.getLoginIdAsString()));
            } else if ("boxed".equals(p)) {
                sysUserService.update(Chain.make("loginBoxed", v), Cnd.where("id", "=", StpUtil.getLoginIdAsString()));
            } else if ("scroll".equals(p)) {
                sysUserService.update(Chain.make("loginScroll", v), Cnd.where("id", "=", StpUtil.getLoginIdAsString()));
            }
            sysUserService.deleteCache(StpUtil.getLoginIdAsString());
        }

    }

    /**
     * 用户登录
     *
     * @return
     */
    @At("/doLogin")
    @Ok("json")
    public Object doLogin(@Param("username") String username,
                          @Param("password") String password,
                          @Param("platformKey") String captchaKey,
                          @Param("platformCaptcha") String captchaCode,
                          HttpServletRequest req, HttpSession session) {
        try {
            Sys_user user = null;
            sysUserService.checkLoginname(username);
            validateService.checkCode(captchaKey, captchaCode);
            user = sysUserService.loginByPassword(username, password);
            if (user == null) {
                throw new BaseException("用户登录失败");
            }
            StpUtil.login(user.getId());
            StpUtil.checkLogin();
            String thisTokenValue = StpUtil.getTokenValue();
            StpUtil.getSession(true).set("loginname", Strings.sNull(user.getLoginname()))
                    .set("username", Strings.sNull(user.getUsername()))
                    .set("unitId", Strings.sNull(user.getUnitId()))
                    .set("unitPath", Strings.sNull(user.getUnitPath()));
            //如果启用了用户唯一登录功能
            if (Globals.MyConfig.getBoolean("SessionOnlyOne", false)) {
                try {
                    for (String token : StpUtil.getTokenValueListByLoginId(user.getId())) {
                        if (!token.equals(thisTokenValue)) {
                            StpUtil.logoutByTokenValue(token);
                            sysMsgService.offline(user.getLoginname(), token);//通知另外一个用户被踢下线
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sysUserService.setLoginInfo(user.getId(), Lang.getIP(req));
            Sys_log sysLog = new Sys_log();
            sysLog.setType("info");
            sysLog.setTag("用户登陆");
            sysLog.setSrc(this.getClass().getName() + "#doLogin");
            sysLog.setMsg("成功登录系统！");
            sysLog.setIp(Lang.getIP(req));
            sysLog.setCreatedBy(user.getId());
            sysLog.setUsername(user.getUsername());
            sysLog.setLoginname(user.getLoginname());
            sLogService.async(sysLog);
            return Result.success("login.success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 退出系统
     */
    @At
    @Ok(">>:/platform/login")
    public void logout(HttpSession session, HttpServletRequest req) {
        try {
            Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
            StpUtil.logout();
            if (user != null) {
                Sys_log sysLog = new Sys_log();
                sysLog.setType("info");
                sysLog.setTag("用户登出");
                sysLog.setSrc(this.getClass().getName() + "#logout");
                sysLog.setMsg("成功退出系统！");
                sysLog.setIp(Lang.getIP(req));
                sysLog.setCreatedBy(user.getId());
                sysLog.setUsername(user.getUsername());
                sysLog.setLoginname(user.getLoginname());
                sLogService.async(sysLog);
            }
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
    }

    @At("/captcha")
    @Ok("json")
    public Object next() {
        return Result.success(validateService.getCaptcha());
    }

    @At("/sendsms")
    @Ok("json")
    public Object sendsms(@Param("mobile") String mobile) {
        try {
            validateService.getSmsCode(mobile);
            return Result.success();
        } catch (BaseException e) {
            return Result.error(e.getMessage());
        }
    }

    @At("/doReg")
    @Ok("json")
    public Object doReg(@Param("mobile") String mobile, @Param("password") String password, @Param("code") String code, HttpServletRequest req) {
        try {
            int count = sysUserService.count(Cnd.where("loginname", "=", Strings.trim(mobile)));
            if (count > 0) {
                return Result.error("手机号已存在");
            }
            validateService.checkSMSCode(mobile, code);
            Sys_user user = new Sys_user();
            user.setLoginname(mobile);
            user.setUsername(mobile);
            user.setMobile(mobile);
            String salt = R.UU32();
            user.setSalt(salt);
            user.setPassword(PwdUtil.getPassword(password, salt));
            user.setLoginPjax(false);
            user.setLoginCount(0);
            user.setLoginTheme("palette.2.css");
            user.setLoginScroll(true);
            user.setMenuTheme("left");
            user.setCreatedBy(SecurityUtil.getUserId());
            Sys_unit unit = sysUnitService.fetch(Cnd.where("unitcode", "=", Globals.MyConfig.getString("AppDefaultUserUnit")));
            if (unit != null) {
                user.setUnitId(unit.getId());
                user.setUnitPath(unit.getPath());
            }
            Sys_role role = sysRoleService.fetch(Cnd.where("code", "=", Globals.MyConfig.getString("AppDefaultUserRole")));
            sysUserService.insert(user);
            if (role != null)
                sysRoleService.insert("sys_user_role", Chain.make("roleId", role.getId()).add("userId", user.getId()));
            sysUserService.clearCache();
            return Result.success();
        } catch (BaseException e) {
            return Result.error(e.getMessage());
        }
    }
}
