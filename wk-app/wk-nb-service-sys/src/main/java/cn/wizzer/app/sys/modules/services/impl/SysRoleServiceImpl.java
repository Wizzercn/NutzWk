package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.app.sys.modules.services.SysRoleService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.framework.page.Pagination;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.DB;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysRoleService.class)
@CacheDefaults(cacheName = "sys_role")
public class SysRoleServiceImpl extends BaseServiceImpl<Sys_role> implements SysRoleService {
    public SysRoleServiceImpl(Dao dao) {
        super(dao);
    }

    @CacheResult
    public List<Sys_menu> getMenusAndButtons(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and" +
                " b.roleId=@roleId and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        sql.params().set("f", false);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    @CacheResult
    public List<Sys_menu> getDatas(String roleId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and" +
                " b.roleId=@roleId and a.type='data' and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        sql.params().set("f", false);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    @CacheResult
    public List<Sys_menu> getDatas() {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and a.type='data' order by a.location ASC,a.path asc");
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询权限
     *
     * @param role
     * @return
     */
    //如果传参是对象,那么要取字符串做为cacheKey值,因为对象的标识是变动的
    @CacheResult(cacheKey = "${args[0].id}_getPermissionNameList")
    public List<String> getPermissionNameList(Sys_role role) {
        dao().fetchLinks(role, "menus");
        List<String> list = new ArrayList<String>();
        for (Sys_menu menu : role.getMenus()) {
            if (!Strings.isEmpty(menu.getPermission()) && !menu.isDisabled()) {
                list.add(menu.getPermission());
            }
        }
        return list;
    }

    @Aop(TransAop.READ_COMMITTED)
    public void del(String roleid) {
        this.dao().clear("sys_user_role", Cnd.where("roleId", "=", roleid));
        this.dao().clear("sys_role_menu", Cnd.where("roleId", "=", roleid));
        this.delete(roleid);
    }

    @Aop(TransAop.READ_COMMITTED)
    public void del(String[] roleids) {
        this.dao().clear("sys_user_role", Cnd.where("roleId", "in", roleids));
        this.dao().clear("sys_role_menu", Cnd.where("roleId", "in", roleids));
        this.delete(roleids);
    }

    /**
     * @param roleId
     * @param pid
     * @return
     */
    @CacheResult
    public List<Sys_menu> getRoleMenus(String roleId, String pid) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                "$m and b.roleId=@roleId and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        sql.params().set("f", false);
        if (Strings.isNotBlank(pid)) {
            sql.vars().set("m", "a.parentId='" + pid + "'");
        } else {
            sql.vars().set("m", "(a.parentId='' or a.parentId is null)");
        }
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * @param roleId
     * @param pid
     * @return
     */
    @CacheResult
    public boolean hasChildren(String roleId, String pid) {
        Sql sql = Sqls.create("select count(*) from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                "$m and b.roleId=@roleId and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("roleId", roleId);
        sql.params().set("f", false);
        if (Strings.isNotBlank(pid)) {
            sql.vars().set("m", "a.parentId='" + pid + "'");
        } else {
            sql.vars().set("m", "(a.parentId='' or a.parentId is null)");
        }
        sql.setCallback(Sqls.callback.integer());
        dao().execute(sql);
        return sql.getInt() > 0;
    }

    /**
     * 查询用户
     *
     * @param roleId
     * @param keyword
     * @param isAdmin
     * @param sysUnit
     * @return
     */
    public Pagination userSearch(String roleId, String keyword, boolean isAdmin, Sys_unit sysUnit) {
        Sql sql;
        if (DB.ORACLE.name().equals(this.dao().getJdbcExpert().getDatabaseType())) {
            //拼接字符串兼容oracle
            sql = Sqls.create("SELECT a.id AS VALUE,a.loginname||'('||a.username||')' AS label,a.disabled,a.unitid,b.name as unitname FROM sys_user a,sys_unit b WHERE a.unitid=b.id  and a.id NOT IN(SELECT b.userId FROM sys_user_role b WHERE b.roleId=@roleId) $s1 $s2 order by a.opAt desc");
        } else {
            sql = Sqls.create("SELECT a.id AS VALUE,CONCAT(a.loginname,'(',a.username,')') AS label,a.disabled,a.unitid,b.name as unitname FROM sys_user a,sys_unit b WHERE a.unitid=b.id  and a.id NOT IN(SELECT b.userId FROM sys_user_role b WHERE b.roleId=@roleId) $s1 $s2 order by a.opAt desc");
        }
        sql.params().set("roleId", roleId);
        if (!isAdmin) {
            //非超级管理员只可查询本单位及下级单位用户
            String menuPath = sysUnit.getPath();
            sql.vars().set("s1", " and b.path like '" + menuPath + "%'");
        }
        if (Strings.isNotBlank(keyword)) {
            sql.vars().set("s2", " and (a.loginname like '%" + keyword + "%' or a.username like '%" + keyword + "%')");
        }
        return this.listPage(1, 10, sql);
    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
