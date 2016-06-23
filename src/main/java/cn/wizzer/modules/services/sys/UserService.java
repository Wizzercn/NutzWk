package cn.wizzer.modules.services.sys;

import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_role;
import cn.wizzer.modules.models.sys.Sys_user;
import cn.wizzer.common.base.BaseService;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class UserService extends BaseService<Sys_user> {
    public UserService(Dao dao) {
        super(dao);
    }

    /**
     * 查询用户菜单权限
     *
     * @param userId
     * @return
     */
    public List<Sys_menu> getMenus(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and" +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=false) and a.disabled=false and a.isShow=true and a.type='menu' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询用户角色code列表
     *
     * @param user
     * @return
     */
    public List<String> getRoleCodeList(Sys_user user) {
        dao().fetchLinks(user, "roles");
        List<String> roleNameList = new ArrayList<String>();
        for (Sys_role role : user.getRoles()) {
            if (!role.isDisabled())
                roleNameList.add(role.getCode());
        }
        return roleNameList;
    }
}
