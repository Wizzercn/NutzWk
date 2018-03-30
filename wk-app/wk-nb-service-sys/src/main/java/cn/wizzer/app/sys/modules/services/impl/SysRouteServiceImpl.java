package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_route;
import cn.wizzer.app.sys.modules.services.SysRouteService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/12/23.
 */
@IocBean(args = {"refer:dao"})
public class SysRouteServiceImpl extends BaseServiceImpl<Sys_route> implements SysRouteService {
    public SysRouteServiceImpl(Dao dao) {
        super(dao);
    }
}