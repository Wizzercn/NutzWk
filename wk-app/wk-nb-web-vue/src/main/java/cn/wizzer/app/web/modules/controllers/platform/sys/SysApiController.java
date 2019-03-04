package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.services.SysApiService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/platform/sys/api")
public class SysApiController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysApiService sysApiService;

    @At("")
    @Ok("beetl:/platform/sys/api/index.html")
    @RequiresPermissions("sys.manager.api")
    public void index() {
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.api.add")
    @SLog(tag = "新建密钥", msg = "应用名称:${name}")
    public Object addDo(@Param("name") String name, HttpServletRequest req) {
        try {
            sysApiService.createAppkey(name, StringUtil.getPlatformUid());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.api.delete")
    @SLog(tag = "删除密钥", msg = "Appid:${appid}")
    public Object delete(String appid, HttpServletRequest req) {
        try {
            sysApiService.deleteAppkey(appid);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.api.edit")
    @SLog(tag = "启用密钥", msg = "Appid:${appid}")
    public Object enable(String appid, HttpServletRequest req) {
        try {
            sysApiService.updateAppkey(appid, false);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.api.edit")
    @SLog(tag = "禁用密钥", msg = "Appid:${appid}")
    public Object disable(String appid, HttpServletRequest req) {
        try {
            sysApiService.updateAppkey(appid, true);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.api")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysApiService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }
}
