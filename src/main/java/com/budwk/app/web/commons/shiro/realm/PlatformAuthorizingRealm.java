package com.budwk.app.web.commons.shiro.realm;

import com.budwk.app.web.commons.exception.CaptchaException;
import com.budwk.app.web.commons.ext.validate.ValidateService;
import com.budwk.app.web.commons.shiro.exception.CaptchaEmptyException;
import com.budwk.app.sys.models.Sys_role;
import com.budwk.app.sys.models.Sys_user;
import com.budwk.app.sys.services.SysRoleService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.shiro.token.PlatformCaptchaToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.nutz.castor.Castors;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * Created by wizzer on 2017/1/11.
 */
@IocBean(name = "platformRealm")
public class PlatformAuthorizingRealm extends AuthorizingRealm {
    private static final Log log = Logs.get();
    @Inject
    private SysUserService sysUserService;
    @Inject
    private SysRoleService sysRoleService;
    @Inject
    private RedisService redisService;
    @Inject
    private ValidateService validateService;

    protected SysUserService getUserService() {
        return sysUserService;
    }

    protected SysRoleService getRoleService() {
        return sysRoleService;
    }

    protected RedisService getRedisService() {
        return redisService;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getClass().isAssignableFrom(PlatformCaptchaToken.class)) {
            PlatformCaptchaToken authcToken = (PlatformCaptchaToken) token;
            String loginname = authcToken.getUsername();
            String captcha = authcToken.getCaptcha();
            String key = authcToken.getKey();
            if (Strings.isBlank(loginname)) {
                throw Lang.makeThrow(AuthenticationException.class, "登录账号不可为空");
            }
            Session session = SecurityUtils.getSubject().getSession(true);
            try{
                validateService.checkCode(key,captcha);
            }catch (CaptchaException ex){
                throw Lang.makeThrow(CaptchaEmptyException.class, "验证码错误", loginname);
            }
            Sys_user user = getUserService().fetch(Cnd.where("loginname", "=", loginname));
            if (Lang.isEmpty(user)) {
                throw Lang.makeThrow(UnknownAccountException.class, "账号不存在", loginname);
            }
            if (user.isDisabled()) {
                throw Lang.makeThrow(LockedAccountException.class, "账号被禁用", loginname);
            }
            user = getUserService().fetchLinks(user, null);
            user = getUserService().fillMenu(user);
            session.setAttribute("platform_uid", user.getId());
            session.setAttribute("platform_username", user.getUsername());
            session.setAttribute("platform_loginname", user.getLoginname());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword().toCharArray(), ByteSource.Util.bytes(user.getSalt()), getName());
            info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
            return info;
        }
        return null;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object object = principals.getPrimaryPrincipal();
        if (object.getClass().isAssignableFrom(Sys_user.class)) {
            Sys_user user = Castors.me().castTo(object, Sys_user.class);
            if (!Lang.isEmpty(user) && !user.isDisabled()) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                info.addRoles(getUserService().getRoleCodeList(user));
                for (Sys_role role : user.getRoles()) {
                    if (!role.isDisabled())
                        info.addStringPermissions(getRoleService().getPermissionNameList(role));
                }
                return info;
            } else {
                return null;
            }
        }
        return null;
    }

    public PlatformAuthorizingRealm() {
        this(null, null);
    }

    public PlatformAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        setAuthenticationTokenClass(PlatformCaptchaToken.class);
        setCredentialsMatcher(hashedCredentialsMatcher);
    }

    public PlatformAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public PlatformAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

}