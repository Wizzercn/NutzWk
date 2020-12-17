package com.budwk.app.sys.services.impl;

import com.budwk.app.base.service.impl.BaseServiceImpl;
import com.budwk.app.sys.models.Sys_app_conf;
import com.budwk.app.sys.services.SysAppConfService;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

/**
 * Created by wizzer on 2019/2/27.
 */
@IocBean(args = {"refer:dao"})
public class SysAppConfServiceImpl extends BaseServiceImpl<Sys_app_conf> implements SysAppConfService {
    public SysAppConfServiceImpl(Dao dao) {
        super(dao);
    }

    public List<String> getConfNameList() {
        Sql sql = Sqls.create("SELECT DISTINCT confName FROM sys_app_conf");
        sql.setCallback(Sqls.callback.strs());
        this.dao().execute(sql);
        return sql.getList(String.class);
    }
}