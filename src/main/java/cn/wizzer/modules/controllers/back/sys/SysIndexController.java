package cn.wizzer.modules.controllers.back.sys;

import cn.wizzer.common.filter.PrivateFilter;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2016/9/6.
 */
@IocBean
@At("/private/index")
@Filters({@By(type = PrivateFilter.class)})
public class SysIndexController {
    private static final Log log = Logs.get();

    @At("")
    @Ok("beetl:/private/index.html")
    @RequiresAuthentication
    public void index() {

    }
}
