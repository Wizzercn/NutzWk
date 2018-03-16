package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_area;
import cn.wizzer.app.sys.modules.services.SysAreaService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

@IocBean(args = {"refer:dao"})
public class SysAreaServiceImpl extends BaseServiceImpl<Sys_area> implements SysAreaService {
    public SysAreaServiceImpl(Dao dao) {
        super(dao);
    }

    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_area shoparea, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Sys_area pp = this.fetch(pid);
            path = pp.getPath();
        }
        shoparea.setPath(getSubPath("sys_area", "path", path));
        shoparea.setParentId(pid);
        dao().insert(shoparea);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }

    @Aop(TransAop.READ_COMMITTED)
    public void deleteAndChild(Sys_area shoparea) {
        dao().execute(Sqls.create("delete from sys_area where path like @path").setParam("path", shoparea.getPath() + "%"));
        if (!Strings.isEmpty(shoparea.getParentId())) {
            int count = count(Cnd.where("parentId", "=", shoparea.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_area set hasChildren=@f where id=@pid").setParam("f", false).setParam("pid", shoparea.getParentId()));
            }
        }
    }

    public String getNameByCode(String code) {
        Sys_area shopArea = this.fetch(Cnd.where("code", "=", code));
        if (shopArea != null)
            return shopArea.getName();
        return "";
    }
}
