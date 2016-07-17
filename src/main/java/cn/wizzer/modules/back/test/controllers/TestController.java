package cn.wizzer.modules.back.test.controllers;

import cn.wizzer.modules.back.sys.services.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by Wizzer on 2016/7/10.
 */
@IocBean
@At("/private/test/velocity")
public class TestController {
    @Inject
    UserService userService;

    @At("")
    @Ok("vm:/private/test/velocity.html")
    @RequiresAuthentication
    public Object index() {
        return userService.query(Cnd.NEW());
    }
}
