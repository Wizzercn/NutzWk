package cn.wizzer.modules.back.sys.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.sys.models.Sys_config;
import cn.wizzer.modules.back.sys.services.ConfigService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/sys/conf")
@Filters({@By(type = PrivateFilter.class)})
public class ConfController {
    private static final Log log = Logs.get();
    @Inject
    ConfigService configService;

    @At("")
    @Ok("beetl:/private/sys/conf/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/sys/conf/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.add")
    @SLog(tag = "添加参数", msg = "${args[0].configKey}:${args[0].configValue}")
    public Object addDo(@Param("..") Sys_config conf, HttpServletRequest req) {
        try {
            if (configService.insert(conf) != null) {
                Globals.init(configService.dao());
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/sys/conf/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return configService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.edit")
    @SLog(tag = "修改参数", msg = "${args[0].configKey}:${args[0].configValue}")
    public Object editDo(@Param("..") Sys_config conf, HttpServletRequest req) {
        try {
            if (configService.updateIgnoreNull(conf) > 0) {
                Globals.init(configService.dao());
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.delete")
    @SLog(tag = "删除参数", msg = "参数:${args[0]}")
    public Object delete(String configKey, HttpServletRequest req) {
        try {
            if (Strings.sBlank(configKey).startsWith("App")) {
                return Result.error("系统参数不可删除", req);
            }
            if (configService.delete(configKey) > 0) {
                Globals.init(configService.dao());
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return configService.data(length, start, draw, order, columns, cnd, null);
    }
}
