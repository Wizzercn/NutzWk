package cn.wizzer.framework.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.nutz.mvc.SessionProvider;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 代理Nutz内部使用Session的调用为Shiro的Shiro的session
 * @author wendal
 *
 */
public class ShiroSessionProvider implements SessionProvider {

	public HttpServletRequest filter(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			resp.addHeader("Access-Control-Allow-Origin", "*");
			resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Key");
		}
		if (req instanceof ShiroHttpServletRequest)
			return req;
		return new ShiroHttpServletRequest(req, servletContext, true);
	}

	public void notifyStop() {
	}

}