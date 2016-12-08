package cn.wizzer.modules.back.robot.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.back.robot.models.Rb_config;
import cn.wizzer.modules.back.robot.services.RbConfigService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

/**
 * Created by wizzer on 2016/7/23.
 */
@IocBean
@At("/private/robot/config")
@Filters({@By(type = PrivateFilter.class)})
public class RbConfigController {
    private static final Log log = Logs.get();
    @Inject
    RbConfigService rbConfigService;

    @At("")
    @Ok("beetl:/private/robot/config/index.html")
    @RequiresAuthentication
    public Object index() {
        Rb_config config = rbConfigService.fetch("robot");
        if (config == null) {
            config = new Rb_config();
            config.setId("robot");
            rbConfigService.insert(config);
        }
        return config;
    }

    @At
    @Ok("json")
    @RequiresPermissions("order.robot.robot.save")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Rb_config config) {
        try {
            rbConfigService.updateIgnoreNull(config);
            Globals.init_robot(rbConfigService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
