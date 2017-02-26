package cn.wizzer.modules.services.sys;

import cn.wizzer.modules.models.sys.Sys_config;
import cn.wizzer.common.base.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class SysConfigService extends Service<Sys_config> {
    public SysConfigService(Dao dao) {
        super(dao);
    }

}
