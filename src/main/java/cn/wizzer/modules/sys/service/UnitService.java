package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseIdEntityService;
import cn.wizzer.modules.sys.bean.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/6/30.
 */
@IocBean(args = {"refer:dao"})
public class UnitService extends BaseIdEntityService<Sys_unit> {
    public UnitService(Dao dao) {
        super(dao);
    }

    public void update(Sys_unit unit) {
        dao().update(unit);
    }

}

