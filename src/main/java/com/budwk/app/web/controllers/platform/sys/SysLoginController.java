package com.budwk.app.web.controllers.platform.sys;

import com.budwk.app.base.result.Result;
import com.budwk.app.sys.models.Sys_log;
import com.budwk.app.sys.models.Sys_role;
import com.budwk.app.sys.models.Sys_unit;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysMsgService;
import com.budwk.app.sys.services.SysRoleService;
import com.budwk.app.sys.services.SysUnitService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.exception.CaptchaException;
import com.budwk.app.web.commons.exception.SmsException;
import com.budwk.app.web.commons.ext.validate.ValidateService;
import com.budwk.app.web.commons.shiro.filter.PlatformAuthenticationFilter;
import com.budwk.app.web.commons.slog.SLogService;
import com.budwk.app.web.commons.utils.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

;

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
    @Inject("refer:shiroWebSessionManager")
    private DefaultWebSessionManager webSessionManager;
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
    @RequiresAuthentication
    public void theme(@Param("loginTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme) && !Globals.MyConfig.getBoolean("AppDemoEnv")) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setLoginTheme(theme);
                sysUserService.update(Chain.make("loginTheme", theme), Cnd.where("id", "=", user.getId()));
                sysUserService.deleteCache(user.getId());
            }
        }
    }

    /**
     * 切换菜单位置，对登陆用户有效
     *
     * @param theme
     * @param req
     */
    @At("/menuTheme")
    @RequiresAuthentication
    public void menuTheme(@Param("menuTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme) && !Globals.MyConfig.getBoolean("AppDemoEnv")) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setMenuTheme(theme);
                sysUserService.update(Chain.make("menuTheme", theme), Cnd.where("id", "=", user.getId()));
                sysUserService.deleteCache(user.getId());
            }
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
    @RequiresAuthentication
    public void layout(@Param("p") String p, @Param("v") boolean v, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && !Globals.MyConfig.getBoolean("AppDemoEnv")) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("sidebar".equals(p)) {
                sysUserService.update(Chain.make("loginSidebar", v), Cnd.where("id", "=", user.getId()));
                user.setLoginSidebar(v);
            } else if ("boxed".equals(p)) {
                sysUserService.update(Chain.make("loginBoxed", v), Cnd.where("id", "=", user.getId()));
                user.setLoginBoxed(v);
            } else if ("scroll".equals(p)) {
                sysUserService.update(Chain.make("loginScroll", v), Cnd.where("id", "=", user.getId()));
                user.setLoginScroll(v);
            }
            sysUserService.deleteCache(user.getId());
        }

    }

    /**
     * 登陆验证
     *
     * @param token
     * @param req
     * @return
     */
    @At("/doLogin")
    @Ok("json")
    @Filters(@By(type = PlatformAuthenticationFilter.class))
    public Object doLogin(@Attr("platformLoginToken") AuthenticationToken token, HttpServletRequest req, HttpSession session) {
        try {
            //session失效咯
            if (token == null) {
                return Result.error("login.error.system");
            }
            //输错三次显示验证码窗口
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            Sys_user user = (Sys_user) subject.getPrincipal();
            int count = user.getLoginCount() == null ? 0 : user.getLoginCount();
            //如果启用了用户唯一登录功能
            if ("true".equals(Globals.MyConfig.getOrDefault("SessionOnlyOne", "false"))) {
                try {
                    Sys_user oldUser = sysUserService.fetch(Cnd.where("id", "=", user.getId()));
                    if (oldUser != null && !Strings.sNull(oldUser.getLoginSessionId()).equals(session.getId())) {
                        Session oldSession = webSessionManager.getSessionDAO().readSession(oldUser.getLoginSessionId());
                        if (oldSession != null) {
                            sysMsgService.offline(oldUser.getLoginname(), oldUser.getLoginSessionId());//通知另外一个用户被踢下线
                            oldSession.stop();
                            webSessionManager.getSessionDAO().delete(oldSession);
                        }
                    }
                } catch (Exception e) {

                }
            }
            sysUserService.update(Chain.make("loginIp", user.getLoginIp()).add("loginAt", Times.getTS())
                            .add("loginCount", count + 1).add("userOnline", true).add("loginSessionId", session.getId())
                    , Cnd.where("id", "=", user.getId()));
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
        } catch (IncorrectCredentialsException e) {
            return Result.error("密码错误");
        } catch (Exception e) {
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
            Subject currentUser = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            currentUser.logout();
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
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
    }

    @At("/captcha")
    @Ok("json")
    public Object next() {
        return Result.success(validateService.getCode());
    }

    @At("/sendsms")
    @Ok("json")
    public Object sendsms(@Param("mobile") String mobile) {
        try {
            validateService.getSMSCode(mobile);
            return Result.success();
        } catch (SmsException e) {
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
            user.setPassword(new Sha256Hash(password, ByteSource.Util.bytes(salt), 1024).toHex());
            user.setLoginPjax(false);
            user.setLoginCount(0);
            user.setLoginTheme("palette.2.css");
            user.setLoginScroll(true);
            user.setMenuTheme("left");
            user.setCreatedBy(ShiroUtil.getPlatformUid());
            Sys_unit unit = sysUnitService.fetch(Cnd.where("unitcode", "=", Globals.MyConfig.getString("AppDefaultUserUnit")));
            if (unit != null)
                user.setUnitid(unit.getId());
            Sys_role role = sysRoleService.fetch(Cnd.where("code", "=", Globals.MyConfig.getString("AppDefaultUserRole")));
            sysUserService.insert(user);
            if (role != null)
                sysRoleService.insert("sys_user_role", Chain.make("roleId", role.getId()).add("userId", user.getId()));
            sysUserService.clearCache();
            return Result.success();
        } catch (CaptchaException e) {
            return Result.error(e.getMessage());
        }
    }
}
