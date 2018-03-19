package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_log;
import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysLogService extends BaseService<Sys_log> {
    /**
     * 快速插入日志
     * @param syslog
     */
    void fastInsertSysLog(Sys_log syslog);

    /**
     * 分表查询数据
     * @param tableName
     * @param length
     * @param start
     * @param draw
     * @param orders
     * @param columns
     * @param cnd
     * @param linkName
     * @return
     */
    NutMap logData(String tableName,int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName);
}
