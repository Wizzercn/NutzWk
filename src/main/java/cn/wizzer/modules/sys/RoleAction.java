package cn.wizzer.modules.sys;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.page.Pagination;
import cn.wizzer.common.util.DateUtils;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.RoleService;
import cn.wizzer.modules.sys.service.UnitService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/25.
 */
@IocBean
@At("/private/sys/role")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag = "角色管理", msg = "")
public class RoleAction {
    private Log log = Logs.get();
    @Inject
    RoleService roleService;
    @Inject
    UnitService unitService;

    @At("")
    @Ok("vm:template.private.sys.role.index")
    @RequiresPermissions("sys:role")
    @SLog(tag = "角色列表", msg = "访问角色列表")
    public Object index() {
        return "";
    }

    @At("/info/?")
    @Ok("vm:template.private.sys.role.info")
    @RequiresPermissions("sys:role")
    public Object info(String unitId, HttpServletRequest req) {
        Cnd cnd;
        if ("_system".equals(unitId)) {
            cnd = Cnd.where("unitid", "is", null).or("unitid", "=", "");
        } else cnd = Cnd.where("unitid", "=", unitId);
        return roleService.query(cnd.asc("location"), null);
    }

    @At("/tree")
    @Ok("raw:json")
    @RequiresPermissions("sys:role")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list1 = new ArrayList<>();
        list1.add(new Record().set("id", "_system").set("text", "系统角色").set("children", false));
        List<Record> list = new ArrayList<>();
        List<Record> listAll = new ArrayList<>();
        Subject currentUser = SecurityUtils.getSubject();
        boolean isSystem = false;
        String uid = "";
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            uid = user.getId();
            if (user.isSystem()) {
                isSystem = true;
            }
        }
        if (isSystem) {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where length(path)=4 order by location asc,path asc"));
                listAll.addAll(list1);
            }
        } else {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(
                        Sqls.create("select a.id,a.name as text,a.has_children as children from sys_unit a,sys_user_unit b " +
                                " where a.id=b.unit_id and b.user_id=@uid order by a.location asc,a.path asc").setParam("uid", uid));
            }
        }
        listAll.addAll(list);
        return listAll;
    }

    @At("/menu/?")
    @Ok("raw:json")
    @RequiresPermissions("sys:role")
    public Object menu(String unitId, @Param("pid") String pid, HttpServletRequest req) {
        List<Record> list = new ArrayList<>();
        if ("_system".equals(unitId)) {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_menu  where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_menu where length(path)=4 order by location asc,path asc"));
            }
        } else {
            if (!Strings.isEmpty(pid)) {
                list = roleService.list(Sqls.create("select id,name as text,has_children as children from sys_menu where parentId = @pid order by location asc,path asc").setParam("pid", pid));
            } else {
                list = roleService.list(
                        Sqls.create("select DISTINCT a.id,a.name as text,a.has_children as children from sys_menu a,sys_role_menu b " +
                                " where a.id=b.menu_id and b.role_id in(select c.id from sys_role c where c.unitid=@unitid) and length(a.path)=4 order by a.location asc,a.path asc").setParam("unitid", unitId));
            }
        }
        return list;
    }

    @At("/add")
    @Ok("vm:template.private.sys.role.add")
    @RequiresPermissions("sys:role")
    public Object add() {
        return "";

    }
}
