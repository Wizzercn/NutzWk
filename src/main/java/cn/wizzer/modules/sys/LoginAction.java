package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.exception.IncorrectCaptchaException;
import cn.wizzer.common.exception.IncorrectIpException;
import cn.wizzer.common.mvc.filter.CaptchaFormAuthenticationFilter;
import cn.wizzer.common.service.log.SysLogService;
import cn.wizzer.common.util.CookieUtils;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_log;
import cn.wizzer.modules.sys.bean.Sys_menu;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.nutz.dao.*;
import org.nutz.dao.Chain;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/private") // 整个模块的路径前缀
@Ok("json:{locked:'password|salt',ignoreNull:true}") // 忽略password和salt属性,忽略空属性的json输出
public class LoginAction {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    SysLogService sysLogService;
    /**
     * 登陆页面
     */
    @At("/login")
    @Ok("vm:template.private.login")
    @Filters
    public void login() {
    }

    /**
     * 切换语言
     *
     * @param language
     * @param req
     * @param rep
     * @return
     */
    @At("/language/?")
    @Ok(">>:${obj}")
    public String language(String language, HttpServletRequest req, HttpServletResponse rep) {
        if (!Strings.isEmpty(language)) {
            Mvcs.setLocalizationKey(language);
            CookieUtils.setCookie(rep, "language", language, 60 * 60 * 24 * 365);
        }
        return req.getHeader("Referer") == null ? "/private/login" : req.getHeader("Referer");
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
    @RequiresUser
    public void theme(@Param("path") String theme, HttpServletRequest req) {
        if (!Strings.isEmpty(theme)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Sys_user user = (Sys_user) subject.getPrincipal();
                user.setLoginTheme(theme);
                userService.update(Chain.make("login_theme", theme), Cnd.where("id", "=", user.getId()));
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
    @RequiresUser
    public void layout(@Param("p") String p, @Param("v") boolean v, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("sidebar".equals(p)) {
                userService.update(Chain.make("login_sidebar", v), Cnd.where("id", "=", user.getId()));
                user.setLoginSidebar(v);
            } else if ("boxed".equals(p)) {
                userService.update(Chain.make("login_boxed", v), Cnd.where("id", "=", user.getId()));
                user.setLoginBoxed(v);
            } else if ("scroll".equals(p)) {
                userService.update(Chain.make("login_scroll", v), Cnd.where("id", "=", user.getId()));
                user.setLoginScroll(v);
            }
        }

    }

    /**
     * 读取用户头像
     *
     * @param req
     * @return
     * @throws SQLException
     */
    @RequiresUser
    @Ok("raw:jpg")
    @At("/avatar")
    @GET
    public Object avatar(HttpServletRequest req) throws SQLException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (user.getProfile() != null && user.getProfile().getAvatar() != null) {
                return user.getProfile().getAvatar();
            }
        }
        return new File(req.getServletContext().getRealPath("/include/img/man.png"));
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
    @Filters(@By(type = CaptchaFormAuthenticationFilter.class))
    public Object doLogin(@Attr("loginToken") AuthenticationToken token, HttpServletRequest req,HttpSession session) {
        try {
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            Sys_user user = (Sys_user) subject.getPrincipal();
            user.setLoginIp(StringUtils.getRemoteAddr());
            //计算左侧菜单
            List<Sys_menu> firstMenus =new ArrayList<>();
            Map<String, List<Sys_menu>> secondMenus = new HashMap<>();
            for (Sys_menu menu : user.getMenus()) {
                if (menu.getPath().length() > 4) {
                    List<Sys_menu> s = secondMenus.get(getParentId(menu.getPath()));
                    if (s == null) s = new ArrayList<>();
                    s.add(menu);
                    secondMenus.put(getParentId(menu.getPath()), s);
                }else if (menu.getPath().length()==4) {
                    firstMenus.add(menu);
                }
            }
            user.setFirstMenus(firstMenus);
            user.setSecondMenus(secondMenus);
            user.setProfile(userService.getProfile(user.getId()));
            Sys_log log= Sys_log.c("info","用户登陆",user.getId(),user.getUsername(),"用户："+user.getUsername()+" 成功登陆系统！",null);
            sysLogService.async(log);
            userService.update(Chain.make("login_ip", user.getLoginIp()).add("login_time", new Date())
                    .add("login_count", user.getLoginCount() + 1).add("is_online",true)
                    , Cnd.where("id", "=", user.getId()));
            return Message.success("login.success", req);
        } catch (IncorrectCaptchaException e) {
            //自定义的验证码错误异常,需shrio.ini 配置authcStrategy属性，加到对应的类中
            return Message.error("login.error.captcha", req);
        } catch (IncorrectIpException e) {
            //IP异常返回信息，其实就是验证码为空
            return new NutMap().setv("type", "iperror").setv("content", "IP is error");
        } catch (LockedAccountException e) {
            return Message.error("login.error.locked", req);
        } catch (AuthenticationException e) {
            return Message.error("login.error.user", req);
        } catch (Exception e) {
            return Message.error("login.error.system", req);
        }
    }

    /**
     * 得到父菜单ID
     * @param id
     * @return
     */
    private String getParentId(String id) {
        if (id == null || id.length() == 4) {
            return "";
        } else {
            return id.substring(0, id.length() - 4);
        }
    }

    /**
     * 登出系统
     */
    @At
    @Ok(">>:/private/login")
    public void logout(HttpSession session) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Sys_user user=(Sys_user)currentUser.getPrincipal();
            Sys_log log= Sys_log.c("info","用户退出",user.getId(),user.getUsername(),"用户："+user.getUsername()+" 手动退出系统！",null);
            sysLogService.async(log);
            userService.update(Chain.make("is_online", false), Cnd.where("id", "=", user.getId()));
            session.setAttribute("logout","true");
            currentUser.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
    }
}
