package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.services.SysLogService;
import cn.wizzer.app.web.commons.utils.DateUtil;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.framework.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean
@At("/platform/sys/log")
public class SysLogController {
    private static final Log log = Logs.get();
    @Inject
    private SysLogService sysLogService;

    @At("")
    @Ok("beetl:/platform/sys/log/index.html")
    @RequiresPermissions("sys.manager.log")
    public void index(HttpServletRequest req) {
        req.setAttribute("today", DateUtil.getDate());
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.log")
    public Object data(@Param("searchDate") String searchDate, @Param("searchType") String searchType, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            String[] date = StringUtils.split(searchDate, ",");
            return Result.success().addData(sysLogService.data(date, searchType, pageOrderName, PageUtil.getOrder(pageOrderBy), pageNumber, pageSize));
        } catch (Exception e) {
            return Result.error();
        }
    }
}
