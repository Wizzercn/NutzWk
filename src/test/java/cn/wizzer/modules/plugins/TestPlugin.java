package cn.wizzer.modules.plugins;

import cn.wizzer.common.annotation.SPlugin;
import cn.wizzer.common.plugin.AbstractPlugin;
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
    public boolean beforeInvoke(Object obj, Method method, Object... args) {
        log.debug("TestPlugin:::加载插件,初始化环境参数等等...");
        return super.beforeInvoke(obj, method, args);
    }

    @Override
    public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
        log.debug("TestPlugin:::调用后重置中间状态等等...");
        return super.afterInvoke(obj, returnObj, method, args);
    }
}
