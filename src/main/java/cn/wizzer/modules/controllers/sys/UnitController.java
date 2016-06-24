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
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

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
    @RequiresPermissions("sys:xxx")
    public Object index() {
        return unitService.query(Cnd.where("parentId","=","").asc("location").asc("path"));
    }
}
