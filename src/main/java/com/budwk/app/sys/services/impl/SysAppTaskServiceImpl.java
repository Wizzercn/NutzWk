package com.budwk.app.sys.services.impl;

import com.budwk.app.base.service.impl.BaseServiceImpl;
import com.budwk.app.sys.models.Sys_app_task;
import com.budwk.app.sys.services.SysAppTaskService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2019/2/27.
 */
@IocBean(args = {"refer:dao"})
public class SysAppTaskServiceImpl extends BaseServiceImpl<Sys_app_task> implements SysAppTaskService {
    public SysAppTaskServiceImpl(Dao dao) {
        super(dao);
    }

}