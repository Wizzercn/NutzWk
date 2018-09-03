package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_log;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.ext.websocket.WkNotifyService;
import cn.wizzer.app.web.commons.shiro.exception.CaptchaEmptyException;
import cn.wizzer.app.web.commons.shiro.exception.CaptchaIncorrectException;
import cn.wizzer.app.web.commons.shiro.filter.PlatformAuthenticationFilter;
import cn.wizzer.app.web.commons.slog.SLogService;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.img.Images;
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
import java.awt.image.BufferedImage;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/platform/login") // 整个模块的路径前缀
@Ok("json:{locked:'password|createAt',ignoreNull:true}") // 忽略password和createAt属性,忽略空属性的json输出
public class SysLoginController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysUserService sysUserService;
    @Inject
    private SLogService sLogService;
    @Inject
    private RedisService redisService;
    @Inject
    private WkNotifyService wkNotifyService;
    @Inject("refer:shiroWebSessionManager")
    private DefaultWebSessionManager webSessionManager;

    @At("")
    @Ok("re")
    @Filters
    public String login(HttpServletRequest req, HttpSession session) {
//        RSA加密删了，需要的用的自己启用
//        try {
//            HashMap<String, Object> map = RSAUtil.getKeys();
//            //生成公钥和私钥
//            RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
//            RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
//            //模
//            String publicKeyModulus = publicKey.getModulus().toString(16);
//            //公钥指数
//            String publicKeyExponent = publicKey.getPublicExponent().toString(16);
//            //私钥指数
//            req.setAttribute("publicKeyExponent", publicKeyExponent);
//            req.setAttribute("publicKeyModulus", publicKeyModulus);
//            session.setAttribute("platformPrivateKey", privateKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "beetl:/platform/sys/login.html";

//        }
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
        if (!Strings.isEmpty(theme)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setLoginTheme(theme);
                sysUserService.update(Chain.make("loginTheme", theme), Cnd.where("id", "=", user.getId()));
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
        if (subject != null) {
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
        int errCount = 0;
        try {
            //session失效咯
            if (token == null) {
                return Result.error(7, "login.error.system");
            }
            //输错三次显示验证码窗口
            errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("platformErrCount")));
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            Sys_user user = (Sys_user) subject.getPrincipal();
            int count = user.getLoginCount() == null ? 0 : user.getLoginCount();
            //如果启用了用户唯一登录功能
            if ("true".equals(Globals.MyConfig.getOrDefault("SysUserSessionOnlyOne", "false"))) {
                try {
                    Sys_user oldUser = sysUserService.fetch(Cnd.where("id", "=", user.getId()));
                    if (oldUser != null && !Strings.sNull(oldUser.getLoginSessionId()).equals(session.getId())) {
                        Session oldSession = webSessionManager.getSessionDAO().readSession(oldUser.getLoginSessionId());
                        if (oldSession != null) {
                            wkNotifyService.offline(oldUser.getLoginname(), oldUser.getLoginSessionId());//通知另外一个用户被踢下线
                            oldSession.stop();
                            webSessionManager.getSessionDAO().delete(oldSession);
                        }
                    }
                } catch (Exception e) {

                }
            }
            sysUserService.update(Chain.make("loginIp", user.getLoginIp()).add("loginAt", Times.getTS())
                            .add("loginCount", count + 1).add("online", true).add("loginSessionId", session.getId())
                    , Cnd.where("id", "=", user.getId()));
            Sys_log sysLog = new Sys_log();
            sysLog.setType("info");
            sysLog.setTag("用户登陆");
            sysLog.setSrc(this.getClass().getName() + "#doLogin");
            sysLog.setMsg("成功登录系统！");
            sysLog.setIp(Lang.getIP(req));
            sysLog.setOpBy(user.getId());
            sysLog.setOpAt(Times.getTS());
            sysLog.setUsername(user.getUsername());
            sLogService.async(sysLog);
            return Result.success("login.success");
        } catch (CaptchaIncorrectException e) {
            //自定义的验证码错误异常
            return Result.error(1, "login.error.captcha");
        } catch (CaptchaEmptyException e) {
            //验证码为空
            return Result.error(2, "login.error.captcha");
        } catch (LockedAccountException e) {
            return Result.error(3, "login.error.locked");
        } catch (UnknownAccountException e) {
            log.error(e.getMessage(), e);
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(4, "login.error.user");
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(5, "login.error.user");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("platformErrCount", errCount);
            return Result.error(6, "login.error.system");
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
                sysLog.setOpBy(user.getId());
                sysLog.setOpAt(Times.getTS());
                sysLog.setUsername(user.getUsername());
                sLogService.async(sysLog);
            }
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
    }

    @At("/captcha")
    @Ok("raw:png")
    public BufferedImage next(HttpSession session, @Param("w") int w, @Param("h") int h) {
        if (w * h < 1) { //长或宽为0?重置为默认长宽.
            w = 200;
            h = 60;
        }
        String text = R.captchaNumber(4);
        redisService.set("platformCaptcha:" + session.getId(), text);
        redisService.expire("platformCaptcha:" + session.getId(), 300);
        return Images.createCaptcha(text, w, h, null, null, null);
    }
}
