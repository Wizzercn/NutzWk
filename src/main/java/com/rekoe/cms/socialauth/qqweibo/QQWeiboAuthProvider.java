package com.rekoe.cms.socialauth.qqweibo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.exception.ServerDataException;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.exception.UserDeniedPermissionException;
import org.brickred.socialauth.oauthstrategy.OAuth1;
import org.brickred.socialauth.oauthstrategy.OAuthStrategyBase;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.OAuthConfig;
import org.brickred.socialauth.util.Response;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.cms.socialauth.AbstractOAuthProvider;

/**
 * 实现腾讯微博帐号登录,OAuth1
 * 
 * @author wendal
 */
@SuppressWarnings("serial")
public class QQWeiboAuthProvider extends AbstractOAuthProvider {

	private static final Log log = Logs.get();
	private final String PROFILE_URL = "http://open.t.qq.com/api/user/info?format=json";
	public QQWeiboAuthProvider(final OAuthConfig providerConfig) throws Exception {
		super(providerConfig);
		ENDPOINTS.put(Constants.OAUTH_REQUEST_TOKEN_URL, "https://open.t.qq.com/cgi-bin/request_token");
		ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL, "https://open.t.qq.com/cgi-bin/authorize");
		ENDPOINTS.put(Constants.OAUTH_ACCESS_TOKEN_URL, "https://open.t.qq.com/cgi-bin/access_token");
		AllPerms = new String[] {};
		AuthPerms = new String[] {};
		authenticationStrategy = new OAuth1(config, ENDPOINTS);
		authenticationStrategy.setPermission(scope);
		authenticationStrategy.setScope(getScope());
	}

	protected Profile doVerifyResponse(final Map<String, String> requestParams) throws Exception {
		log.info("Retrieving Access Token in verify response function");
		if (requestParams.get("error_reason") != null && "user_denied".equals(requestParams.get("error_reason"))) {
			throw new UserDeniedPermissionException();
		}
		accessGrant = authenticationStrategy.verifyResponse(requestParams, verifyResponseMethod());

		if (accessGrant != null) {
			log.debug("Obtaining user profile");
			// try {
			// String presp =
			// authenticationStrategy.executeFeed(PROFILE_URL).getResponseBodyAsString("utf8");
			// System.out.println(Json.toJson(Json.fromJson(presp)));
			// } catch (Throwable e) {
			// e.printStackTrace();
			// }
			Profile p = new Profile();
			p.setValidatedId(requestParams.get("openid"));
			p.setProviderId(getProviderId());
			userProfile = p;
			return p;
		} else {
			throw new SocialAuthException("Access token not found");
		}
	}

	@SuppressWarnings("unchecked")
	protected Profile authLogin() throws Exception {
		String presp;
		try {
			Response response = authenticationStrategy.executeFeed(PROFILE_URL);
			presp = response.getResponseBodyAsString(Constants.ENCODING);
		} catch (Exception e) {
			throw new SocialAuthException("Error while getting profile from " + PROFILE_URL, e);
		}
		try {
			// System.out.println("User Profile : " + presp);
			Map<String, Object> data = Json.fromJson(Map.class, presp);
			if (!"ok".equals(data.get("msg")))
				throw new SocialAuthException("Error: " + presp);
			if (userProfile == null)
				userProfile = new Profile();
			data = (Map<String, Object>) data.get("data");
			userProfile.setValidatedId(data.get("uid").toString());
			return userProfile;
		} catch (Exception ex) {
			throw new ServerDataException("Failed to parse the user profile json : " + presp, ex);
		}
	}

	@Override
	public Response updateStatus(String arg0) throws Exception {
		log.warn("WARNING: Not implemented for QQAuthProvider");
		throw new SocialAuthException("Update Status is not implemented for QQAuthProvider");
	}

	@Override
	public Response uploadImage(String arg0, String arg1, InputStream arg2) throws Exception {
		log.warn("WARNING: Not implemented for QQAuthProvider");
		throw new SocialAuthException("Upload Image is not implemented for QQAuthProvider");
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

	@Override
	protected String getPlatform() {
		return "qqweibo";
	}
}
