package cn.wizzer.modules.sys;

import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.modules.sys.bean.Sys_unit;
import cn.wizzer.modules.sys.service.UnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/unit")
@Filters({ @By(type = PrivateFilter.class) })
public class UnitAction {
    @Inject
    UnitService unitService;
    @At("")
    @Ok("vm:template.private.sys.unit.index")
    @RequiresPermissions("sys:unit")
    public Object index(){
        return unitService.query(Cnd.where("length(id)","=",4),null);
    }
    @At("/add")
    @Ok("vm:template.private.sys.unit.index")
    @RequiresPermissions("sys:unit")
    public void add(){

    }
    @At("/add/a")
    @Ok("vm:template.private.sys.unit.index")
    @RequiresPermissions("sys:unit")
    public void adda(){

    }
}
