package cn.wizzer.modules.back.robot.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.robot.models.Rb_config;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class RbConfigService extends Service<Rb_config> {
    public RbConfigService(Dao dao) {
        super(dao);
    }
}
