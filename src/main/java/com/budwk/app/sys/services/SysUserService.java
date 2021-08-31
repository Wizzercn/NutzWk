package com.budwk.app.sys.services;


import com.budwk.app.base.exception.BaseException;
import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.models.Sys_user;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysUserService extends BaseService<Sys_user> {
    /**
     * 获取用户权限标识
     *
     * @param userId
     * @return
     */
    List<String> getPermissionList(String userId);

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
     *
     * @param userId
     */
    void deleteCache(String userId);

    /**
     * 清空缓存
     */
    void clearCache();

    /**
     * 检查用户是否存在
     *
     * @param loginname 用户名
     */
    void checkLoginname(String loginname) throws BaseException;

    /**
     * 检查用户是否存在
     *
     * @param mobile 手机号码
     */
    void checkMobile(String mobile) throws BaseException;

    /**
     * 通过用户名和密码获取用户信息
     *
     * @param loginname 用户名
     * @param passowrd  密码
     * @return
     * @throws BaseException
     */
    Sys_user loginByPassword(String loginname, String passowrd) throws BaseException;

    /**
     * 通过短信验证码登录
     *
     * @param mobile 手机号
     * @return
     * @throws BaseException
     */
    Sys_user loginByMobile(String mobile) throws BaseException;

    /**
     * 通过用户名获取用户信息
     *
     * @param loginname 用户名
     * @return
     * @throws BaseException
     */
    Sys_user getUserByLoginname(String loginname) throws BaseException;

    /**
     * 通过用户ID获取用户信息
     *
     * @param id 用户ID
     * @return
     * @throws BaseException
     */
    Sys_user getUserById(String id) throws BaseException;

    /**
     * 获取登录用户及菜单信息
     *
     * @param userId 用户ID
     * @return
     */
    Sys_user getUserAndMenuById(String userId);

    /**
     * 通过用户名重置密码
     *
     * @param loginname 用户名
     * @param password  新密码
     * @throws BaseException
     */
    void setPwdByLoginname(String loginname, String password) throws BaseException;

    /**
     * 通过用户ID重置密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @throws BaseException
     */
    void setPwdById(String id, String password) throws BaseException;

    /**
     * 设置用户自定义样式
     *
     * @param id          用户ID
     * @param themeConfig 样式内容
     */
    void setThemeConfig(String id, String themeConfig);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param ip     IP地址
     */
    void setLoginInfo(String userId, String ip);
}
