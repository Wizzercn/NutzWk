package cn.wizzer.modules.back.robot.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.robot.models.Rb_order;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean(args = {"refer:dao"})
public class RbOrderService extends Service<Rb_order> {
    public RbOrderService(Dao dao) {
        super(dao);
    }
}
