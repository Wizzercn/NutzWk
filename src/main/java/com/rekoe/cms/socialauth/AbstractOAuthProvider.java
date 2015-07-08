package com.rekoe.cms.socialauth;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.brickred.socialauth.AbstractProvider;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.exception.AccessTokenExpireException;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.exception.UserDeniedPermissionException;
import org.brickred.socialauth.oauthstrategy.OAuthStrategyBase;
import org.brickred.socialauth.util.AccessGrant;
import org.brickred.socialauth.util.OAuthConfig;
import org.brickred.socialauth.util.Response;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@SuppressWarnings("serial")
public abstract class AbstractOAuthProvider extends AbstractProvider implements AuthProvider {

	private static final Log log = Logs.get();

	protected Permission scope;
	protected OAuthConfig config;
	protected Profile userProfile;
	protected AccessGrant accessGrant;
	protected OAuthStrategyBase authenticationStrategy;

	protected static String[] AllPerms;
	protected static String[] AuthPerms;
	protected static Map<String, String> ENDPOINTS = new HashMap<String, String>();

	protected abstract String getPlatform();
	public AbstractOAuthProvider(OAuthConfig providerConfig) throws Exception {
		this.config = providerConfig;
	}

	public String getLoginRedirectURL(final String successUrl) throws Exception {
		return authenticationStrategy.getLoginRedirectURL(successUrl);
	}

	public Profile verifyResponse(HttpServletRequest httpReq) throws Exception {
		Map<String, String> params = SocialAuthUtil.getRequestParametersMap(httpReq);
		return doVerifyResponse(params);
	}

	public Profile verifyResponse(Map<String, String> params) throws Exception {
		return doVerifyResponse(params);
	}

	protected Profile doVerifyResponse(final Map<String, String> requestParams) throws Exception {
		log.info("Retrieving Access Token in verify response function");
		if (requestParams.get("error_reason") != null && "user_denied".equals(requestParams.get("error_reason"))) {
			throw new UserDeniedPermissionException();
		}
		accessGrant = authenticationStrategy.verifyResponse(requestParams, verifyResponseMethod());
		if (accessGrant != null) {
			log.debug("Obtaining user profile");
			Profile proFile = authLogin();
			return proFile;
		} else {
			throw new SocialAuthException("Access token not found");
		}
	}

	protected abstract Profile authLogin() throws Exception;

	public Response api(final String url, final String methodType, final Map<String, String> params, final Map<String, String> headerParams, final String body) throws Exception {
		try {
			return authenticationStrategy.executeFeed(url, methodType, params, headerParams, body);
		} catch (Exception e) {
			throw new SocialAuthException("Error while making request to URL : " + url, e);
		}
	}

	public List<Contact> getContactList() throws Exception {
		throw Lang.makeThrow(SocialAuthException.class, "Get contact list is not implemented for %s", getPlatform());
	}

	public void logout() {
		accessGrant = null;
		authenticationStrategy.logout();
	}

	@Override
	public void setPermission(Permission permission) {
		this.scope = permission;
		authenticationStrategy.setPermission(this.scope);
		authenticationStrategy.setScope(getScope());
	}

	public Profile getUserProfile() throws Exception {
		return userProfile;
	}

	public AccessGrant getAccessGrant() {
		return accessGrant;
	}

	public String getProviderId() {
		return config.getId();
	}

	protected String getScope() {
		StringBuffer result = new StringBuffer();
		String arr[] = null;
		if (Permission.AUTHENTICATE_ONLY.equals(scope)) {
			arr = AuthPerms;
		} else if (Permission.CUSTOM.equals(scope) && config.getCustomPermissions() != null) {
			arr = config.getCustomPermissions().split(",");
		} else {
			arr = AllPerms;
		}
		if (arr.length > 0)
			result.append(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			result.append(",").append(arr[i]);
		}
		return result.toString();
	}

	protected String verifyResponseMethod() {
		return "GET";
	}

	@Override
	public void setAccessGrant(AccessGrant accessGrant) throws AccessTokenExpireException, SocialAuthException {
		this.accessGrant = accessGrant;
	}

	@Override
	public Response updateStatus(String arg0) throws Exception {
		throw Lang.makeThrow(SocialAuthException.class, "Update Status is not implemented for %s", getPlatform());
	}

	@Override
	public Response uploadImage(String arg0, String arg1, InputStream arg2) throws Exception {
		throw Lang.makeThrow(SocialAuthException.class, "Update Image is not implemented for %s", getPlatform());
	}

	@Override
	protected OAuthStrategyBase getOauthStrategy() {
		return authenticationStrategy;
	}

	@Override
	protected List<String> getPluginsList() {
		List<String> list = new ArrayList<String>();
		if (config.getRegisteredPlugins() != null && config.getRegisteredPlugins().length > 0) {
			list.addAll(Arrays.asList(config.getRegisteredPlugins()));
		}
		return list;
	}
}
