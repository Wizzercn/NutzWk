package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.budwk.app.base.result.Result;
import com.budwk.app.sys.models.Sys_unit;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysUnitService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

;

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
    private SysUserService sysUserService;

    @At("")
    @Ok("beetl:/platform/sys/unit/index.html")
    @SaCheckPermission("sys.manager.unit")
    public void index() {

    }

    @At("/child")
    @Ok("json")
    @SaCheckLogin
    public Object child(@Param("pid") String pid, HttpServletRequest req) {
        List<Sys_unit> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        if (StpUtil.hasRole("sysadmin")) {
            Cnd cnd = Cnd.NEW();
            if (Strings.isBlank(pid)) {
                cnd.and("parentId", "=", "").or("parentId", "is", null);
            } else {
                cnd.and("parentId", "=", pid);
            }
            cnd.asc("location").asc("path");
            list = sysUnitService.query(cnd);
        } else {
            Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
            if (user != null && Strings.isBlank(pid)) {
                list = sysUnitService.query(Cnd.where("id", "=", user.getUnitId()).asc("path"));
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
            NutMap map = Lang.obj2nutmap(unit);
            map.addv("expanded", false);
            map.addv("children", new ArrayList<>());
            treeList.add(map);
        }
        return Result.success().addData(treeList);
    }


    @At("/tree")
    @Ok("json")
    @SaCheckLogin
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        try {
            List<Sys_unit> list = new ArrayList<>();
            List<NutMap> treeList = new ArrayList<>();
            if (Strings.isBlank(pid)) {
                NutMap root = NutMap.NEW().addv("value", "root").addv("label", "不选择单位").addv("leaf", true);
                treeList.add(root);
            }
            if (StpUtil.hasRole("sysadmin")) {
                Cnd cnd = Cnd.NEW();
                if (Strings.isBlank(pid)) {
                    cnd.and("parentId", "=", "").or("parentId", "is", null);
                } else {
                    cnd.and("parentId", "=", pid);
                }
                cnd.asc("location").asc("path");
                list = sysUnitService.query(cnd);
            } else {
                Sys_user user = sysUserService.getUserById(SecurityUtil.getUserId());
                if (user != null && Strings.isBlank(pid)) {
                    list = sysUnitService.query(Cnd.where("id", "=", user.getUnitId()).asc("path"));
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
                    map.addv("leaf", false);
                } else {
                    map.addv("leaf", true);
                }
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.unit.add")
    @SLog(tag = "新建单位", msg = "单位名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            if ("root".equals(parentId)) {
                parentId = "";
            }
            unit.setCreatedBy(SecurityUtil.getUserId());
            sysUnitService.save(unit, parentId);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.unit")
    public Object edit(String id, HttpServletRequest req) {
        try {
            Sys_unit unit = sysUnitService.fetch(id);
            return Result.success().addData(unit);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.unit.edit")
    @SLog(tag = "编辑单位", msg = "单位名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            unit.setUpdatedBy(SecurityUtil.getUserId());
            sysUnitService.updateIgnoreNull(unit);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.unit.delete")
    @SLog(tag = "删除单位", msg = "单位名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_unit unit = sysUnitService.fetch(id);
            req.setAttribute("name", unit.getName());
            if ("0001".equals(unit.getPath())) {
                return Result.error("system.not.allow");
            }
            sysUnitService.deleteAndChild(unit);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

}
