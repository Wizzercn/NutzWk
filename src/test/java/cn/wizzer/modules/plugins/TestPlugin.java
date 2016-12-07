package cn.wizzer.modules.plugins;

import cn.wizzer.common.annotation.SPlugin;
import cn.wizzer.common.plugin.AbstractPlugin;
import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.lang.reflect.Method;

/**
 * Created by Wizzer on 2016/12/5.
 */
@SPlugin(value = "first ", method = "list")
public class TestPlugin extends AbstractPlugin {
    private static final Log log = Logs.get();

    @Override
    public boolean init(Ioc ioc, String[] args) {
        log.debug("TestPlugin::插件安装成功后执行的方法,初始化资源");
        return true;
    }

    @Override
    public void destory() {
        log.debug("TestPlugin::插件卸载时执行的方法,释放资源");
    }

    @Override
    public boolean beforeInvoke(Object obj, Method method, Object... args) {
        log.debug("TestPlugin:::调用前...");
        return super.beforeInvoke(obj, method, args);
    }

    @Override
    public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
        log.debug("TestPlugin:::调用后...");
        return super.afterInvoke(obj, returnObj, method, args);
    }
}
