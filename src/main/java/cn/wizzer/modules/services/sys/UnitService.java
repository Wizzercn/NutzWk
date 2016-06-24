package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.BaseService;
import cn.wizzer.modules.models.sys.Sys_unit;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean(args = {"refer:dao"})
public class UnitService extends BaseService<Sys_unit> {
    public UnitService(Dao dao) {
        super(dao);
    }

    /**
     * 新增单位
     *
     * @param unit
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_unit unit, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Sys_unit pp = this.fetch(pid);
            path = pp.getPath();
        }
        unit.setPath(getSubPath("sys_unit", "path", path));
        unit.setParentId(pid);
        dao().insert(unit);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren",true), Cnd.where("id","=",pid));
        }
    }
}
