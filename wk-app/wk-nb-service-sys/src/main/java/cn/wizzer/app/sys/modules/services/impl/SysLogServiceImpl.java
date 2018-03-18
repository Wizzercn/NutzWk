package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_log;
import cn.wizzer.app.sys.modules.services.SysLogService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    public void fastInsertSysLog(Sys_log syslog) {
        logDao().fastInsert(syslog);
    }
}
