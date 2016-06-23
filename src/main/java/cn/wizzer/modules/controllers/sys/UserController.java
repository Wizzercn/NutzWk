package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.filter.PrivateFilter;
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
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
public class UserController {
    private static final Log log = Logs.get();

    @At
    @Ok("beetl:/private/sys/user/pass.html")
    @Filters
    public void pass() {

    }
}
