package com.budwk.app.sys.services;


import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.models.Sys_user;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysUserService extends BaseService<Sys_user> {
    /**
     * 查询用户的角色
     *
     * @param user
     * @return
     */
    List<String> getRoleCodeList(Sys_user user);

    /**
     * 通过用户ID获取菜单及权限
     *
     * @param userId
     * @return
     */
    List<Sys_menu> getMenusAndButtons(String userId);

    /**
     * 通过用户ID获取菜单
     *
     * @param userId
     * @return
     */
    List<Sys_menu> getDatas(String userId);

    /**
     * 绑定菜单到用户
     *
     * @param user
     */
    Sys_user fillMenu(Sys_user user);

    /**
     * 通过用户ID删除用户
     *
     * @param userId
     */
    void deleteById(String userId);

    /**
     * 批量删除用户
     *
     * @param userIds
     */
    void deleteByIds(String[] userIds);

    /**
     * 通过用户ID和菜单父ID获取下级权限菜单
     *
     * @param userId
     * @param pid
     * @return
     */
    List<Sys_menu> getRoleMenus(String userId, String pid);

    /**
     * 判断用户是否有下级数据权限
     *
     * @param userId
     * @param pid
     * @return
     */
    boolean hasChildren(String userId, String pid);

    /**
     * 清除一个用户的缓存
     * @param userId
     */
    void deleteCache(String userId);
    /**
     * 清空缓存
     */
    void clearCache();
}
