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
@At("/private/home")
@Filters({ @By(type = PrivateFilter.class) })
public class HomeController {
    private static final Log log = Logs.get();
    @At("")
    @Ok("vm:views.private.index")
    @Filters
    public void index() {

    }
}
