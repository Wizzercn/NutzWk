package cn.wizzer.common.shiro.realm;

import cn.wizzer.common.exception.CreateUserSaltException;
import cn.wizzer.common.exception.IncorrectIpException;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.service.UserService;
import cn.wizzer.common.exception.IncorrectCaptchaException;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.util.ByteSource;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import java.util.List;

public class NutDaoRealm extends AbstractNutAuthoRealm {
	private static final Log log = Logs.get();
	public NutDaoRealm() {
		setAuthenticationTokenClass(CaptchaUsernamePasswordToken.class);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		CaptchaUsernamePasswordToken authcToken = (CaptchaUsernamePasswordToken) token;
		String accountName = authcToken.getUsername();
		if (Strings.isBlank(accountName)) {
			throw Lang.makeThrow(AuthenticationException.class, "Account is empty");
		}
		Sys_user user = getUserService().fetchByUsername(authcToken.getUsername());
		if (Lang.isEmpty(user)) {
			throw Lang.makeThrow(UnknownAccountException.class, "Account [ %s ] not found", authcToken.getUsername());
		}
		if(!Strings.sNull(user.getLoginIp()).equals(StringUtils.getRemoteAddr())){
			boolean isCaptchaBlank = Strings.isBlank(authcToken.getCaptcha());
			if (isCaptchaBlank) {
				throw Lang.makeThrow(IncorrectIpException.class, "Captcha is must");
			}
			String _captcha = Strings.sBlank(SecurityUtils.getSubject().getSession(true).getAttribute("captcha"));
			if (!authcToken.getCaptcha().equalsIgnoreCase(_captcha)) {
				throw Lang.makeThrow(IncorrectCaptchaException.class, "Captcha is error");
			}
		}
		if (user.isLocked()) {
			throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", authcToken.getUsername());
		}
		String userSalt = user.getSalt();
		if (Strings.isBlank(userSalt)) {
			throw Lang.makeThrow(CreateUserSaltException.class, "Account [ %s ] is not set PassWord", authcToken.getUsername());
		}
		user.setMenus(getUserService().getMenus(user.getId()));
		user.setUnits(getUserService().getUnits(user.getId()));
		List<String> roles=getUserService().getRoleCodeList(user);
		if(roles!=null&&roles.contains("superadmin")){
			user.setSystem(true);
		}
		ByteSource salt = ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		info.setCredentialsSalt(salt);
		return info;
	}
}
