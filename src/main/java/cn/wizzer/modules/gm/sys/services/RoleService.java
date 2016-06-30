package cn.wizzer.modules.gm.sys.services;

import cn.wizzer.modules.gm.sys.models.Sys_menu;
import cn.wizzer.modules.gm.sys.models.Sys_role;
import cn.wizzer.common.base.Service;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class RoleService extends Service<Sys_role> {
    public RoleService(Dao dao) {
        super(dao);
    }

    public List<Sys_menu> getMenusAndButtons(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and" +
                " b.roleId=@roleId and a.disabled=false order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    public List<Sys_menu> getDatas(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and" +
                " b.roleId=@roleId and a.type='data' and a.disabled=false order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    public List<Sys_menu> getDatas() {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and a.type='data' order by a.location ASC,a.path asc");
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询权限
     *
     * @param role
     * @return
     */
    public List<String> getPermissionNameList(Sys_role role) {
        dao().fetchLinks(role, "menus");
        List<String> list = new ArrayList<String>();
        for (Sys_menu menu : role.getMenus()) {
            if (!Strings.isEmpty(menu.getPermission())) {
                list.add(menu.getPermission());
            }
        }
        return list;
    }
}
