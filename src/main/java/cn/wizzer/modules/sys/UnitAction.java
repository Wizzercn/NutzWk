package cn.wizzer.modules.sys;

import cn.wizzer.common.mvc.filter.PrivateFilter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@At("/private/sys/unit")
@Filters({ @By(type = PrivateFilter.class) })
public class UnitAction {
    @At("")
    @Ok("vm:template.private.sys.unit.index")
    @RequiresPermissions("sys:unit")
    public void index(){

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
