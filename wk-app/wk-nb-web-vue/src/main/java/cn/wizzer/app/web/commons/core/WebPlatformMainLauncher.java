package cn.wizzer.app.web.commons.core;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.ext.pubsub.WebPubSub;
import cn.wizzer.app.web.modules.tags.*;
import org.beetl.core.GroupTemplate;
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

import javax.servlet.ServletContext;

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
    @Inject
    private GroupTemplate groupTemplate;

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
        Mvcs.X_POWERED_BY = "nutzwk 5.2.x <wizzer.cn>";
        Globals.AppBase = Mvcs.getServletContext().getContextPath();
        Globals.AppRoot = Mvcs.getServletContext().getRealPath("/");
        //注册自定义标签
        groupTemplate.registerTagFactory("cms_channel_list", () -> ioc.get(CmsChannelListTag.class));
        groupTemplate.registerTagFactory("cms_channel", () -> ioc.get(CmsChannelTag.class));
        groupTemplate.registerTagFactory("cms_article_list", () -> ioc.get(CmsArticleListTag.class));
        groupTemplate.registerTagFactory("cms_article", () -> ioc.get(CmsArticleTag.class));
        groupTemplate.registerTagFactory("cms_link_list", () -> ioc.get(CmsLinkListTag.class));

    }

    public void depose() {

    }
}
