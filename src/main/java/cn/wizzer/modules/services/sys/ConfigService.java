package cn.wizzer.modules.services.sys;

import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.common.base.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class ConfigService extends BaseService<Sys_user> {
    public ConfigService(Dao dao) {
        super(dao);
    }

}
