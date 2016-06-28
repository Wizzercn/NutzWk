package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.BaseService;
import cn.wizzer.modules.models.sys.Sys_log;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class LogService extends BaseService<Sys_log> {
    public LogService(Dao dao) {
        super(dao);
    }
}
