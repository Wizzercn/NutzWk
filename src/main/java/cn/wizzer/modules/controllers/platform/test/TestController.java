package cn.wizzer.modules.controllers.platform.test;

import cn.wizzer.modules.services.sys.SysUserService;
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
@At("/platform/test/velocity")
public class TestController {
    @Inject
    SysUserService sysUserService;

    @At("")
    @Ok("vm:/platform/test/velocity.html")
    @RequiresAuthentication
    public Object index() {
        return sysUserService.query(Cnd.NEW());
    }
}
