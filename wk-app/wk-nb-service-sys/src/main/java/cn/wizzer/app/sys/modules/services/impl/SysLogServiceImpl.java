package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_log;
import cn.wizzer.app.sys.modules.services.SysLogService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.framework.page.Pagination;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.math.NumberUtils;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;

import java.util.*;

/**
 * Created by wizzer on 2016/12/22.
 */
@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysLogService.class)
public class SysLogServiceImpl extends BaseServiceImpl<Sys_log> implements SysLogService {
    public SysLogServiceImpl(Dao dao) {
        super(dao);
    }

    /**
     * 按月分表的dao实例
     */
    protected Map<String, Dao> ymDaos = new HashMap<String, Dao>();

    /**
     * 获取按月分表的Dao实例,即当前日期的dao实例
     *
     * @return
     */
    public Dao logDao() {
        Calendar cal = Calendar.getInstance();
        String key = String.format("%d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        return logDao(key);
    }

    /**
     * 获取特定月份的Dao实例
     *
     * @param key
     * @return
     */
    public Dao logDao(String key) {
        Dao dao = ymDaos.get(key);
        if (dao == null) {
            synchronized (this) {
                dao = ymDaos.get(key);
                if (dao == null) {
                    dao = Daos.ext(this.dao(), key);
                    dao.create(Sys_log.class, false);
                    ymDaos.put(key, dao);
                    try {
                        Daos.migration(dao, Sys_log.class, true, false);
                    } catch (Throwable e) {
                    }
                }
            }
        }
        return dao;
    }

    @Async
    public void fastInsertSysLog(Sys_log syslog) {
        logDao().insert(syslog);
    }

    public NutMap logData(String tableName, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName) {
        if (logDao(tableName).exists(Sys_log.class)) {
            SysLogService sysLogService2 = new SysLogServiceImpl(logDao(tableName));
            return sysLogService2.data(length, start, draw, orders, columns, cnd, null);
        } else
            return this.data(length, start, draw, orders, columns, cnd, null);
    }

    /**
     * 查询日期
     *
     * @param tablaeName 分表名称
     * @param pageNumber 页码
     * @param pageSize   页大小
     * @param cnd        查询条件
     * @return
     */
    public Pagination data(String tablaeName, int pageNumber, int pageSize, Cnd cnd) {
        Pager pager = this.logDao(tablaeName).createPager(pageNumber, pageSize);
        List<Sys_log> list = this.logDao(tablaeName).query(this.getEntityClass(), cnd, pager);
        pager.setRecordCount(this.logDao(tablaeName).count(this.getEntityClass(), cnd));
        return new Pagination(pageNumber, pageSize, pager.getRecordCount(), list);
    }

    /**
     * 多月日志条件查询
     *
     * @param date          时间范围
     * @param type          日志类型
     * @param pageOrderName 排序字段名称
     * @param pageOrderBy   排序方式
     * @param pageNumber    页码
     * @param pageSize      页大小
     * @return
     */
    public Pagination data(String[] date, String type, String pageOrderName, String pageOrderBy, int pageNumber, int pageSize) {
        String tableName = Times.format("yyyyMM", new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select sl.* from (");
        if (date == null || date.length == 0) {
            stringBuilder.append(" select * from sys_log_" + tableName);
            if (Strings.isNotBlank(type)) {
                stringBuilder.append(" where type='" + type + "'");
            }
        } else {
            int m1 = NumberUtils.toInt(Times.format("yyyyMM", Times.D(date[0])));
            int m2 = NumberUtils.toInt(Times.format("yyyyMM", Times.D(date[1])));
            if (m1 == m2) {
                stringBuilder.append(" select * from sys_log_" + m1 + " where 1=1 ");
                if (Strings.isNotBlank(type)) {
                    stringBuilder.append(" and type='" + type + "'");
                }
                stringBuilder.append(" and opAt>=" + Times.d2TS(Times.D(date[0])));
                stringBuilder.append(" and opAt<=" + Times.d2TS(Times.D(date[1])));
            } else {
                for (int i = m1; i < m2 + 1; i++) {
                    if (this.dao().exists("sys_log_" + i)) {
                        stringBuilder.append(" select * from sys_log_" + i + " where 1=1 ");
                        if (Strings.isNotBlank(type)) {
                            stringBuilder.append(" and type='" + type + "'");
                        }
                        stringBuilder.append(" and opAt>=" + Times.d2TS(Times.D(date[0])));
                        stringBuilder.append(" and opAt<=" + Times.d2TS(Times.D(date[1])));
                        if (i < m2) {
                            stringBuilder.append(" UNION ALL ");
                        }
                    }
                }
            }
        }
        stringBuilder.append(")sl ");
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            stringBuilder.append(" order by sl." + pageOrderName + " " + pageOrderBy);
        }
        return this.listPage(pageNumber, pageSize, Sqls.create(stringBuilder.toString()));
    }
}
