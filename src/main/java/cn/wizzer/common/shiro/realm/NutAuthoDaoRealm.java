package cn.wizzer.common.shiro.realm;

import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.brickred.socialauth.Profile;
import org.nutz.lang.Lang;

public class NutAuthoDaoRealm extends AbstractNutAuthoRealm {

	public NutAuthoDaoRealm() {
		setAuthenticationTokenClass(OAuthToken.class);
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws DisabledAccountException {
		OAuthToken oauthToken = (OAuthToken) token;
		Profile credential = oauthToken.getCredentials();
		String openid = credential.getValidatedId();
		Sys_user user = getUserService().fetchByOpenID(openid);
		if (Lang.isEmpty(user)) {
			String nickName = StringUtils.defaultString(credential.getDisplayName(), openid);
			String providerid = credential.getProviderId();
			//user = getUserService().initUser(nickName, openid, providerid, oauthToken.getAddr());
		}
		if (user.isLocked()) {
			throw Lang.makeThrow(LockedAccountException.class, "Account [ %s ] is locked.", user.getUsername());
		}
		oauthToken.setRname(user.isSystem());
		oauthToken.setUserId(openid);
		SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(user, credential, getName());
		oauthToken.getSession().setAttribute(org.nutz.web.Webs.ME, user);
		return account;
	}
}
