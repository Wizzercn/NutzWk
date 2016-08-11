package cn.wizzer.modules.back.sys.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.sys.models.Sys_api;
import cn.wizzer.modules.back.sys.services.ApiService;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.*;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.UUID;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/sys/api")
@Filters({@By(type = PrivateFilter.class)})
public class ApiController {
    private static final Log log = Logs.get();
    @Inject
    ApiService apiService;

    @At("")
    @Ok("beetl:/private/sys/api/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/sys/api/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.api.add")
    @SLog(tag = "添加应用", msg = "应用名称:${args[0].appName}")
    public Object addDo(@Param("..") Sys_api api) {
        try {
            if (apiService.fetch(Cnd.where("appId", "=", api.getAppId())) != null)
                return Result.error("system.error");
            api.setAppId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10));
            api.setAppSecret(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));
            apiService.insert(api);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.api.delete")
    @SLog(tag = "删除应用", msg = "ID:${args[0]}")
    public Object delete(String id) {
        try {
            apiService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/reset/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.api.edit")
    @SLog(tag = "重置应用", msg = "ID:${args[0]}")
    public Object reset(String id) {
        try {
            apiService.update(org.nutz.dao.Chain.make("appSecret", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16)), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return apiService.data(length, start, draw, order, columns, cnd, null);
    }
}
