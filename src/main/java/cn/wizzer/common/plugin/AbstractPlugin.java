package cn.wizzer.common.plugin;

import cn.wizzer.common.annotation.SPlugin;
import org.nutz.aop.interceptor.AbstractMethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author wendal
 */
public abstract class AbstractPlugin extends AbstractMethodInterceptor implements IPlugin {

    protected Ioc ioc;
    protected String[] args;
    protected SPlugin sPlugin;
    protected Pattern clazz;
    protected Pattern method;

    public boolean init(Ioc ioc, String[] args) {
        this.ioc = ioc;
        this.args = args;
        sPlugin = getClass().getAnnotation(SPlugin.class);
        if (sPlugin != null) {
            if (!Strings.isBlank(sPlugin.clazz()))
                clazz = Pattern.compile(sPlugin.clazz());
            if (!Strings.isBlank(sPlugin.method()))
                method = Pattern.compile(sPlugin.method());
        }
        return true;
    }

    public void destory() {
    }

    public boolean match(Method _method) {
        if (sPlugin == null)
            return false;
        if (clazz != null && !clazz.matcher(_method.getDeclaringClass().getName()).find())
            return false;
        if (method != null && !method.matcher(_method.getName()).find())
            return false;
        return true;
    }

}
