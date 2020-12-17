package com.budwk.app.web.commons.slog;

import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.SimpleAopMaker;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(name="$aop_syslog")
public class SLogAopConfigration extends SimpleAopMaker<SLog> {

    @Inject("refer:$ioc")
    protected Ioc ioc;

    public List<? extends MethodInterceptor> makeIt(SLog slog, Method method, Ioc ioc) {
        return Arrays.asList(new SLogAopInterceptor(ioc, slog, method));
    }

    public String[] getName() {
        return new String[0];
    }

    public boolean has(String name) {
        return false;
    }
}
