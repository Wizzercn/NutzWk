package cn.wizzer.common.shiro.filter;

import cn.wizzer.common.shiro.realm.CaptchaToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class AuthenticationFilter extends FormAuthenticationFilter implements ActionFilter {
	private String captchaParam = "captcha";

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected AuthenticationToken createToken(HttpServletRequest request) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new CaptchaToken(username, password, rememberMe, host, captcha);
	}

	public View match(ActionContext actionContext) {
		HttpServletRequest request = actionContext.getRequest();
		AuthenticationToken authenticationToken = createToken(request);
		request.setAttribute("loginToken", authenticationToken);
		return null;
	}
}
