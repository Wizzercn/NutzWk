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
import org.nutz.trans.Trans;

import java.util.ArrayList;
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
    public void save(String resourceIds, String uids, String unitId, Sys_role role) {
        if ("_system".equals(unitId)) {
            role.setUnitid("");
        } else {
            role.setUnitid(unitId);
        }
        role.setCreateUser(cn.wizzer.common.util.StringUtils.getUid());
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

    public void addMenu(Long roleId, String menuId) {
        dao().insert("sys_role_menu", Chain.make("role_id", roleId).add("menu_id", menuId));
    }

    public void removeMenu(Long roleId, String menuId) {
        dao().clear("sys_role_menu", Cnd.where("role_id", "=", roleId).and("menu_id", "=", menuId));
    }
}
