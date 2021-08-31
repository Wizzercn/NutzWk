package com.budwk.app.web.commons.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * @author wizzer@qq.com
 */
@IocBean
@Slf4j
public class AuthService {
    @Inject
    private SysUserService sysUserService;

    public Sys_user getLogonUser(){
        return sysUserService.getUserAndMenuById(SecurityUtil.getUserId());
    }

    public Object getPrincipalProperty(String property) {
        Sys_user user = getLogonUser();
        if (user != null) {
            try {
                BeanInfo bi = Introspector.getBeanInfo(user.getClass());
                for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                    if (pd.getName().equals(property)) {
                        return pd.getReadMethod().invoke(user, (Object[]) null);
                    }
                }
                log.trace("Property [{}] not found in principal of type [{}]", property,
                        user.getClass().getName());
            } catch (Exception e) {
                log.trace("Error reading property [{}] from principal of type [{}]", property,
                        user.getClass().getName());
            }
        }
        return null;
    }

    public String getSessionId(){
        return StpUtil.getTokenValue();
    }
}
