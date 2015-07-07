package cn.wizzer.common.shiro.realm;

import cn.wizzer.common.exception.CreateUserSaltException;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.util.ByteSource;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

public class UsernamePasswordRealm extends AbstractNutAuthoRealm {

	public UsernamePasswordRealm() {
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		String accountName = authcToken.getUsername();
		if (StringUtils.isBlank(accountName)) {
			throw Lang.makeThrow(AuthenticationException.class, "Account is empty");
		}
		Sys_user user = getUserService().fetchByUsername(authcToken.getUsername());
		if (Lang.isEmpty(user)) {
			throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", authcToken.getUsername());
		}
		if (user.isLocked()) {
			throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", authcToken.getUsername());
		}
		String userSalt = user.getSalt();
		if (Strings.isBlank(userSalt)) {
			throw Lang.makeThrow(CreateUserSaltException.class, "Account [ %s ] is not set PassWord", authcToken.getUsername());
		}
		ByteSource salt = ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		info.setCredentialsSalt(salt);
		return info;
	}
}
