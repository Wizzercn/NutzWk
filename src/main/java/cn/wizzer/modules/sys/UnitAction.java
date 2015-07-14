package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_unit;
import cn.wizzer.modules.sys.service.UnitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
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
        return unitService.query(Cnd.where("length(path)", "=", 4).asc("location").asc("path"), null);
    }

    @At("/add")
    @Ok("vm:template.private.sys.unit.add")
    @RequiresPermissions("sys:unit")
    public void add(@Param("pid") String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            req.setAttribute("parentUnit", unitService.fetch(pid));
        }
    }

    @At("/add/do")
    @Ok("json")
    @RequiresPermissions("sys:unit")
    public Object addDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        int sum=unitService.count(Cnd.where("parentId","=",parentId).and("name","=",unit.getName()));
        if(sum>0){
            return Message.error("机构名称已存在！", req);
        }
        if (unitService.save(unit, parentId)) {
            return Message.success("system.success", req);
        }
        return Message.error("system.error", req);
    }

    @At("/edit/?")
    @Ok("vm:template.private.sys.unit.edit")
    @RequiresPermissions("sys:unit")
    public Object edit(String id, HttpServletRequest req) {
        Sys_unit unit = unitService.fetch(id);
        if (unit != null && !Strings.isEmpty(unit.getParentId())) {
            req.setAttribute("parentUnit", unitService.fetch(unit.getParentId()));
        }
        return unit;
    }

    @At("/edit/do")
    @Ok("json")
    @RequiresPermissions("sys:unit")
    public Object editDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        if(unit.getId().equals(parentId)){
            return Message.error("上级机构不可选择自身！", req);
        }
        int sum=unitService.count(Cnd.where("parentId","=",parentId).and("name","=",unit.getName()));
        if(sum>0){
            return Message.error("机构名称已存在！", req);
        }
        if (unitService.edit(unit, parentId)) {
            return Message.success("system.success", req);
        }
        return Message.success("system.error", req);
    }

    @At("/detail/?")
    @Ok("vm:template.private.sys.unit.detail")
    @RequiresPermissions("sys:unit")
    public Object detail(String id, HttpServletRequest req) {
        Sys_unit unit = unitService.fetch(id);
        if (unit != null && !Strings.isEmpty(unit.getParentId())) {
            req.setAttribute("parentUnit", unitService.getField("name", unit.getParentId()).getName());
        }
        return unit;
    }

    @At("/child/?")
    @Ok("vm:template.private.sys.unit.child")
    @RequiresPermissions("sys:unit")
    public Object child(String id, HttpServletRequest req) {
        return unitService.query(Cnd.where("parentId", "=", id), null);
    }

    @At("/tree")
    @Ok("json")
    @RequiresPermissions("sys:unit")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list;
        if (!Strings.isEmpty(pid)) {
            list = unitService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where parentId = '" + pid + "' order by location asc,path asc"));
        } else {
            list = unitService.list(Sqls.create("select id,name as text,has_children as children from sys_unit where length(path)=4 order by location asc,path asc"));
        }
        return list;
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
