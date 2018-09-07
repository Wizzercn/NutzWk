package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_config;
import cn.wizzer.app.sys.modules.services.SysConfigService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/sys/conf")
public class SysConfController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysConfigService sysConfigService;
    @Inject
    private PubSubService pubSubService;

    @At("")
    @Ok("beetl:/platform/sys/conf/index.html")
    @RequiresPermissions("sys.manager.conf")
    public void index() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.add")
    @SLog(tag = "添加参数", msg = "${args[0].configKey}:${args[0].configValue}")
    public Object addDo(@Param("..") Sys_config conf) {
        try {
            conf.setOpBy(StringUtil.getPlatformUid());
            conf.setOpAt(Times.getTS());
            if (sysConfigService.insert(conf) != null) {
                pubSubService.fire("nutzwk:web:platform", "sys_config");
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.conf")
    public Object edit(String id) {
        try {
            return Result.success("system.success", sysConfigService.fetch(id));
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.edit")
    @SLog(tag = "修改参数", msg = "${args[0].configKey}:${args[0].configValue}")
    public Object editDo(@Param("..") Sys_config conf) {
        try {
            conf.setOpBy(StringUtil.getPlatformUid());
            conf.setOpAt(Times.getTS());
            if (sysConfigService.updateIgnoreNull(conf) > 0) {
                pubSubService.fire("nutzwk:web:platform", "sys_config");
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.delete")
    @SLog(tag = "删除参数", msg = "参数:${args[0]}")
    public Object delete(String configKey) {
        try {
            if (Strings.sBlank(configKey).startsWith("App")) {
                return Result.error("系统参数不可删除");
            }
            if (sysConfigService.delete(configKey) > 0) {
                pubSubService.fire("nutzwk:web:platform", "sys_config");
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.conf")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(sysConfigService.listPage(pageNumber, pageSize, cnd));
    }
}
