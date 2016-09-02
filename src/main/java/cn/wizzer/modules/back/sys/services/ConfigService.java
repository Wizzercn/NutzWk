package cn.wizzer.modules.back.sys.services;

import cn.wizzer.modules.back.sys.models.Sys_config;
import cn.wizzer.common.base.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class ConfigService extends Service<Sys_config> {
    public ConfigService(Dao dao) {
        super(dao);
    }

}
