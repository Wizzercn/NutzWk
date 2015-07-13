package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.modules.sys.bean.Sys_role;
import cn.wizzer.modules.sys.bean.Sys_menu;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

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

    public Sys_role fetchByName(String name) {
        return fetch(Cnd.where("name", "=", name));
    }

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

    public void addMenu(Long roleId, String menuId) {
        dao().insert("sys_role_menu", Chain.make("role_id", roleId).add("menu_id", menuId));
    }

    public void removeMenu(Long roleId, String menuId) {
        dao().clear("sys_role_menu", Cnd.where("role_id", "=", roleId).and("menu_id", "=", menuId));
    }
}
