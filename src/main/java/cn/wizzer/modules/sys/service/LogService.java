package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.modules.sys.bean.Sys_log;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/22.
 */
@IocBean(args = {"refer:dao"})
public class LogService extends BaseService<Sys_log> {
    public LogService(Dao dao) {
        super(dao);
    }

    public void update(Sys_log log) {
        dao().update(log);
    }

}
