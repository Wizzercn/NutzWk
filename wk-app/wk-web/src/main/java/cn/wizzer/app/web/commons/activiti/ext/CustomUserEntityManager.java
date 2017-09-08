package cn.wizzer.app.web.commons.activiti.ext;

import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysUserService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户工厂类
 * Created by wizzer on 15-4-24.
 */
@IocBean
public class CustomUserEntityManager extends UserEntityManager {
    Dao dao = Mvcs.getIoc().get(Dao.class);
    private final Log log = Logs.get();

    @Override
    public User findUserById(String userId) {
        log.debug("findUserById:::::::::::::::::::::::::::::::" + userId);
        UserEntity userEntity = new UserEntity();
        Sys_user sysUser = dao.fetch(Sys_user.class, Cnd.where("id", "=", userId));
        userEntity.setId(userId);
        userEntity.setFirstName(sysUser.getUsername());
        userEntity.setEmail(sysUser.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        // TODO Auto-generated method stub
        log.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Sql sql = Sqls.create("SELECT a.* FROM sys_role a,sys_user_role b WHERE a.id=b.roleid AND b.userid=@c");
        sql.params().set("c", userId);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<Record> list = sql.getList(Record.class);
        List<Group> groupList = new ArrayList<Group>();
        for (Record m : list) {
            GroupEntity group = new GroupEntity();
            group.setId(Strings.sNull(m.get("id")));
            group.setName(Strings.sNull(m.get("name")));
            group.setType("assignment");
            group.setRevision(1);
            groupList.add(group);
        }
        return groupList;
    }


}