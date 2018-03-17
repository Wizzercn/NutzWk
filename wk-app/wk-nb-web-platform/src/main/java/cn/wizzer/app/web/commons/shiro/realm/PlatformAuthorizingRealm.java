package cn.wizzer.app.web.commons.shiro.realm;

import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysRoleService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.app.web.commons.shiro.exception.CaptchaEmptyException;
import cn.wizzer.app.web.commons.shiro.exception.CaptchaIncorrectException;
import cn.wizzer.app.web.commons.shiro.token.CaptchaToken;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
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
@IocBean
public class PlatformAuthorizingRealm extends AuthorizingRealm {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysUserService sysUserService;
    @Inject
    @Reference
    private SysRoleService roleService;
    @Inject
    private RedisService redisService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        CaptchaToken authcToken = (CaptchaToken) token;
        String loginname = authcToken.getUsername();
        String captcha = authcToken.getCaptcha();
        if (Strings.isBlank(loginname)) {
            throw Lang.makeThrow(AuthenticationException.class, "Account name is empty");
        }
        int errCount = NumberUtils.toInt(Strings.sNull(SecurityUtils.getSubject().getSession(true).getAttribute("platformErrCount")));
        if (errCount > 2) {
            //输错三次显示验证码窗口
            if (Strings.isBlank(captcha)) {
                throw Lang.makeThrow(CaptchaEmptyException.class, "Captcha is empty");
            }
            String _captcha = redisService.get("platformCaptcha");
            if (!authcToken.getCaptcha().equalsIgnoreCase(_captcha)) {
                throw Lang.makeThrow(CaptchaIncorrectException.class, "Captcha is error");
            }
        }
        Sys_user user = sysUserService.fetch(Cnd.where("loginname", "=", loginname));
        if (Lang.isEmpty(user)) {
            throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", loginname);
        }
        if (user.isDisabled()) {
            throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", loginname);
        }
        sysUserService.fetchLinks(user, null);
        sysUserService.fillMenu(user);
        Session session = SecurityUtils.getSubject().getSession(true);
        session.setAttribute("platformErrCount", 0);
        session.setAttribute("platform_uid", user.getId());
        session.setAttribute("platform_username", user.getUsername());
        session.setAttribute("platform_loginname", user.getLoginname());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return info;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Sys_user user = (Sys_user) principals.getPrimaryPrincipal();
        if (!Lang.isEmpty(user) && !user.isDisabled()) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRoles(sysUserService.getRoleCodeList(user));
            for (Sys_role role : user.getRoles()) {
                if (!role.isDisabled())
                    info.addStringPermissions(roleService.getPermissionNameList(role));
            }
            return info;
        } else {
            return null;
        }
    }

    public PlatformAuthorizingRealm() {
        this(null, null);
    }

    public PlatformAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
        setAuthenticationTokenClass(CaptchaToken.class);
    }

    public PlatformAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public PlatformAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }
}