package cn.wizzer.modules.controllers.sys;

import cn.wizzer.modules.services.sys.UserService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean // 声明为Ioc容器中的一个Bean
@At("/private") // 整个模块的路径前缀
@Ok("json:{locked:'password|createAt',ignoreNull:true}") // 忽略password和createAt属性,忽略空属性的json输出
public class LoginController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @At("/login")
    @Ok("vm:views.private.login")
    @Filters
    public void login() {
    }
}
