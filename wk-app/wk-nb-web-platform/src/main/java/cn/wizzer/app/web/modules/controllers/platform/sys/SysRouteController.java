package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_route;
import cn.wizzer.app.sys.modules.services.SysRouteService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/sys/route")
public class SysRouteController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysRouteService routeService;
    @Inject
    private PubSubService pubSubService;

    @At("")
    @Ok("beetl:/platform/sys/route/index.html")
    @RequiresPermissions("sys.manager.route")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.route")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return routeService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/platform/sys/route/add.html")
    @RequiresPermissions("sys.manager.route")
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建路由", msg = "URL:${args[0].url}")
    @RequiresPermissions("sys.manager.route.add")
    public Object addDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            route.setOpBy(StringUtil.getPlatformUid());
            routeService.insert(route);
            pubSubService.fire("nutzwk:web:platform","sys_route");
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/route/edit.html")
    @RequiresPermissions("sys.manager.route")
    public Object edit(String id) {
        return routeService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改路由", msg = "URL:${args[0].url}")
    @RequiresPermissions("sys.manager.route.edit")
    public Object editDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            route.setOpBy(StringUtil.getPlatformUid());
            route.setOpAt(Times.getTS());
            routeService.updateIgnoreNull(route);
            pubSubService.fire("nutzwk:web:platform","sys_route");

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
            pubSubService.fire("nutzwk:web:platform","sys_route");
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
            pubSubService.fire("nutzwk:web:platform","sys_route");
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
            pubSubService.fire("nutzwk:web:platform","sys_route");
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
