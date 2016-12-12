package cn.wizzer.common.plugin;

import org.nutz.aop.matcher.SimpleMethodMatcher;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.aop.config.AopConfigration;
import org.nutz.ioc.aop.config.InterceptorPair;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数SimpleAopMaker实现动态加载Aop拦截器
 *
 * @author wendal
 */
@IocBean(name = "$aop_1_plugin") // 这个ioc名称,会被自动加载为aop配置
public class PluginAopConfigure implements AopConfigration {

    private static final Log log = Logs.get();

    @Inject
    private DynamicInterceptor dynamicInterceptor;

    public List<InterceptorPair> getInterceptorPairList(Ioc ioc, Class<?> clazz) {
        if (!checkClass(clazz) || !clazz.getName().startsWith("cn.wizzer.modules.plugins") ||
                clazz.getName().startsWith(getClass().getPackage().getName())) {
            log.debug("skip -- " + clazz.getName());
            return null;
        }
        List<InterceptorPair> list = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!checkMethod(method))
                continue;
            list.add(new InterceptorPair(dynamicInterceptor, new SimpleMethodMatcher(method)));
        }
        if (list.isEmpty())
            return null;
        return list;
    }

    /**
     * 弱弱过滤一下不靠谱的方法
     */
    public boolean checkMethod(Method method) {
        int mod = method.getModifiers();
        if (mod == 0
                || Modifier.isStatic(mod)
                || Modifier.isPrivate(mod)
                || Modifier.isFinal(mod)
                || Modifier.isAbstract(mod))
            return false;
        return true;
    }


    /**
     * 弱弱过滤一下不靠谱的类
     *
     * @param klass
     * @return
     */
    public boolean checkClass(Class<?> klass) {
        return !(klass.isInterface()
                || klass.isArray()
                || klass.isEnum()
                || klass.isPrimitive()
                || klass.isMemberClass()
                || klass.isAnnotation()
                || klass.isAnonymousClass());
    }
}
