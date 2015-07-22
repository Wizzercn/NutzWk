package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.page.Pagination;
import cn.wizzer.modules.sys.bean.Sys_role;
import cn.wizzer.modules.sys.bean.Sys_unit;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.RoleService;
import cn.wizzer.modules.sys.service.UnitService;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag = "用户管理", msg = "")
public class UserAction {
    @Inject
    UserService userService;
    @Inject
    UnitService unitService;
    @Inject
    RoleService roleService;

    @At("")
    @Ok("vm:template.private.sys.user.index")
    @RequiresPermissions("sys:user")
    @SLog(tag = "用户列表", msg = "访问用户列表")
    public Object index() {
        return "";
    }

    @At
    @Ok("vm:template.private.sys.user.list")
    @RequiresPermissions("sys:user")
    public Pagination list(@Param("curPage") int curPage, @Param("pageSize") int pageSize, @Param("unitid") String unitid, HttpServletRequest req) {
        return userService.listPage(curPage, pageSize, Sqls.create("SELECT a.id,a.username,a.is_online,a.is_locked,b.email,b.nickname " +
                "FROM sys_user a,sys_user_profile b ,sys_user_unit c " +
                "WHERE a.id=b.user_id AND a.id=c.user_id AND " +
                "c.unit_id=@unitid ORDER BY a.create_time desc").setParam("unitid", unitid));
    }

    @At("/tree")
    @Ok("raw:json")
    @RequiresPermissions("sys:user")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list;
        if (!Strings.isEmpty(pid)) {
            list = userService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = '" + pid + "' order by location asc,path asc"));
        } else {
            list = userService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where length(path)=4 order by location asc,path asc"));
        }
        return list;
    }

    @At("/add")
    @Ok("vm:template.private.sys.user.add")
    @RequiresPermissions("sys:user")
    public void add(@Param("unitId") String unitId, HttpServletRequest req) {
        if (!Strings.isEmpty(unitId)) {
            req.setAttribute("parentUnit", unitService.fetch(unitId));
        }
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            if (user.isSystem()) {
                req.setAttribute("roleList", roleService.query(Cnd.where("unitid", "=", "").or("unitid", "is", null).asc("location"), null));
            }
        }
    }

    @At("/add/do")
    @Ok("json")
    @RequiresPermissions("sys:user")
    @SLog(tag = "新建用户", msg = "用户名称：${args[0].name}")
    public Object addDo(@Param("..") Sys_user user, @Param("unitId") String unitId, HttpServletRequest req) {

        return Message.error("system.error", req);
    }

    @At("/role/?")
    @Ok("json")
    @RequiresPermissions("sys:user")
    public Object role(String unitId, HttpServletRequest req) {
        Cnd cnd = Cnd.where("unitid", "=", unitId);
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Sys_user user = (Sys_user) currentUser.getPrincipal();
            if (user.isSystem()) {
                cnd = Cnd.where("unitid", "=", "").or("unitid", "is", null).or("unitid", "=", unitId);
            }
        }
        return Message.success("system.success", roleService.query(cnd.asc("location"), null), req);
    }
}
