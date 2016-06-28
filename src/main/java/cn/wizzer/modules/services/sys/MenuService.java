package cn.wizzer.modules.services.sys;

import cn.wizzer.common.base.BaseService;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_unit;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean(args = {"refer:dao"})
public class MenuService extends BaseService<Sys_menu> {
    public MenuService(Dao dao) {
        super(dao);
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_menu menu, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Sys_menu pp = this.fetch(pid);
            path = pp.getPath();
        }else pid="";
        menu.setPath(getSubPath("sys_menu", "path", path));
        menu.setParentId(pid);
        dao().insert(menu);
        if (!Strings.isEmpty(pid)&&"menu".equals(menu.getType())) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }
}
