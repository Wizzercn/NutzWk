package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysUnitService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.ShiroUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

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
    @Reference
    private SysUnitService sysUnitService;
    @Inject
    private ShiroUtil shiroUtil;

    @At("")
    @Ok("beetl:/platform/sys/unit/index.html")
    @RequiresPermissions("sys.manager.unit")
    public Object index() {
        List<Sys_unit> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        if (shiroUtil.hasRole("sysadmin")) {
            list = sysUnitService.query(Cnd.NEW().asc("location").asc("path"));
        } else {
            Sys_user user = (Sys_user) shiroUtil.getPrincipal();
            if (user != null) {
                list = sysUnitService.query(Cnd.where("id", "=", user.getUnitid()).asc("location").asc("path"));
            }
        }
        for (Sys_unit unit : list) {
            NutMap map = Lang.obj2nutmap(unit);
            map.addv("expanded", false);
            if (unit.isHasChildren()) {
                map.addv("children", getChildren(unit.getId(), list));
            } else {
                map.addv("children", null);
            }
            treeList.add(map);
        }
        return Json.toJson(treeList, JsonFormat.compact());
    }

    private List<String> getChildren(String parentId, List<Sys_unit> list) {
        List<String> idList = new ArrayList<>();
        for (Sys_unit unit : list) {
            if (parentId.equals(unit.getId())) {
                idList.add(unit.getId());
            }
        }
        return idList;
    }

    @At("/tree")
    @Ok("json")
    @RequiresPermissions("sys.manager.unit")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        try {
            List<Sys_unit> list = new ArrayList<>();
            List<NutMap> treeList = new ArrayList<>();
            if (shiroUtil.hasRole("sysadmin")) {
                Cnd cnd = Cnd.NEW();
                if (Strings.isBlank(pid)) {
                    cnd.and("parentId", "=", "").or("parentId", "is", null);
                } else {
                    cnd.and("parentId", "=", pid);
                }
                cnd.asc("location").asc("path");
                list = sysUnitService.query(cnd);
            } else {
                Sys_user user = (Sys_user) shiroUtil.getPrincipal();
                if (user != null && Strings.isBlank(pid)) {
                    list = sysUnitService.query(Cnd.where("id", "=", user.getUnitid()).asc("path"));
                } else {
                    Cnd cnd = Cnd.NEW();
                    if (Strings.isBlank(pid)) {
                        cnd.and("parentId", "=", "").or("parentId", "is", null);
                    } else {
                        cnd.and("parentId", "=", pid);
                    }
                    cnd.asc("location").asc("path");
                    list = sysUnitService.query(cnd);
                }
            }
            for (Sys_unit unit : list) {
                NutMap map = NutMap.NEW().addv("value", unit.getId()).addv("label", unit.getName());
                if (unit.isHasChildren()) {
                    map.addv("children", new ArrayList<>());
                }
                treeList.add(map);
            }
            return Result.success("system.success").addData(treeList);
        } catch (Exception e) {
            return Result.error("system.error");
        }
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
            unit.setOpBy(StringUtil.getPlatformUid());
            unit.setOpAt(Times.getTS());
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

}
