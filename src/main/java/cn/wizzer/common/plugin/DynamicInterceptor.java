package cn.wizzer.common.plugin;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

/**
 * 通过PluginMaster获取当前方法所需要的额外拦截器(插件)列表
 *
 * @author wendal
 */
@IocBean
public class DynamicInterceptor implements MethodInterceptor {

    /**
     * 注入Ioc容器,目的是为了懒加载PluginMaster
     */
    @Inject("refer:$ioc")
    private Ioc ioc;

    /**
     * 真正使用的时候才加载,不要注入
     */
    private PluginMaster master;

    public void filter(InterceptorChain chain) throws Throwable {
        if (master == null)
            master = ioc.get(PluginMaster.class);
        // 获取插件列表
        List<IPlugin> its = master.getPlugins(chain.getCallingMethod());
        if (its == null || its.isEmpty()) {
            // 木有生效的插件,886,直接执行了
            chain.doChain();
        } else {
            // 恩,代理之
            new InterceptorChainProxy(chain, its.iterator()).doChain();
        }
    }
}
