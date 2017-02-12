package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.framework.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysRoleService extends BaseService<Sys_role> {
    List<Sys_menu> getMenusAndButtons(String roleId);

    List<Sys_menu> getDatas(String roleId);

    List<Sys_menu> getDatas();

    List<String> getPermissionNameList(Sys_role role);
}
