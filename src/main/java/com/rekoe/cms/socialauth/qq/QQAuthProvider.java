package com.rekoe.cms.socialauth.qq;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.oauthstrategy.OAuth2;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.OAuthConfig;
import org.brickred.socialauth.util.Response;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.rekoe.cms.socialauth.AbstractOAuthProvider;

/**
 * 实现QQ帐号登录,OAuth2
 * 
 * @author wendal
 */
@SuppressWarnings("serial")
public class QQAuthProvider extends AbstractOAuthProvider {

	private final static Log log = Logs.get();
	private String PROFILE_URL= "https://graph.qq.com/oauth2.0/me";
	static {
		ENDPOINTS.put(Constants.OAUTH_AUTHORIZATION_URL, "https://graph.qq.com/oauth2.0/authorize");
		ENDPOINTS.put(Constants.OAUTH_ACCESS_TOKEN_URL, "https://graph.qq.com/oauth2.0/token");
		AllPerms = new String[] { "get_user_info", "get_info" };
		AuthPerms = new String[] { "get_user_info", "get_info" };
	}

	public QQAuthProvider(final OAuthConfig providerConfig) throws Exception {
		super(providerConfig);
		authenticationStrategy = new OAuth2(config, ENDPOINTS);
		authenticationStrategy.setPermission(scope);
		authenticationStrategy.setScope(getScope());
	}

	protected Profile authLogin() throws Exception {
		String presp;
		try {
			Response response = authenticationStrategy.executeFeed(PROFILE_URL);
			presp = response.getResponseBodyAsString(Constants.ENCODING);
			if (presp != null) {
				presp = presp.trim().intern();
				if (presp.startsWith("callback(") && presp.endsWith(");")) {
					presp = presp.substring(presp.indexOf("{"), presp.indexOf("}") + 1);
					Map<String, String> map = Json.fromJsonAsMap(String.class, presp);
					if (map.get("openid") != null) {
						Profile p = new Profile();
						p.setValidatedId(map.get("openid")); // QQ定义的
						p.setProviderId(getProviderId());
						userProfile = p;
						try {
							Map<String, String> params = new HashMap<String, String>();
							params.put("format", "json");
							params.put("openid", map.get("openid"));
							params.put("oauth_consumer_key", config.get_consumerKey());
							response = authenticationStrategy.executeFeed("https://graph.qq.com/user/get_user_info", "GET", params, null, null);
							presp = response.getResponseBodyAsString(Constants.ENCODING);
							Map<String, String> user_info = Json.fromJsonAsMap(String.class, presp);
							boolean isRight = NumberUtils.toInt(user_info.get("ret"), -1) == 0;
							if (isRight) { // 获取成功
								if (user_info.get("nickname") != null)
									p.setDisplayName(user_info.get("nickname"));
								if (user_info.get("figureurl") != null)
									p.setProfileImageURL(user_info.get("figureurl"));
								if (user_info.get("gender") != null)
									p.setGender(user_info.get("gender"));
							}
						} catch (Throwable e) {
							log.error(e);
						}
						return p;
					}
				}
			}
			throw new SocialAuthException("QQ Error : " + presp);
		} catch (Exception e) {
			throw new SocialAuthException("Error while getting profile from " + PROFILE_URL, e);
		}
	}

	@Override
	protected String getPlatform() {
		return "QQ";
	}
}