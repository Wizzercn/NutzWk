package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.filter.PrivateFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/private/home")
@Filters({@By(type = PrivateFilter.class)})
public class HomeController {
    private static final Log log = Logs.get();

    @At("")
    @Ok("beetl:/private/home.html")
    @RequiresAuthentication
    public void home() {
        //SecurityUtils.getSubject().getSession().setTimeout(1000);
    }
}
