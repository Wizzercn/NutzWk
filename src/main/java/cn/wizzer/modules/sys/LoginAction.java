package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.exception.IncorrectCaptchaException;
import cn.wizzer.common.mvc.filter.CaptchaFormAuthenticationFilter;
import cn.wizzer.common.util.CookieUtils;
import cn.wizzer.common.util.StringUtils;
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
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                Sys_user user = (Sys_user) subject.getPrincipals().getPrimaryPrincipal();
                user.setLoginTheme(theme);
                userService.update(Chain.make("login_theme", theme), Cnd.where("id", "=", user.getId()));
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
    @Filters(@By(type = CaptchaFormAuthenticationFilter.class))
    public Message doLogin(@Attr("loginToken") AuthenticationToken token, HttpServletRequest req) {
        try {
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            Sys_user user = (Sys_user) subject.getPrincipals().getPrimaryPrincipal();
            userService.update(Chain.make("login_ip", StringUtils.getRemoteAddr(req)).add("login_time",new Date())
                    .add("login_count",user.getLoginCount()+1)
                    , Cnd.where("id", "=", user.getId()));
            //计算左侧菜单
            List<Sys_menu> firstMenus=getChildMenus("", user.getMenus());
            Map<String,List<Sys_menu>> secondMenus=new HashMap<String, List<Sys_menu>>();
            for(Sys_menu menu:user.getMenus()){
                if(menu.getId().length()>4){
                    List<Sys_menu> s=secondMenus.get(getParentId(menu.getId()));
                    if(s==null)s=new ArrayList<Sys_menu>();
                    s.add(menu);
                    secondMenus.put(getParentId(menu.getId()),s);
                }
            }
            user.setFirstMenus(firstMenus);
            user.setSecondMenus(secondMenus);
            return Message.success("login.success", req);
        } catch (IncorrectCaptchaException e) {
            //自定义的验证码错误异常,需shrio.ini 配置authcStrategy属性，加到对应的类中
            return Message.error("login.error.captcha", req);
        } catch (LockedAccountException e) {
            return Message.error("login.error.locked", req);
        } catch (AuthenticationException e) {
            return Message.error("login.error.user", req);
        } catch (Exception e) {
            return Message.error("login.error.system", req);
        }
    }

    private List<Sys_menu> getChildMenus(String id,List<Sys_menu> menus){
        List<Sys_menu> menuList=new ArrayList<Sys_menu>();
        for(Sys_menu menu:menus){
            if(id.equals(getParentId(menu.getId()))){
                menuList.add(menu);
            }
        }
        return menuList;
    }

    private String getParentId(String id){
        if(id==null||id.length()==4){
            return "";
        }else {
            return id.substring(0,id.length()-4);
        }
    }
    /**
     * 登出系统
     */
    @At
    @Ok(">>:/private/login")
    @RequiresAuthentication
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            log.debug("Logout error", e);
        }
    }
}
