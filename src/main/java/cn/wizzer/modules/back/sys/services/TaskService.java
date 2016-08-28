package cn.wizzer.modules.back.sys.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.sys.models.Sys_task;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
@IocBean(args = {"refer:dao"})
public class TaskService extends Service<Sys_task> {
	private static final Log log = Logs.get();

    public TaskService(Dao dao) {
    	super(dao);
    }
}

