package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.services.SysLogService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.DateUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean
@At("/platform/sys/log")
public class SysLogController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
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
    public Object data(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("type") String type, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        String tableName = Times.format("yyyyMM", new Date());
        if (Strings.isNotBlank(beginDate)) {
            tableName = Times.format("yyyyMM", Times.D(beginDate + " 00:00:00"));
            cnd.and("opAt", ">=", DateUtil.getTime(beginDate + " 00:00:00"));
        }
        if (Strings.isNotBlank(endDate)) {
            cnd.and("opAt", "<=", DateUtil.getTime(endDate + " 23:59:59"));
        }
        if (Strings.isNotBlank(type)) {
            cnd.and("type", "=", type);
        }
        return sysLogService.logData(tableName, length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.log.delete")
    @SLog(tag = "清除日志", msg = "清除日志")
    public Object delete(HttpServletRequest req) {
        try {
            sysLogService.clear();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
