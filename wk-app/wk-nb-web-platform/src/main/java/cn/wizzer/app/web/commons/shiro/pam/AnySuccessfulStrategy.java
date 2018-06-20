package cn.wizzer.app.web.commons.shiro.pam;

import cn.wizzer.app.web.commons.shiro.exception.CaptchaEmptyException;
import cn.wizzer.app.web.commons.shiro.exception.CaptchaIncorrectException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import java.util.Collection;

@IocBean(name = "authenticationStrategy")
public class AnySuccessfulStrategy extends AbstractAuthenticationStrategy {

    /**
     * Returns {@code null} immediately, relying on this class's {@link #merge
     * merge} implementation to return only the first {@code info} object it
     * encounters, ignoring all subsequent ones.
     */
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return null;
    }

    /**
     * Returns the specified {@code aggregate} instance if is non null and valid
     * (that is, has principals and they are not empty) immediately, or, if it
     * is null or not valid, the {@code info} argument is returned instead.
     * <p>
     * This logic ensures that the first valid info encountered is the one
     * retained and all subsequent ones are ignored, since this strategy
     * mandates that only the info from the first successfully authenticated
     * realm be used.
     */
    protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        if (aggregate != null && !Lang.isEmpty(aggregate.getPrincipals())) {
            return aggregate;
        }
        return info != null ? info : aggregate;
    }

    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        if (singleRealmInfo == null) {
            if (t.getClass().isAssignableFrom(CaptchaIncorrectException.class)) {
                throw Lang.makeThrow(CaptchaIncorrectException.class, t.getMessage());
            } else if (t.getClass().isAssignableFrom(CaptchaEmptyException.class)) {
                throw Lang.makeThrow(CaptchaEmptyException.class, t.getMessage());
            } else if (t.getClass().isAssignableFrom(LockedAccountException.class)) {
                throw Lang.makeThrow(LockedAccountException.class, t.getMessage());
            } else if (t.getClass().isAssignableFrom(UnknownAccountException.class)) {
                throw Lang.makeThrow(UnknownAccountException.class, t.getMessage());
            } else if (t.getClass().isAssignableFrom(IncorrectCredentialsException.class)) {
                throw Lang.makeThrow(IncorrectCredentialsException.class, t.getMessage());
            } else if (t.getClass().isAssignableFrom(ExcessiveAttemptsException.class)) {
                throw Lang.makeThrow(ExcessiveAttemptsException.class, t.getMessage());
            }
            throw Lang.makeThrow(AuthenticationException.class, t.getMessage());
        }
        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
    }

}
