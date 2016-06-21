package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.nutzwk.models.sys.Sys_menu;
import cn.wizzer.modules.sys.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/menu")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag = "菜单管理", msg = "")
public class MenuAction {
    @Inject
    MenuService menuService;

    @At("")
    @Ok("vm:template.private.sys.menu.index")
    @RequiresPermissions("sys:menu")
    @SLog(tag = "菜单列表", msg = "访问菜单列表")
    public Object index() {
        return menuService.query(Cnd.where("parentId", "=", "").asc("location").asc("path"), null);
    }

    @At("/add")
    @Ok("vm:template.private.sys.menu.add")
    @RequiresPermissions("sys:menu")
    public void add(@Param("pid") String pid, HttpServletRequest req) {
        if (!Strings.isEmpty(pid)) {
            req.setAttribute("parentMenu", menuService.fetch(pid));
        }
    }

    @At("/add/do")
    @Ok("json")
    @RequiresPermissions("sys:menu")
    @SLog(tag = "新建菜单", msg = "菜单名称：${args[0].name}")
    public Object addDo(@Param("..") Sys_menu menu, @Param("btns") String btns, @Param("parentId") String parentId, HttpServletRequest req) {
        int sum = menuService.count(Cnd.where("parentId", "=", parentId).and("name", "=", menu.getName()));
        if (sum > 0) {
            return Message.error("菜单名称已存在！", req);
        }
        try {
            menuService.save(menu, parentId, btns);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("vm:template.private.sys.menu.edit")
    @RequiresPermissions("sys:menu")
    public Sys_menu edit(String id, HttpServletRequest req) {
        Sys_menu menu = menuService.fetch(id);
        if (menu != null && !Strings.isEmpty(menu.getParentId())) {
            req.setAttribute("parentMenu", menuService.fetch(menu.getParentId()));
        }
        return menu;
    }

    @At("/edit/do")
    @Ok("json")
    @RequiresPermissions("sys:menu")
    @SLog(tag = "修改菜单", msg = "菜单名称：${args[0].name}")
    public Object editDo(@Param("..") Sys_menu menu, @Param("pid") String pid, HttpServletRequest req) {
        if (menu.getParentId().equals(menu.getId())) {
            return Message.error("上级菜单不可为自身！", req);
        }
        if (!Strings.sNull(pid).equals(menu.getParentId())) {
            int sum = menuService.count(Cnd.where("parentId", "=", menu.getParentId()).and("name", "=", menu.getName()));
            if (sum > 0) {
                return Message.error("菜单名称已存在！", req);
            }
        }
        try {
            menuService.edit(menu, pid);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.success("system.error", req);
        }
    }

    @At("/detail/?")
    @Ok("vm:template.private.sys.menu.detail")
    @RequiresPermissions("sys:menu")
    public Sys_menu detail(String id, HttpServletRequest req) {
        Sys_menu menu = menuService.fetch(id);
        if (menu != null && !Strings.isEmpty(menu.getParentId())) {
            req.setAttribute("parentMenu", menuService.getField("name", menu.getParentId()).getName());
        }
        return menu;
    }

    @At("/child/?")
    @Ok("vm:template.private.sys.menu.child")
    @RequiresPermissions("sys:menu")
    public List<Sys_menu> child(String id, HttpServletRequest req) {
        return menuService.query(Cnd.where("parentId", "=", id), null);
    }

    @At("/tree")
    @Ok("json")
    @RequiresPermissions("sys:menu")
    public Object tree(@Param("pid") String pid, HttpServletRequest req) {
        List<Record> list;
        if (!Strings.isEmpty(pid)) {
            list = menuService.list(Sqls.create("select id,name as text,has_children as children from sys_menu where parentId =@pid order by location asc,path asc").setParam("pid", pid));
        } else {
            list = menuService.list(Sqls.create("select id,name as text,has_children as children from sys_menu where length(path)=4 order by location asc,path asc"));
        }
        return list;
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys:menu")
    @SLog(tag = "删除菜单", msg = "菜单名称：${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        Sys_menu menu = menuService.fetch(id);
        req.setAttribute("name", menu.getName());
        if (Strings.sNull(menu.getPath()).startsWith("0001")) {
            return Message.error("system.not.allow", req);
        }
        try {
            menuService.deleteAndChild(id);
            return Message.success("system.success", req);
        } catch (Exception e) {
            return Message.error("system.error", req);
        }
    }
}
