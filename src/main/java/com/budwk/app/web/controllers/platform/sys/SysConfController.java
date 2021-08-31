package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.sys.models.Sys_config;
import com.budwk.app.sys.services.SysConfigService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
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

;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/sys/conf")
public class SysConfController {
    private static final Log log = Logs.get();
    @Inject
    private SysConfigService sysConfigService;
    @Inject
    private PubSubService pubSubService;

    @At("")
    @Ok("beetl:/platform/sys/conf/index.html")
    @SaCheckPermission("sys.manager.conf")
    public void index() {

    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.conf.add")
    @SLog(tag = "添加参数", msg = "${conf.configKey}:${conf.configValue}")
    public Object addDo(@Param("..") Sys_config conf) {
        try {
            conf.setCreatedBy(SecurityUtil.getUserId());
            if (sysConfigService.insert(conf) != null) {
                pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_config");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.conf")
    public Object edit(String id) {
        try {
            return Result.success().addData(sysConfigService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.conf.edit")
    @SLog(tag = "修改参数", msg = "${conf.configKey}:${conf.configValue}")
    public Object editDo(@Param("..") Sys_config conf) {
        try {
            conf.setUpdatedBy(SecurityUtil.getUserId());
            if (sysConfigService.updateIgnoreNull(conf) > 0) {
                pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_config");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.conf.delete")
    @SLog(tag = "删除参数", msg = "参数:${configKey}")
    public Object delete(String configKey) {
        try {
            if (Strings.sBlank(configKey).startsWith("App")) {
                return Result.error("系统参数不可删除");
            }
            if (sysConfigService.delete(configKey) > 0) {
                pubSubService.fire(RedisConstant.PLATFORM_REDIS_PREFIX + "web:platform", "sys_config");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("sys.manager.conf")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysConfigService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }
}
