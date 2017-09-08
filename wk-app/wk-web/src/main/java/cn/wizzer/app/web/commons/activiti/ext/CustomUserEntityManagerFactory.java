package cn.wizzer.app.web.commons.activiti.ext;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 用户接口类
 * Created by wizzer on 15-4-24.
 */
@IocBean
public class CustomUserEntityManagerFactory implements SessionFactory {
    private UserEntityManager userEntityManager;
    public void setUserEntityManager(UserEntityManager userEntityManager) {
        this.userEntityManager = userEntityManager;
    }
    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }
    @Override
    public Session openSession() {
        return userEntityManager;
    }
}