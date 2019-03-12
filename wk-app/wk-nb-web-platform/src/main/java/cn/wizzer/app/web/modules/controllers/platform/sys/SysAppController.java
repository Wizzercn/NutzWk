package cn.wizzer.app.web.modules.controllers.platform.sys;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2019/3/12.
 */
@IocBean
@At("/platform/sys/app")
public class SysAppController {
    private static final Log log = Logs.get();

    @At("")
    @Ok("beetl:/platform/sys/app/index.html")
    public void index() {

    }

}
