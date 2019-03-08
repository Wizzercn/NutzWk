package cn.wizzer.app.web.modules.controllers.open;

import cn.wizzer.app.web.commons.filter.ApiMoSignFilter;
import cn.wizzer.framework.base.Result;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

/**
 * 应用管理服务端接口
 * Created by wizzer on 2019/3/8.
 */
@IocBean
@At("/open/api/mo")
@Filters({@By(type = ApiMoSignFilter.class)})
public class ApiMoController {
    private final static Log log = Logs.get();

    @At("/task")
    @Ok("json")
    @POST
    public Object task(@Param("hostName") String hostName) {

        return Result.success();
    }
}
