package cn.wizzer.common.filter;

import cn.wizzer.common.base.Globals;
import cn.wizzer.modules.back.sys.models.Sys_route;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Wizzer on 2016/7/31.
 */
public class RouteFilter implements Filter {
    private static final Log log = Logs.get();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req2 = (HttpServletRequest) req;
        HttpServletResponse res2 = (HttpServletResponse) res;
        Sys_route route = Globals.RouteMap.get(req2.getRequestURI());
        if (route != null) {
            if ("show".equals(route.getType())) {
                res2.sendRedirect(route.getToUrl());
            } else {
                res2.setCharacterEncoding("utf-8");
                req2.getRequestDispatcher(route.getToUrl()).forward(req2, res2);
            }
        } else chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
