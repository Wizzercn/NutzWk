package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_unit;
import cn.wizzer.modules.sys.service.UnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/unit")
@Filters({@By(type = PrivateFilter.class)})
public class UnitAction {
    @Inject
    UnitService unitService;

    @At("")
    @Ok("vm:template.private.sys.unit.index")
    @RequiresPermissions("sys:unit")
    public Object index() {
        return unitService.query(Cnd.where("length(id)", "=", 4), null);
    }

    @At("/add")
    @Ok("vm:template.private.sys.unit.add")
    @RequiresPermissions("sys:unit")
    public void add(@Param("pid") String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            req.setAttribute("parentUnit", unitService.fetch(pid));
        }
    }

    @At("/detail/?")
    @Ok("vm:template.private.sys.unit.detail")
    @RequiresPermissions("sys:unit")
    public Object detail(String id, HttpServletRequest req) {
        Sys_unit unit = unitService.fetch(id);
        if (unit != null && unit.getId().length() > 4) {
            req.setAttribute("parentUnit", unitService.getField("name", StringUtils.getParentId(id)).getName());
        }
        return unit;
    }

    @At("/child/?")
    @Ok("vm:template.private.sys.unit.child")
    @RequiresPermissions("sys:unit")
    public Object child(String id, HttpServletRequest req) {
        return unitService.query(Cnd.where("id", "like", id + "____"), null);
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys:unit")
    public Object delete(String id, HttpServletRequest req) {
        if ("0001".equals(id)) {
            return Message.error("system.nodel", req);
        }
        if (unitService.deleteAndChild(id)) {
            return Message.success("system.success", req);
        }
        return Message.error("system.error", req);
    }
}
