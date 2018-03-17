package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.framework.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysRoleService extends BaseService<Sys_role> {
    /**
     * 通过角色ID获取菜单及数据权限
     * @param roleId
     * @return
     */
    List<Sys_menu> getMenusAndButtons(String roleId);

    /**
     * 通过角色ID获取菜单数据
     * @return
     */
    List<Sys_menu> getDatas(String roleId);

    /**
     * 获取所有菜单数据
     * @return
     */
    List<Sys_menu> getDatas();

    /**
     * 通过角色获取权限标识符
     * @param role
     * @return
     */
    List<String> getPermissionNameList(Sys_role role);

    /**
     * 删除角色
     * @param roleid
     */
    void del(String roleid);

    /**
     * 批量删除角色
     * @param roleids
     */
    void del(String[] roleids);
}
