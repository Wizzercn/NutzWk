package com.budwk.app.sys.services.impl;

import com.budwk.app.base.service.impl.BaseServiceImpl;
import com.budwk.app.sys.models.Sys_route;
import com.budwk.app.sys.services.SysRouteService;
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