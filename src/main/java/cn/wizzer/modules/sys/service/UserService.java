package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.*;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.trans.Trans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/6/30.
 */
@IocBean(args = {"refer:dao"})
public class UserService extends BaseService<Sys_user> {
    public UserService(Dao dao) {
        super(dao);
    }

    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_user user, Sys_user_profile profile, String unitId, String[] roleIds) {
        String uid = StringUtils.getUid();
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        String salt = rng.nextBytes().toBase64();
        String hashedPasswordBase64 = new Sha256Hash(user.getPassword(), salt, 1024).toBase64();
        user.setSalt(salt);
        user.setPassword(hashedPasswordBase64);
        user.setCreateUser(uid);
        user.setUsername(user.getUsername().trim());
        Sys_user u = dao().insert(user);
        profile.setUserId(u.getId());
        profile.setCreateUser(uid);
        dao().insert(profile);
        if (!Strings.isEmpty(unitId)) {
            dao().insert("sys_user_unit", Chain.make("user_id", u.getId()).add("unit_id", unitId));
        }
        for (String roleId : roleIds) {
            dao().insert("sys_user_role", Chain.make("user_id", u.getId()).add("role_id", roleId));
        }
    }

    @Aop(TransAop.READ_COMMITTED)
    public void deleteById(String userId) {
        dao().clear("sys_user_unit", Cnd.where("user_id", "=", userId));
        dao().clear("sys_user_role", Cnd.where("user_id", "=", userId));
        dao().clear("sys_user_profile", Cnd.where("user_id", "=", userId));
        dao().clear("sys_user", Cnd.where("id", "=", userId));
    }

    @Aop(TransAop.READ_COMMITTED)
    public void deleteByIds(String[] userId) {
        dao().clear("sys_user_unit", Cnd.where("user_id", "in", userId));
        dao().clear("sys_user_role", Cnd.where("user_id", "in", userId));
        dao().clear("sys_user_profile", Cnd.where("user_id", "in", userId));
        dao().clear("sys_user", Cnd.where("id", "in", userId));
    }

    public void update(Sys_user user) {
        dao().update(user);
    }

    public Sys_user info(String uid) {
        return dao().fetchLinks(fetch(uid), "profile");
    }

    public Sys_unit getUnit(String uid) {
        Record record = dao().fetch("sys_user_unit", Cnd.where("user_id", "=", uid));
        if (record == null) return null;
        return dao().fetch(Sys_unit.class, Cnd.where("id", "=", record.get("unit_id")));
    }

    public String getReolnames(String uid) {
        List<Record> list = list(Sqls.create("select name,unitid from sys_role a,sys_user_role b where a.id=b.role_id and b.user_id=@uid").setParam("uid", uid));
        StringBuilder sb = new StringBuilder();
        for (Record record : list) {
            sb.append(record.get("name"));
            if (Strings.isEmpty(Strings.sNull(record.get("unitid")))) {
                sb.append("[系统]");
            }
            sb.append(",");
        }
        return sb.toString();
    }

    public Sys_user fetchByUsername(String username) {
        return dao().fetchLinks(fetch(Cnd.where("username", "=", username)), "roles");
    }

    public List<Sys_menu> getMenus(String uid) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menu_id and" +
                " b.role_id in(select role_id from sys_user_role where user_id=@userId) and a.is_enabled=1 and a.is_show=1 order by a.location ASC,a.path asc");
        sql.params().set("userId", uid);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    public List<Sys_unit> getUnits(String uid) {
        Sql sql = Sqls.create("select distinct a.* from sys_unit a,sys_user_unit b where a.id=b.unit_id and b.user_id=@userId order by a.location ASC,a.path asc");
        sql.params().set("userId", uid);
        Entity<Sys_unit> entity = dao().getEntity(Sys_unit.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_unit.class);
    }

    public Sys_user_profile getProfile(String uid) {
        return dao().fetch(Sys_user_profile.class, Cnd.where("user_id", "=", uid));
    }

    public List<String> getRoleCodeList(Sys_user user) {
        dao().fetchLinks(user, "roles");
        List<String> roleNameList = new ArrayList<String>();
        for (Sys_role role : user.getRoles()) {
            roleNameList.add(role.getCode());
        }
        return roleNameList;
    }

    public Sys_user fetchByOpenID(String openid) {
        Sys_user user = fetch(Cnd.where("openid", "=", openid));
        if (!Lang.isEmpty(user) && !user.isLocked()) {
            dao().fetchLinks(user, "servers");
            dao().fetchLinks(user, "roles");
        }
        return user;
    }
}

