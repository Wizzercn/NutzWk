package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.sys.Sys_log;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class SysLogService extends Service<Sys_log> {
    public SysLogService(Dao dao) {
        super(dao);
    }
}
