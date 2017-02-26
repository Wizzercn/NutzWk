package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.sys.Sys_plugin;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean(args = {"refer:dao"})
public class SysPluginService extends Service<Sys_plugin> {
    private static final Log log = Logs.get();

    public SysPluginService(Dao dao) {
        super(dao);
    }
}

