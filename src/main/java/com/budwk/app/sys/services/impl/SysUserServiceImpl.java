package com.budwk.app.sys.services.impl;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.exception.BaseException;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import com.budwk.app.base.utils.PwdUtil;
import com.budwk.app.sys.models.Sys_menu;
import com.budwk.app.sys.models.Sys_role;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysMenuService;
import com.budwk.app.sys.services.SysRoleService;
import com.budwk.app.sys.services.SysUserService;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemove;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/12/22.
 */
@IocBean(args = {"refer:dao"})
@CacheDefaults(cacheName = RedisConstant.PLATFORM_REDIS_WKCACHE_PREFIX + "sys_user", isHash = false, cacheLiveTime = 3600)
public class SysUserServiceImpl extends BaseServiceImpl<Sys_user> implements SysUserService {
    public SysUserServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private SysMenuService sysMenuService;
    @Inject
    private SysRoleService sysRoleService;

    @Override
    @CacheResult(cacheKey = "${userId}_getPermissionList")
    public List<String> getPermissionList(String userId) {
        Sys_user user = this.fetch(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        this.fetchLinks(user, "roles");
        if (user.getRoles() == null) {
            return new ArrayList<>();
        }
        List<String> permissionList = new ArrayList<String>();
        for (Sys_role role : user.getRoles()) {
            if (!role.isDisabled()) {
                permissionList.addAll(sysRoleService.getPermissionList(role));
            }
        }
        // 追加public公共角色权限
        permissionList.addAll(sysRoleService.getPermissionList(sysRoleService.fetch(Cnd.where("code", "=", "public"))));
        return permissionList;
    }

    /**
     * 查询用户角色code列表
     *
     * @param user
     * @return
     */
    @CacheResult(cacheKey = "${user.id}_getRoleCodeList")
    public List<String> getRoleCodeList(Sys_user user) {
        dao().fetchLinks(user, "roles");
        List<String> roleNameList = new ArrayList<String>();
        for (Sys_role role : user.getRoles()) {
            if (!role.isDisabled())
                roleNameList.add(role.getCode());
        }
        return roleNameList;
    }

    /**
     * 获取用户菜单
     *
     * @param user
     */
    //如果传参是对象,那么要取字符串做为cacheKey值,因为对象的标识是变动的
    @CacheResult(cacheKey = "${user.id}_fillMenu")
    public Sys_user fillMenu(Sys_user user) {
        user.setMenus(getMenus(user.getId()));
        //计算左侧菜单
        List<Sys_menu> firstMenus = new ArrayList<>();
        Map<String, List<Sys_menu>> secondMenus = new HashMap<>();
        for (Sys_menu menu : user.getMenus()) {
            if (menu.getPath().length() > 4) {
                List<Sys_menu> s = secondMenus.get(getParentPath(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(getParentPath(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        user.setFirstMenus(firstMenus);
        user.setSecondMenus(secondMenus);
        if (!Strings.isBlank(user.getCustomMenu())) {
            user.setCustomMenus(sysMenuService.query(Cnd.where("id", "in", user.getCustomMenu().split(","))));
        }
        return user;
    }

    /**
     * 查询用户菜单权限
     *
     * @param userId
     * @return
     */
    @CacheResult(cacheKey = "${userId}_getMenus")
    public List<Sys_menu> getMenus(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f and a.showit=@t and a.type='menu' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f", false);
        sql.params().set("t", true);
        return sysMenuService.listEntity(sql);
    }

    /**
     * 查询用户菜单和按钮权限
     *
     * @param userId
     * @return
     */
    @CacheResult(cacheKey = "${userId}_getMenusAndButtons")
    public List<Sys_menu> getMenusAndButtons(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f", false);
        return sysMenuService.listEntity(sql);
    }

    /**
     * 查询用户按钮权限
     *
     * @param userId
     * @return
     */
    @CacheResult(cacheKey = "${userId}_getDatas")
    public List<Sys_menu> getDatas(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId  and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f and a.type='data' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f", false);
        return sysMenuService.listEntity(sql);
    }

    /**
     * 删除一个用户
     *
     * @param userId
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteById(String userId) {
        dao().clear("sys_user_unit", Cnd.where("userId", "=", userId));
        dao().clear("sys_user_role", Cnd.where("userId", "=", userId));
        dao().clear("sys_user", Cnd.where("id", "=", userId));
    }

    /**
     * 批量删除用户
     *
     * @param userIds
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteByIds(String[] userIds) {
        dao().clear("sys_user_unit", Cnd.where("userId", "in", userIds));
        dao().clear("sys_user_role", Cnd.where("userId", "in", userIds));
        dao().clear("sys_user", Cnd.where("id", "in", userIds));
    }

    /**
     * @param userId
     * @param pid
     * @return
     */
    @CacheResult(cacheKey = "${userId}_${pid}_getRoleMenus")
    public List<Sys_menu> getRoleMenus(String userId, String pid) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                "$m and b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f", false);
        if (Strings.isNotBlank(pid)) {
            sql.vars().set("m", "a.parentId='" + pid + "'");
        } else {
            sql.vars().set("m", "(a.parentId='' or a.parentId is null)");
        }
        return sysMenuService.listEntity(sql);

    }

    /**
     * @param userId
     * @param pid
     * @return
     */
    @CacheResult(cacheKey = "${userId}_${pid}_hasChildren")
    public boolean hasChildren(String userId, String pid) {
        Sql sql = Sqls.create("select count(*) from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                "$m and b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f", false);
        if (Strings.isNotBlank(pid)) {
            sql.vars().set("m", "a.parentId='" + pid + "'");
        } else {
            sql.vars().set("m", "(a.parentId='' or a.parentId is null)");
        }
        return sysMenuService.count(sql) > 0;
    }

    @CacheRemove(cacheKey = "${userId}_*")
    //可以通过el表达式加 * 通配符来批量删除一批缓存
    public void deleteCache(String userId) {

    }

    @CacheRemoveAll
    public void clearCache() {

    }

    @Override
    public void checkLoginname(String loginname) throws BaseException {
        if (this.count(Cnd.where("loginname", "=", loginname)) < 1) {
            throw new BaseException("用户名不存在");
        }
    }

    @Override
    public void checkMobile(String mobile) throws BaseException {
        if (this.count(Cnd.where("mobile", "=", mobile)) < 1) {
            throw new BaseException("手机号不存在");
        }
    }

    @Override
    public Sys_user loginByPassword(String loginname, String passowrd) throws BaseException {
        Sys_user user = this.fetch(Cnd.where("loginname", "=", loginname));
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        if (user.isDisabled()) {
            throw new BaseException("用户被禁用");
        }
        String hashedPassword = PwdUtil.getPassword(passowrd, user.getSalt());
        if (!Strings.sNull(hashedPassword).equalsIgnoreCase(user.getPassword())) {
            throw new BaseException("密码不正确");
        }
        user = this.fetchLinks(user, "unit");
        return user;
    }

    @Override
    public Sys_user loginByMobile(String mobile) throws BaseException {
        Sys_user user = this.fetch(Cnd.where("mobile", "=", mobile));
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        if (user.isDisabled()) {
            throw new BaseException("用户被禁用");
        }
        user = this.fetchLinks(user, "unit");
        return user;
    }

    // 这里不能用缓存,因为没有 userId 没法用前缀清除,会造成缓存清除不干净
    @Override
    public Sys_user getUserByLoginname(String loginname) throws BaseException {
        Sys_user user = this.fetch(Cnd.where("loginname", "=", loginname));
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        return user;
    }

    @Override
    @CacheResult(cacheKey = "${userId}_getUserById")
    public Sys_user getUserById(String userId) throws BaseException {
        Sys_user user = this.fetch(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        return user;
    }

    @Override
    @CacheResult(cacheKey = "${userId}_getUserAndMenuById")
    public Sys_user getUserAndMenuById(String userId) throws BaseException {
        Sys_user user = this.fetch(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        user = this.fetchLinks(user, null);
        user = this.fillMenu(user);
        return user;
    }

    @Override
    public void setPwdByLoginname(String loginname, String password) throws BaseException {
        Sys_user user = this.fetch(Cnd.where("loginname", "=", loginname));
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        String salt = R.UU32();
        this.update(Chain.make("salt", salt).add("password", PwdUtil.getPassword(password, salt)),
                Cnd.where("loginname", "=", loginname));
        this.deleteCache(user.getId());
    }

    @Override
    public void setPwdById(String id, String password) throws BaseException {
        Sys_user user = this.fetch(id);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        String salt = R.UU32();
        this.update(Chain.make("salt", salt).add("password", PwdUtil.getPassword(password, salt)),
                Cnd.where("id", "=", id));
        this.deleteCache(user.getId());
    }

    @Override
    public void setThemeConfig(String id, String themeConfig) {
        this.update(Chain.make("themeConfig", themeConfig), Cnd.where("id", "=", id));
        this.deleteCache(id);// 清一下缓存
    }

    @Override
    public void setLoginInfo(String userId, String ip) {
        this.update(Chain.make("loginIp", ip).add("loginAt", System.currentTimeMillis()).addSpecial("loginCount", "+1"), Cnd.where("id", "=", userId));
    }
}
