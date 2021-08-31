package com.budwk.app.web.commons.auth.satoken.aop;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;

import java.lang.reflect.Method;

/**
 * @author wizzer@qq.com
 */
@IocBean(singleton = false)
public class SaCheckPermissionInterceptor implements MethodInterceptor {
    @Override
    public void filter(InterceptorChain chain) throws Throwable {
        Method method = chain.getCallingMethod();
        SaCheckPermission at = method.getAnnotation(SaCheckPermission.class);
        SaManager.getStpLogic(at.type()).checkByAnnotation(at);
        chain.doChain();
    }
}
