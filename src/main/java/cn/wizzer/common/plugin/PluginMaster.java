package cn.wizzer.common.plugin;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 实验性插件管理器
 * @author wendal
 *
 */
@IocBean(depose="depose")
public class PluginMaster {

    private static final Log log = Logs.get();

    @Inject("refer:$ioc")
    protected Ioc ioc;

    /**
     * 将插件保持在一个有序Map中, TODO 换成线程安全的Map?
     */
    protected LinkedHashMap<String, IPlugin> plugins = new LinkedHashMap<>();

    /**
     * 注入一个新插件
     * @param key 插件的唯一key
     * @param plugin 插件对象
     * @param args 需要传递给插件的初始化参数
     * @return true, 如果成功的话
     */
    public boolean register(String key, IPlugin plugin, String[] args) {
        try {
            remove(key); //移除老的版本
            plugin.init(ioc, args); // 初始化插件
            log.infof("load plugin key=%s class=%s", key, plugin.getClass().getName());
            plugins.put(key, plugin); // 放入插件池
            return true;
        }
        catch (Exception e) {
            log.infof("load plugin fail key=%s class=%s", key, plugin.getClass().getName(), e);
            return false;
        }
    }

    /**
     * 通过类数据(byte数组)和类名,构建一个插件实例
     * @param className 类名
     * @param buf 类数据
     * @return 类实例
     */
    public IPlugin build(final String className, byte[] buf) {
        try {
            ByteArrayClassLoader c = new ByteArrayClassLoader();
            c._defineClass(className, buf, 0, buf.length);
            return (IPlugin) c.loadClass(className).newInstance();
        }
        catch (Exception e) {
            log.info("load plugin fail name="+className, e);
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 逐一销毁每个插件
     */
    public void depose() {
        Iterator<String> it = plugins.keySet().iterator();
        while (it.hasNext()) {
            plugins.remove(it.next());
            it.remove();
        }
    }

    /**
     * 移除特定的插件
     * @param key 插件唯一识别
     */
    public void remove(String key) {
        IPlugin plugin = plugins.get(key);
        if (plugin == null)
            return;
        log.debugf("plugin remove : %s : %s", key, plugin.getClass().getName());
        plugin.destory();
    }

    /**
     * 获取当前插件列表
     * @return
     */
    public Map<String, IPlugin> getPlugins() {
        return plugins;
    }

    /**
     * 根据一个方法动态获取所需要的插件列表
     * @param method 正准备被拦截的方法
     * @return 插件列表
     */
    public List<IPlugin> getPlugins(Method method) {
        List<IPlugin> list = new ArrayList<>();
        for (IPlugin plugin : plugins.values()) {
            if (plugin.match(method))
                list.add(plugin);
        }
        return list;
    }

    public class ByteArrayClassLoader extends ClassLoader {

        public ByteArrayClassLoader() {
            super(IPlugin.class.getClassLoader());
        }

        public void _defineClass(String name, byte[] b, int off, int len) {
            super.defineClass(name, b, off, len);
        }
    }
}
