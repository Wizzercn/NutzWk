package cn.wizzer.modules.back.sys.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.sys.models.Sys_log;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class LogService extends Service<Sys_log> {
    public LogService(Dao dao) {
        super(dao);
    }
}
