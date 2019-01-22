package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.framework.base.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelCommand;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelService;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wizzer on 2019/1/18.
 */
@IocBean
@At("/platform/sys/monitor")
public class SysMonitorController {
    private static final Log log = Logs.get();
    @Inject
    private LoglevelService loglevelService;
    @Inject
    private RedisService redisService;

    @At("")
    @Ok("beetl:/platform/sys/monitor/index.html")
    @RequiresPermissions("sys.operation.monitor")
    public void index() {
//        LoglevelCommand loglevelCommand = new LoglevelCommand();
//        loglevelCommand.setAction("name");
//        loglevelCommand.setLevel("error");
//        loglevelCommand.setName("wk-nb-dubbo-sys");
//        loglevelService.changeLoglevel(loglevelCommand);
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.operation.monitor")
    public Object data() {
        try {
            return Result.success().addData(loglevelService.getData());
        } catch (Exception e) {
            return Result.error();
        }
    }
}
