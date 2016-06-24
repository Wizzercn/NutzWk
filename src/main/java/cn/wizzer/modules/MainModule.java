package cn.wizzer.modules;

import org.beetl.ext.nutz.BeetlViewMaker;
import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * Created by wizzer on 2016/6/21.
 */
@Modules(scanPackage = true)
@Ok("json:full")
@Fail("http:500")
@IocBy(type=ComboIocProvider.class,args={"*json","config/ioc/","*anno","cn.wizzer","*tx","*org.nutz.integration.quartz.QuartzIocLoader"})
@Localization(value="locales/", defaultLocalizationKey="zh_CN")
@Encoding(input = "UTF-8", output = "UTF-8")
@Views({ BeetlViewMaker.class})
@SetupBy(value=MainSetup.class)
@ChainBy(args="config/chain/nutzwk-mvc-chain.json")
@SessionBy(ShiroSessionProvider.class)
public class MainModule {
}
