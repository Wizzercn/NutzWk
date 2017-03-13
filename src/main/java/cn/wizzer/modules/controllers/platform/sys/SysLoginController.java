package cn.wizzer.modules.controllers.platform.sys;

import cn.apiclub.captcha.Captcha;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.util.StringUtil;
import cn.wizzer.common.services.log.SLogService;
import cn.wizzer.common.shiro.exception.EmptyCaptchaException;
import cn.wizzer.common.shiro.exception.IncorrectCaptchaException;
import cn.wizzer.common.shiro.filter.AuthenticationFilter;
import cn.wizzer.modules.models.sys.Sys_log;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.modules.services.sys.SysUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
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
    SysUserService userService;
    @Inject
    SLogService sLogService;

    @At("")
    @Ok("re")
    @Filters
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/platform/home";
        } else {
            return "beetl:/platform/sys/login.html";
        }
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
     * @RequiresUser 记住我有效
     * @RequiresAuthentication 就算记住我也需要重新验证身份
     */
    @At("/theme")
    @RequiresAuthentication
    public void theme(@Param("loginTheme") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setLoginTheme(theme);
                userService.update(Chain.make("loginTheme", theme), Cnd.where("id", "=", user.getId()));
            }
        }
    }

    /**
     * 切换布局，对登陆用户有效
     *
     * @param p
     * @param v
     * @param req
     * @RequiresUser 记住我有效
     * @RequiresAuthentication 就算记住我也需要重新验证身份
     */
    @At("/layout")
    @RequiresAuthentication
    public void layout(@Param("p") String p, @Param("v") boolean v, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("sidebar".equals(p)) {
                userService.update(Chain.make("loginSidebar", v), Cnd.where("id", "=", user.getId()));
                user.setLoginSidebar(v);
            } else if ("boxed".equals(p)) {
                userService.update(Chain.make("loginBoxed", v), Cnd.where("id", "=", user.getId()));
                user.setLoginBoxed(v);
            } else if ("scroll".equals(p)) {
                userService.update(Chain.make("loginScroll", v), Cnd.where("id", "=", user.getId()));
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
    @Filters(@By(type = AuthenticationFilter.class))
    public Object doLogin(@Attr("loginToken") AuthenticationToken token, HttpServletRequest req, HttpSession session) {
        int errCount = 0;
        try {
            //输错三次显示验证码窗口
            errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("errCount")));
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            Sys_user user = (Sys_user) subject.getPrincipal();
            int count = user.getLoginCount() == null ? 0 : user.getLoginCount();
            userService.update(Chain.make("loginIp", user.getLoginIp()).add("loginAt", (int) (System.currentTimeMillis() / 1000))
                            .add("loginCount", count + 1).add("isOnline", true)
                    , Cnd.where("id", "=", user.getId()));
			Sys_log sysLog = new Sys_log();
            sysLog.setType("info");
            sysLog.setTag("用户登陆");
            sysLog.setSrc(this.getClass().getName()+"#doLogin");
            sysLog.setMsg("成功登录系统！");
            sysLog.setIp(StringUtil.getRemoteAddr());
            sysLog.setOpBy(user.getId());
            sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysLog.setNickname(user.getNickname());
            sLogService.async(sysLog);
            return Result.success("login.success");
        } catch (IncorrectCaptchaException e) {
            //自定义的验证码错误异常
            return Result.error(1, "login.error.captcha");
        } catch (EmptyCaptchaException e) {
            //验证码为空
            return Result.error(2, "login.error.captcha");
        } catch (LockedAccountException e) {
            return Result.error(3, "login.error.locked");
        } catch (UnknownAccountException e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("errCount", errCount);
            return Result.error(4, "login.error.user");
        } catch (AuthenticationException e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("errCount", errCount);
            return Result.error(5, "login.error.user");
        } catch (Exception e) {
            errCount++;
            SecurityUtils.getSubject().getSession(true).setAttribute("errCount", errCount);
            return Result.error(6, "login.error.system");
        }
    }

    /**
     * 退出系统
     */
    @At
    @Ok(">>:/platform/login")
    public void logout(HttpSession session) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            currentUser.logout();
            Sys_log sysLog = new Sys_log();
            sysLog.setType("info");
            sysLog.setTag("用户登出");
            sysLog.setSrc(this.getClass().getName()+"#logout");
            sysLog.setMsg("成功退出系统！");
            sysLog.setIp(StringUtil.getRemoteAddr());
            sysLog.setOpBy(user.getId());
            sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysLog.setNickname(user.getNickname());
            sLogService.async(sysLog);
            userService.update(Chain.make("isOnline", false), Cnd.where("id", "=", user.getId()));
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
        Captcha captcha = new Captcha.Builder(w, h)
                .addText()
//								.addBackground(new GradiatedBackgroundProducer())
//								.addNoise(new StraightLineNoiseProducer()).addBorder()
//								.gimp(new FishEyeGimpyRenderer())
                .build();
        String text = captcha.getAnswer();
        session.setAttribute("captcha", text);
        return captcha.getImage();
    }
}
