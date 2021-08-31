package com.budwk.app.web.commons.auth.satoken.aop;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.config.AopConfigration;
import org.nutz.ioc.aop.config.InterceptorPair;
import org.nutz.ioc.loader.annotation.IocBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wizzer@qq.com
 */
@IocBean(name = "$aop_satoken")
public class SaTokenAopConfigration implements AopConfigration {

    @Override
    public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
        List<InterceptorPair> list = new ArrayList<InterceptorPair>();
        boolean flag = true;
        for (Method method : clazz.getMethods()) {
            if (method.getAnnotation(SaCheckLogin.class) != null
                    || method.getAnnotation(SaCheckRole.class) != null
                    || method.getAnnotation(SaCheckPermission.class) != null) {
                flag = false;
                break;
            }
        }
        if (flag)
            return list;
        list.add(new InterceptorPair(ioc.get(SaCheckLoginInterceptor.class),
                new SaTokenMethodMatcher(SaCheckLogin.class)));
        list.add(new InterceptorPair(ioc.get(SaCheckRoleInterceptor.class),
                new SaTokenMethodMatcher(SaCheckRole.class)));
        list.add(new InterceptorPair(ioc.get(SaCheckPermissionInterceptor.class),
                new SaTokenMethodMatcher(SaCheckPermission.class)));
        return list;
    }
}
