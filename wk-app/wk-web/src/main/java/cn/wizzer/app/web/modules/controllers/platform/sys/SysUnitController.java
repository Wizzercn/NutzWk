package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysUnitService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.util.ShiroUtil;
import cn.wizzer.framework.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/platform/sys/unit")
public class SysUnitController {
    private static final Log log = Logs.get();
    @Inject
    private SysUnitService sysUnitService;
    @Inject
    private ShiroUtil shiroUtil;

    @At("")
    @Ok("beetl:/platform/sys/unit/index.html")
    @RequiresPermissions("sys.manager.unit")
    public Object index() {
        if (shiroUtil.hasRole("sysadmin")) {
            return sysUnitService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("path"));
        }
        Sys_user user = (Sys_user) shiroUtil.getPrincipal();
        if (user != null) {
            return sysUnitService.query(Cnd.where("id", "=", user.getUnitid()).asc("path"));
        }
        return new ArrayList<>();
    }

    @At
    @Ok("beetl:/platform/sys/unit/add.html")
    @RequiresPermissions("sys.manager.unit")
    public Object add(@Param("pid") String pid) {
        return Strings.isBlank(pid) ? null : sysUnitService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.add")
    @SLog(tag = "新建单位", msg = "单位名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            sysUnitService.save(unit, parentId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/child/?")
    @Ok("beetl:/platform/sys/unit/child.html")
    @RequiresPermissions("sys.manager.unit")
    public Object child(String id) {
        return sysUnitService.query(Cnd.where("parentId", "=", id).asc("path"));
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/unit/detail.html")
    @RequiresPermissions("sys.manager.unit")
    public Object detail(String id) {
        return sysUnitService.fetch(id);
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/unit/edit.html")
    @RequiresPermissions("sys.manager.unit")
    public Object edit(String id, HttpServletRequest req) {
        Sys_unit unit = sysUnitService.fetch(id);
        if (unit != null) {
            req.setAttribute("parentUnit", sysUnitService.fetch(unit.getParentId()));
        }
        return unit;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.edit")
    @SLog(tag = "编辑单位", msg = "单位名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            unit.setOpBy(StringUtil.getUid());
            unit.setOpAt((int) (System.currentTimeMillis() / 1000));
            sysUnitService.updateIgnoreNull(unit);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.delete")
    @SLog(tag = "删除单位", msg = "单位名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_unit unit = sysUnitService.fetch(id);
            req.setAttribute("name", unit.getName());
            if ("0001".equals(unit.getPath())) {
                return Result.error("system.not.allow");
            }
            sysUnitService.deleteAndChild(unit);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit")
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = new ArrayList<>();
        if (shiroUtil.hasRole("sysadmin")) {
            list = sysUnitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        } else {
            Sys_user user = (Sys_user) shiroUtil.getPrincipal();
            if (user != null && Strings.isBlank(pid)) {
                list = sysUnitService.query(Cnd.where("id", "=", user.getUnitid()).asc("path"));
            } else {
                list = sysUnitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
            }
        }
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_unit unit : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }
}
