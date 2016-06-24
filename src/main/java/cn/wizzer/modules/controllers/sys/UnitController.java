package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.services.sys.UnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/private/sys/unit")
@Filters({@By(type = PrivateFilter.class)})
public class UnitController {
    private static final Log log = Logs.get();
    @Inject
    UnitService unitService;

    @At("")
    @Ok("beetl:/private/sys/unit/index.html")
    @RequiresPermissions("sys.manager.unit")
    public Object index() {
        return unitService.query(Cnd.where("parentId", "=", "").asc("location").asc("path"));
    }

    @At("/child/*")
    @Ok("beetl:/private/sys/unit/child.html")
    @RequiresPermissions("sys.manager.unit")
    public Object child(@Param("id") String id) {
        return unitService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
    }

    @At("/detail/*")
    @Ok("beetl:/private/sys/unit/detail.html")
    @RequiresPermissions("sys.manager.unit")
    public Object detail(@Param("id") String id) {
        return unitService.fetch(id);
    }
}
