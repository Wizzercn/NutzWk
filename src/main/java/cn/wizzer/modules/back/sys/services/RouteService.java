package cn.wizzer.modules.back.sys.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.sys.models.Sys_route;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * Created by Wizzer on 2016/7/31.
 */
@IocBean(args = {"refer:dao"})
public class RouteService extends Service<Sys_route> {
    private static final Log log = Logs.get();

    public RouteService(Dao dao) {
        super(dao);
    }
}
