package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.sys.Sys_task;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
@IocBean(args = {"refer:dao"})
public class SysTaskService extends Service<Sys_task> {
	private static final Log log = Logs.get();

    public SysTaskService(Dao dao) {
    	super(dao);
    }
}

