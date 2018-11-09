package cn.wizzer.app.web.commons.core;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.ext.pubsub.WebPubSub;

import javax.servlet.ServletContext;

import org.nutz.boot.NbApp;
import org.nutz.integration.jedis.JedisAgent;
import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

/**
 * Created by wizzer on 2018/3/16.
 */
@IocBean(create = "init", depose = "depose")
@Modules(packages = "cn.wizzer")
@Localization(value = "locales/", defaultLocalizationKey = "zh_CN")
@Encoding(input = "UTF-8", output = "UTF-8")
@ChainBy(args = "chain/nutzwk-mvc-chain.json")
@SessionBy(ShiroSessionProvider.class)
public class WebPlatformMainLauncher {
    private static final Log log = Logs.get();
    @Inject("refer:$ioc")
    private Ioc ioc;
    @Inject
    private PropertiesProxy conf;
    @Inject
    private JedisAgent jedisAgent;
    @Inject
    private Globals globals;//注入一下为了初始化
    @Inject
    private WebPubSub webPubSub;//注入一下为了初始化

    public static void main(String[] args) throws Exception {
        NbApp nb = new NbApp().setArgs(args).setPrintProcDoc(true);
        nb.getAppContext().setMainPackage("cn.wizzer");
        nb.run();
    }
    
    public static NbApp warMain(ServletContext sc) {
        NbApp nb = new NbApp().setPrintProcDoc(true);
        nb.getAppContext().setMainPackage("cn.wizzer");
        return nb;
    }

    public void init() {
        Mvcs.X_POWERED_BY="nutzwk 5.1.x <wizzer.cn>";
    }

    public void depose() {

    }
}
