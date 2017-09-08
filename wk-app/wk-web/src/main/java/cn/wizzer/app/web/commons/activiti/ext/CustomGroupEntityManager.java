package cn.wizzer.app.web.commons.activiti.ext;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
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
 * 分组工厂类
 * Created by wizzer on 15-4-27.
 */
@IocBean
public class CustomGroupEntityManager extends GroupEntityManager {
    Dao dao = Mvcs.getIoc().get(Dao.class);
    private final Log log = Logs.get();

    @Override
    public List<Group> findGroupsByUser(String userId) {
        log.debug("findGroupsByUser:::::::::::::::::::::::::::::::"+userId);

        List<Group> groupList = new ArrayList<Group>();

        Sql sql = Sqls.create("select distinct roleid from sys_user_role where userid=@c");
        sql.params().set("c", userId);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<Record> list = sql.getList(Record.class);
        for (Record m : list) {
            GroupEntity group = new GroupEntity();
            group.setId(Strings.sNull(m.get("roleid")));
            group.setName(Strings.sNull(m.get("roleid")));
            group.setType("assignment");
            group.setRevision(1);
            groupList.add(group);
        }
        return groupList;
    }
}
