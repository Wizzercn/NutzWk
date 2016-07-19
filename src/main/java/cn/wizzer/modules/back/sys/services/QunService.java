package cn.wizzer.modules.back.sys.services;

import java.util.List;

import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.sys.models.Sys_qun_black_user;

/**
 * 群用户服务
 */
@IocBean(args = {"refer:dao"})
public class QunService extends Service<Sys_qun_black_user> {

	
    public QunService(Dao dao) {
        super(dao);
    }
    
    /**
     * 新增QQ
     *
     * @param unit
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_qun_black_user qq) {
        dao().insert(qq);
    }


    /**
     * 查询用户按钮权限
     *
     * @param userId
     * @return
     */
    public List<Sys_qun_black_user> getDatas(String contact) {
        Sql sql = Sqls.create("select * from sys_qun_black_user where contact = @contact order by createdAt DESC limit 10");
        sql.params().set("contact", contact);
        Entity<Sys_qun_black_user> entity = dao().getEntity(Sys_qun_black_user.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_qun_black_user.class);
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
}