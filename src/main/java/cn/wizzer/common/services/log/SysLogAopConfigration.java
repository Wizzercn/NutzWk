package cn.wizzer.common.services.log;

import cn.wizzer.common.annotation.SLog;
import org.nutz.aop.MethodInterceptor;
import org.nutz.aop.matcher.SimpleMethodMatcher;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.SimpleAopMaker;
import org.nutz.ioc.aop.config.AopConfigration;
import org.nutz.ioc.aop.config.InterceptorPair;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(name="$aop_syslog")
public class SysLogAopConfigration extends SimpleAopMaker<SLog> {

    @Inject
    protected SysLogService sysLogService;

    public List<? extends MethodInterceptor> makeIt(SLog slog, Method method, Ioc ioc) {
        return Arrays.asList(new SysLogAopInterceptor(sysLogService, slog, method));
    }

    public String[] getName() {
        return new String[0];
    }

    public boolean has(String name) {
        return false;
    }
}
