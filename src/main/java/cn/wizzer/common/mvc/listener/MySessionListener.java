package cn.wizzer.common.mvc.listener;

import cn.wizzer.common.service.log.SysLogService;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_log;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Wizzer.cn on 2015/7/15.
 */
public class MySessionListener implements HttpSessionListener {
    SysLogService sysLogService;
    UserService userService;

    public void sessionCreated(HttpSessionEvent event) {
    }

    /**
     * 记录用户登出日志并改变在线状态
     *
     * @param event
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        String username = null, uid = null;
        Object u = SecurityUtils.getSubject().getPrincipal();
        if (u != null) {
            Sys_user user = (Sys_user) u;
            uid = user.getId();
            username = user.getUsername();
        }
        if (Strings.isEmpty(uid))
            return;
        if (sysLogService == null) {
            sysLogService = Mvcs.ctx().getDefaultIoc().get(SysLogService.class);
        }
        if (userService == null) {
            userService = Mvcs.ctx().getDefaultIoc().get(UserService.class);
        }
        Sys_log syslog = Sys_log.c("system", "用户退出", uid, username, "用户：" + username + " 超时，自动退出系统！", null);
        sysLogService.async(syslog);
        userService.update(Chain.make("is_online", false), Cnd.where("id", "=", uid));
    }
}
