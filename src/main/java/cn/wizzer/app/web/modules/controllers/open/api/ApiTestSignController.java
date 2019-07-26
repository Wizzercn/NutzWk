package cn.wizzer.app.web.modules.controllers.open.api;

import cn.wizzer.app.web.commons.filter.ApiSignFilter;
import cn.wizzer.framework.base.Result;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

/**
 * Created by wizzer on 2018/4/4.
 */
@IocBean
@At("/open/api/test")
@Filters({@By(type = ApiSignFilter.class)})
public class ApiTestSignController {
    private final static Log log = Logs.get();

    @At("/test2")
    @Ok("json")
    @POST
    public Object test2(@Param("openid") String openid) {
        return Result.success("执行成功","openid:::"+openid);
    }
}
