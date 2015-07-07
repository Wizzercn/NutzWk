package cn.wizzer.modules;

import cn.wizzer.common.mvc.view.VelocityViewMaker;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * Created by Wizzer.cn on 2015/6/26.
 */
@Modules(scanPackage = true)
@Ok("raw")
@Fail("http:500")
@IocBy(type=ComboIocProvider.class,args={"*json","config/ioc/","*anno","cn.wizzer","*tx","*org.nutz.integration.quartz.QuartzIocLoader"})
@Localization(value="msg/", defaultLocalizationKey="zh_CN")
@Encoding(input = "UTF-8", output = "UTF-8")
@Views({ VelocityViewMaker.class})
@SetupBy(value=MainSetup.class)
@ChainBy(args="mvc/nutzfw-mvc-chain.js")
public class MainModule {
}
