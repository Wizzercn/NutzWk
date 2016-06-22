package cn.wizzer.nutzwk.services.sys;

import cn.wizzer.nutzwk.models.sys.Sys_menu;
import cn.wizzer.nutzwk.models.sys.Sys_role;
import cn.wizzer.nutzwk.models.sys.Sys_user;
import cn.wizzer.common.base.BaseService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/22.
 */
@IocBean(args = {"refer:dao"})
public class RoleService  extends BaseService<Sys_user> {
    public RoleService(Dao dao) {
        super(dao);
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
