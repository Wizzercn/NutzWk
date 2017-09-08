package cn.wizzer.app.web.commons.activiti.ext;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 分组接口类
 * Created by wizzer on 15-4-27.
 */
@IocBean
public class CustomGroupEntityManagerFactory implements SessionFactory {
    private GroupEntityManager groupEntityManager;
    public void setGroupEntityManager(GroupEntityManager groupEntityManager) {
        this.groupEntityManager = groupEntityManager;
    }
    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }
    @Override
    public Session openSession() {
        return groupEntityManager;
    }
}
