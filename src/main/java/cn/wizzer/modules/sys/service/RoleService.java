package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.modules.sys.bean.Sys_role;
import cn.wizzer.modules.sys.bean.Sys_menu;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.commons.lang3.StringUtils;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/1.
 */
@IocBean(args = {"refer:dao"})
public class RoleService extends BaseService<Sys_role> {
    public RoleService(Dao dao) {
        super(dao);
    }

    public void update(Sys_user user) {
        dao().update(user);
    }

    /**
     * 新增角色
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(String resourceIds, String uids, Sys_role role) {
        Sys_role r = dao().insert(role);
        String[] res = StringUtils.split(resourceIds, ",");
        String[] uid = StringUtils.split(uids, ",");
        for (String s : res) {
            if (!Strings.isEmpty(s)) {
                dao().insert("sys_role_menu", Chain.make("role_id", r.getId()).add("menu_id", s));
            }
        }
        for (String s : uid) {
            if (!Strings.isEmpty(s)) {
                dao().insert("sys_user_role", Chain.make("role_id", r.getId()).add("user_id", s));
            }
        }
    }

    /**
     * 修改角色
     */
    @Aop(TransAop.READ_COMMITTED)
    public void edit(Sys_role role) {
        role.setCreateUser(cn.wizzer.common.util.StringUtils.getUid());
        role.setCreateTime(new Date());
        dao().update(role);
    }

    /**
     * 设置权限
     */
    @Aop(TransAop.READ_COMMITTED)
    public void saveRole(String resourceIds, String roleId) {
        dao().clear("sys_role_menu", Cnd.where("role_id", "=", roleId));
        String[] res = StringUtils.split(resourceIds, ",");
        for (String s : res) {
            if (!Strings.isEmpty(s)) {
                dao().insert("sys_role_menu", Chain.make("role_id", roleId).add("menu_id", s));
            }
        }
    }

    /**
     * 设置用户
     */
    @Aop(TransAop.READ_COMMITTED)
    public void saveUser(String uids, String roleId) {
        dao().clear("sys_user_role", Cnd.where("role_id", "=", roleId));
        String[] uid = StringUtils.split(uids, ",");
        for (String s : uid) {
            if (!Strings.isEmpty(s)) {
                dao().insert("sys_user_role", Chain.make("role_id", roleId).add("user_id", s));
            }
        }
    }

    public void deleteById(String roleId){
        dao().clear("sys_role_menu", Cnd.where("role_id", "=", roleId));
        dao().clear("sys_user_role", Cnd.where("role_id", "=", roleId));
        dao().delete(Sys_role.class,roleId);
    }
    public Sys_role fetchByName(String name) {
        return fetch(Cnd.where("name", "=", name));
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

    /**
     * 查询用户按钮权限
     *
     * @param ids
     * @return
     */
    public List<Sys_menu> getButtons(String[] ids) {
        Sql sql = Sqls.create("select distinct * from sys_menu where");
        sql.params().set("ids", ids);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询角色按钮权限
     *
     * @param roleId
     * @return
     */
    public List<Sys_menu> getButtonsByRoleid(String roleId) {
        Sql sql = Sqls.create("SELECT DISTINCT a.* FROM sys_menu a,sys_role_menu b WHERE a.id=b.menu_id AND b.role_id=@roleId AND a.type='button'");
        sql.params().set("roleId", roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询角色菜单权限
     *
     * @param roleId
     * @return
     */
    public List<Sys_menu> getMenusByRoleid(String roleId) {
        Sql sql = Sqls.create("SELECT DISTINCT a.* FROM sys_menu a,sys_role_menu b WHERE a.id=b.menu_id AND b.role_id=@roleId AND a.type='menu'");
        sql.params().set("roleId", roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    public void addMenu(Long roleId, String menuId) {
        dao().insert("sys_role_menu", Chain.make("role_id", roleId).add("menu_id", menuId));
    }

    public void removeMenu(Long roleId, String menuId) {
        dao().clear("sys_role_menu", Cnd.where("role_id", "=", roleId).and("menu_id", "=", menuId));
    }
}
