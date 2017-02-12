package cn.wizzer.framework.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 使用2级缓存时, session的touch操作太频繁了,改成1分钟一次(默认)
 * Created by wizzer on 2017/1/11.
 */
public class WkShiroFilter extends AbstractShiroFilter {
    private static final Logger log = LoggerFactory.getLogger(WkShiroFilter.class);
    private static final int TOUCH_TIME = 60 * 1000;

    protected void updateSessionLastAccessTime(ServletRequest request, ServletResponse response) {
        if (!isHttpSessions()) { //'native' sessions
            Subject subject = SecurityUtils.getSubject();
            //Subject should never _ever_ be null, but just in case:
            if (subject != null) {
                Session session = subject.getSession(false);
                // 存在且超过1分钟,才touch
                if (session != null && System.currentTimeMillis() - session.getLastAccessTime().getTime() > TOUCH_TIME) {
                    try {
                        session.touch();
                    } catch (Throwable t) {
                        log.error("session.touch() method invocation has failed.  Unable to update" +
                                "the corresponding session's last access time based on the incoming request.", t);
                    }
                }
            }
        }
    }

    protected WkShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
        if(webSecurityManager == null) {
            throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
        } else {
            this.setSecurityManager(webSecurityManager);
            if(resolver != null) {
                this.setFilterChainResolver(resolver);
            }

        }
    }
}
