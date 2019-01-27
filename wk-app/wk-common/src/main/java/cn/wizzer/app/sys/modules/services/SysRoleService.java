package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.framework.page.Pagination;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysRoleService extends BaseService<Sys_role> {
    /**
     * 通过角色ID获取菜单及数据权限
     *
     * @param roleId
     * @return
     */
    List<Sys_menu> getMenusAndButtons(String roleId);

    /**
     * 通过角色ID获取菜单数据
     *
     * @return
     */
    List<Sys_menu> getDatas(String roleId);

    /**
     * 获取所有菜单数据
     *
     * @return
     */
    List<Sys_menu> getDatas();

    /**
     * 通过角色获取权限标识符
     *
     * @param role
     * @return
     */
    List<String> getPermissionNameList(Sys_role role);

    /**
     * 删除角色
     *
     * @param roleid
     */
    void del(String roleid);

    /**
     * 批量删除角色
     *
     * @param roleids
     */
    void del(String[] roleids);

    /**
     * 通过角色ID和菜单父ID获取下级权限菜单
     *
     * @param roleId
     * @param pid
     * @return
     */
    List<Sys_menu> getRoleMenus(String roleId, String pid);

    /**
     * 判断角色是否有下级数据权限
     *
     * @param roleId
     * @param pid
     * @return
     */
    boolean hasChildren(String roleId, String pid);

    /**
     * 查询用户
     *
     * @param roleId
     * @param keyword
     * @param isAdmin
     * @param sysUnit
     * @return
     */
    Pagination userSearch(String roleId, String keyword, boolean isAdmin, Sys_unit sysUnit);

    /**
     * 清空缓存
     */
    void clearCache();
}
