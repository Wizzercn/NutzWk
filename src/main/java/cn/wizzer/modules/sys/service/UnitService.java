package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseNameEntityService;
import cn.wizzer.modules.sys.bean.*;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer.cn on 2015/6/30.
 */
@IocBean(args = {"refer:dao"})
public class UnitService extends BaseNameEntityService<Sys_unit> {
    public UnitService(Dao dao) {
        super(dao);
    }

    public void update(Sys_unit unit) {
        dao().update(unit);
    }

}

