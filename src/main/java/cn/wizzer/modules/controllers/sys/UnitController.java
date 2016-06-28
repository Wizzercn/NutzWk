package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.services.sys.UnitService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
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
@At("/private/sys/unit")
@Filters({@By(type = PrivateFilter.class)})
public class UnitController {
    private static final Log log = Logs.get();
    @Inject
    UnitService unitService;

    @At("")
    @Ok("beetl:/private/sys/unit/index.html")
    @RequiresAuthentication
    public Object index() {
        return unitService.query(Cnd.where("parentId", "=", "").asc("path"));
    }

    @At
    @Ok("beetl:/private/sys/unit/add.html")
    @RequiresAuthentication
    public Object add(@Param("pid") String pid) {
        return Strings.isBlank(pid) ? null : unitService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.add")
    @SLog(tag = "新建单位", msg = "单位名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            unitService.save(unit, parentId);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/child/?")
    @Ok("beetl:/private/sys/unit/child.html")
    @RequiresAuthentication
    public Object child(String id) {
        return unitService.query(Cnd.where("parentId", "=", id).asc("path"));
    }

    @At("/detail/?")
    @Ok("beetl:/private/sys/unit/detail.html")
    @RequiresAuthentication
    public Object detail(String id) {
        return unitService.fetch(id);
    }

    @At("/edit/?")
    @Ok("beetl:/private/sys/unit/edit.html")
    @RequiresAuthentication
    public Object edit(String id, HttpServletRequest req) {
        Sys_unit unit = unitService.fetch(id);
        if (unit != null) {
            req.setAttribute("parentUnit", unitService.fetch(unit.getParentId()));
        }
        return unit;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.edit")
    @SLog(tag = "编辑单位", msg = "单位名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            unit.setUpdateAt((int) (System.currentTimeMillis() / 1000));
            unitService.updateIgnoreNull(unit);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.delete")
    @SLog(tag = "删除单位", msg = "单位名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_unit unit = unitService.fetch(id);
            req.setAttribute("name", unit.getName());
            if ("0001".equals(unit.getPath())) {
                return Result.error("system.not.allow", req);
            }
            unitService.deleteAndChild(unit);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
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
