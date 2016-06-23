package cn.wizzer.modules.controllers.sys;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/private/home") // 整个模块的路径前缀
public class HomeController {
    private static final Log log = Logs.get();
    @At("")
    @Ok("vm:views.private.index")
    @Filters
    public void index() {

    }
}
