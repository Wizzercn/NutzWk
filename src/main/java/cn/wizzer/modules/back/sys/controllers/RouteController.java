package cn.wizzer.modules.back.sys.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.sys.models.Sys_route;
import cn.wizzer.modules.back.sys.models.Sys_task;
import cn.wizzer.modules.back.sys.services.RouteService;
import cn.wizzer.modules.back.sys.services.TaskService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.quartz.JobKey;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/private/sys/route")
@Filters({@By(type = PrivateFilter.class)})
public class RouteController {
    private static final Log log = Logs.get();
    @Inject
    private RouteService routeService;

    @At("")
    @Ok("beetl:/private/sys/route/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return routeService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/private/sys/route/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建路由", msg = "URL:${args[0].url}")
    @RequiresPermissions("sys.manager.route.add")
    public Object addDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            routeService.insert(route);
            Globals.initRoute(routeService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/sys/route/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return routeService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改路由", msg = "URL:${args[0].url}")
    @RequiresPermissions("sys.manager.route.edit")
    public Object editDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            route.setOpBy(Strings.sNull(req.getAttribute("uid")));
            route.setOpAt((int) (System.currentTimeMillis() / 1000));
            routeService.updateIgnoreNull(route);
            Globals.initRoute(routeService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @At({"/delete", "/delete/?"})
    @Ok("json")
    @SLog(tag = "删除路由", msg = "路由ID:${args[2].getAttribute('id')}")
    @RequiresPermissions("sys.manager.route.delete")
    public Object delete(String id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                routeService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                routeService.delete(id);
                req.setAttribute("id", id);
            }
            Globals.initRoute(routeService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.route.edit")
    @SLog(tag = "启用路由", msg = "URL:${args[1].getAttribute('url')}")
    public Object enable(String id, HttpServletRequest req) {
        try {
            Sys_route route = routeService.fetch(id);
            req.setAttribute("url", route.getUrl());
            routeService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            Globals.initRoute(routeService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.route.edit")
    @SLog(tag = "禁用路由", msg = "URL:${args[1].getAttribute('name')}")
    public Object disable(String id, HttpServletRequest req) {
        try {
            Sys_route route = routeService.fetch(id);
            req.setAttribute("url", route.getUrl());
            routeService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            Globals.initRoute(routeService.dao());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
