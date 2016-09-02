package cn.wizzer.common.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wizzer on 2016/6/24.
 */
public class SimpleAuthenticationFilter extends org.apache.shiro.web.filter.authc.AuthenticationFilter {

    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return false;
    }

    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ((HttpServletResponse)response).sendError(403);
        return false;
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (pathsMatch(getLoginUrl(), request))
            return true;
        return super.isAccessAllowed(request, response, mappedValue);
    }
}