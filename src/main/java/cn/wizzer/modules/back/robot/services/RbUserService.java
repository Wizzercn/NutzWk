package cn.wizzer.modules.back.robot.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.robot.models.Rb_user;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class RbUserService extends Service<Rb_user> {
    public RbUserService(Dao dao) {
        super(dao);
    }
}
