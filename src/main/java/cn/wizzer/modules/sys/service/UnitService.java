package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.*;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * Created by Wizzer.cn on 2015/6/30.
 */
@IocBean(args = {"refer:dao"})
public class UnitService extends BaseService<Sys_unit> {
    public UnitService(Dao dao) {
        super(dao);
    }

    public void update(Sys_unit unit) {
        dao().update(unit);
    }

    @Aop(TransAop.READ_COMMITTED)
    public boolean deleteAndChild(String id) {
        String pid = StringUtils.getParentId(id);
        dao().execute(Sqls.create("delete from sys_unit where id like '" + id + "%'"));
        if (!Strings.isEmpty(pid)) {
            int count = count(Cnd.where("id", "like", pid + "____"));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_unit set has_children=false where id='" + pid + "'"));
            }
        }
        return true;
    }
}

