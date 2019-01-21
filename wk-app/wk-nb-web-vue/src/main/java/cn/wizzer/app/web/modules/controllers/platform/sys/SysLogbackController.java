package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.framework.base.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelCommand;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by wizzer on 2019/1/18.
 */
@IocBean
@At("/platform/sys/logback")
public class SysLogbackController {
    private static final Log log = Logs.get();
    @Inject
    private LoglevelService loglevelService;

    @At("")
    @Ok("beetl:/platform/sys/logback/index.html")
    @RequiresPermissions("sys.operation.logback")
    public void index() {
        LoglevelCommand loglevelCommand=new LoglevelCommand();
        loglevelCommand.setAction("name");
        loglevelCommand.setLevel("error");
        loglevelCommand.setName("wk-nb-dubbo-sys");
        loglevelService.changeLoglevel(loglevelCommand);
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.conf")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
