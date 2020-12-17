package com.budwk.app.web.commons.shiro.listener;

import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.base.Globals;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2018/7/5.
 */
@IocBean
public class WebSessionListener implements SessionListener {
    @Inject
    private SysUserService sysUserService;

    @Override
    public void onStart(Session session) {//会话创建触发 已进入shiro过滤器的会话就触发这个方法

    }

    @Override
    public void onStop(Session session) {//退出
        if ("true".equals(Globals.MyConfig.getOrDefault("SessionOnlyOne", "false"))) {
            if (Strings.isNotBlank(Strings.sNull(session.getAttribute("platform_loginname")))) {
                //这里不能使用StringUtil.getPlatformLoginname 方法,因为那会创建新的会话
                sysUserService.update(Chain.make("userOnline", false), Cnd.where("loginname", "=", Strings.sNull(session.getAttribute("platform_loginname"))));
            }
        }
    }

    @Override
    public void onExpiration(Session session) {//会话过期时触发
        if ("true".equals(Globals.MyConfig.getOrDefault("SessionOnlyOne", "false"))) {
            if (Strings.isNotBlank(Strings.sNull(session.getAttribute("platform_loginname")))) {
                //这里不能使用StringUtil.getPlatformLoginname 方法,因为那会创建新的会话
                sysUserService.update(Chain.make("userOnline", false), Cnd.where("loginname", "=", Strings.sNull(session.getAttribute("platform_loginname"))));
            }
        }
    }

}
