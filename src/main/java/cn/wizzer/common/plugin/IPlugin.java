package cn.wizzer.common.plugin;

import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;

import java.lang.reflect.Method;

/**
 * 请继承AbstracePlugin
 *
 * @author wendal
 */
public interface IPlugin extends MethodInterceptor {

    /**
     * 初始化本插件
     */
    boolean init(Ioc ioc, String[] args);

    /**
     * 销毁本插件
     */
    void destory();

    /**
     * 当前方法是否需要本插件拦截
     *
     * @param method 准备被拦截的方法
     * @return true, 如果需要拦截
     */
    boolean match(Method method);
}
