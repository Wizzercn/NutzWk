package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_api;
import cn.wizzer.app.sys.modules.services.SysApiService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/12/23.
 */
@IocBean(args = {"refer:dao"})
public class SysApiServiceImpl extends BaseServiceImpl<Sys_api> implements SysApiService {
    public SysApiServiceImpl(Dao dao) {
        super(dao);
    }
}