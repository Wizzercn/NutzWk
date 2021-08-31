package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.sys.models.Sys_route;
import com.budwk.app.sys.services.SysRouteService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

;

@IocBean
@At("/platform/sys/route")
public class SysRouteController {
    private static final Log log = Logs.get();
    @Inject
    private SysRouteService routeService;
    @Inject
    private PubSubService pubSubService;

    @At("")
    @Ok("beetl:/platform/sys/route/index.html")
    @SaCheckPermission("sys.manager.route")
    public void index() {

    }

    @At
    @Ok("json:full")
    @SaCheckPermission("sys.manager.route")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(routeService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SLog(tag = "新建路由", msg = "URL:${args[0].url}")
    @SaCheckPermission("sys.manager.route.add")
    public Object addDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            route.setCreatedBy(SecurityUtil.getUserId());
            routeService.insert(route);
            pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_route");
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.route")
    public Object edit(String id) {
        try {
            return Result.success().addData(routeService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SLog(tag = "修改路由", msg = "URL:${args[0].url}")
    @SaCheckPermission("sys.manager.route.edit")
    public Object editDo(@Param("..") Sys_route route, HttpServletRequest req) {
        try {
            route.setUpdatedBy(SecurityUtil.getUserId());
            routeService.updateIgnoreNull(route);
            pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_route");
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At("/delete/?")
    @Ok("json")
    @SLog(tag = "删除路由", msg = "路由ID:${args[0]}")
    @SaCheckPermission("sys.manager.route.delete")
    public Object delete(String id, HttpServletRequest req) {
        try {
            routeService.delete(id);
            pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_route");
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.route.edit")
    @SLog(tag = "启用路由", msg = "URL:${args[1].getAttribute('url')}")
    public Object enable(String id, HttpServletRequest req) {
        try {
            Sys_route route = routeService.fetch(id);
            req.setAttribute("url", route.getUrl());
            routeService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_route");
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.route.edit")
    @SLog(tag = "禁用路由", msg = "URL:${args[1].getAttribute('name')}")
    public Object disable(String id, HttpServletRequest req) {
        try {
            Sys_route route = routeService.fetch(id);
            req.setAttribute("url", route.getUrl());
            routeService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_route");
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
