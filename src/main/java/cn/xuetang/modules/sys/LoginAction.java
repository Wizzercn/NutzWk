package cn.xuetang.modules.sys;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.common.util.OnlineUtil;
import cn.xuetang.modules.sys.bean.Sys_safeconfig;
import cn.xuetang.modules.sys.bean.Sys_user_log;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.modules.sys.bean.Sys_unit;
import cn.xuetang.modules.sys.bean.Sys_user;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
@IocBean
@At("/private")
@Filters({@By(type = GlobalsFilter.class)})
public class LoginAction extends BaseAction {
    @Inject
    protected Dao dao;

    @At("/doLogin")
    public String login(@Param("loginname") String loginname,
                        @Param("password") String password,
                        @Param("verifcode") String verifcode, HttpSession session,
                        HttpServletRequest req) {
        if (Strings.isBlank(loginname) || Strings.isBlank(password))
            return "用户名及密码不能为空！";
        String vcode = Strings.sNull(session
                .getAttribute("ValidateCode"));
		if (!vcode.equals(verifcode))
			return "验证码不正确！";
        Sys_user user = daoCtl.detailByName(dao, Sys_user.class, "loginname", loginname);
        if (user == null)
            return "用户名不存在！";
        if (user.getState() == 1)
            return "用户被禁止登陆。请联系管理员！";
        String hashedPasswordBase64 = new Sha256Hash(password, user.getSalt(), 1024).toBase64();
        if (!hashedPasswordBase64.equals(user.getPassword())) {
            int all = 5;
            int count = NumberUtils.toInt(Strings.sNull(session
                    .getAttribute("errorlogincount")), 0);
            if (count > 4) {
                if (user.getState() == 0)
                    daoCtl.update(dao, Sys_user.class, Chain.make("state", 1),
                            Cnd.where("loginname", "=", user.getLoginname()));
                session.removeAttribute("errorlogincount");
                return "密码输错次数过多，用户已被禁用！";
            } else {
                session.setAttribute("errorlogincount", count + 1);
            }

            all = all - count;
            return "密码不正确，你还有" + all + "次机会！";
        }
        String ip = getIpAddr(req);

        Sys_safeconfig safe = daoCtl.detailByName(dao, Sys_safeconfig.class, "state", 0);

        if (safe != null) {
            if (safe.getType() == 0) // 拒绝登陆IP
            {
                if (safe.getNote() != null && safe.getNote().contains(ip)) {
                    return "用户当前IP地址被禁止登陆。";
                }
            } else // 允许登陆IP
            {
                if (safe.getNote() != null && safe.getNote().contains(ip)) {
                    return "用户当前IP地址被禁止登陆。";
                }
            }
        }
        Sys_unit u = daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid());
        if (u != null)
            user.setUnitname(u.getName());
        user.setLogincount(user.getLogincount() + 1);
        user.setLoginip(ip);
        user.setLogintime(DateUtil.getCurDateTime());
        user.setLogintype(0);
        daoCtl.update(dao, user);
        Sys_user_log log = new Sys_user_log();
        log.setUserid(user.getUid());
        log.setLoginip(ip);
        log.setType(0);
        log.setLoginname(user.getLoginname());
        log.setReaalname(user.getRealname());
        log.setLogintime(user.getLogintime());
        daoCtl.add(dao, log);
        session.setAttribute("userSession", user);
        session.setAttribute("validate", "");

        return "true";

    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @At
    @Ok(">>:/private/login")
    public void logout(HttpSession session) {
        session.removeAttribute("userSession");
    }

    @At
    @Ok("vm:template.private.login")
    public void login() {

    }

    @At
    @Ok("raw")
    public int Online(@Param("loginname") String loginname, HttpSession session) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        if (user == null) {
            return -2;
        }
        if (loginname != null && !"".equals(loginname)) {
            OnlineUtil.addUser(loginname, String.valueOf(1));
        }
        return OnlineUtil.getOnlineCount(String.valueOf(1));
    }

}
