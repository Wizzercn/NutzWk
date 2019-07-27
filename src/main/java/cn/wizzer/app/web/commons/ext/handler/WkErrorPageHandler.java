package cn.wizzer.app.web.commons.ext.handler;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 错误页拦截器,登陆后台显示友好提示
 */
@IocBean
public class WkErrorPageHandler extends ErrorPageErrorHandler {
    private static final Log log = Logs.get();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (response.getStatus() == 403 || response.getStatus() == 404 || response.getStatus() == 500) {
            try {
                if (isAjax(request)) {
                    response.getWriter().write(Json.toJson(new NutMap("code", "-1").setv("msg", response.getStatus() + " error")));
                    return;
                } else {
                    request.setAttribute("original_request_uri", request.getRequestURI());
                    RequestDispatcher rd = request.getRequestDispatcher("/platform/home/" + response.getStatus());
                    rd.forward(request, response);
                    return;
                }
            } catch (ServletException e) {
                log.error(e);
            }
        }
        super.handle(target, baseRequest, request, response);
    }

    private boolean isAjax(ServletRequest req) {
        String value = ((HttpServletRequest) req).getHeader("X-Requested-With");
        return value != null && "XMLHttpRequest".equalsIgnoreCase(value.trim());
    }
}