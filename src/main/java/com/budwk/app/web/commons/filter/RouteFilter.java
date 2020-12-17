package com.budwk.app.web.commons.filter;

import com.budwk.app.sys.models.Sys_route;
import com.budwk.app.web.commons.base.Globals;
import org.nutz.lang.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Wizzer on 2016/7/31.
 */
public class RouteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req2 = (HttpServletRequest) req;
        HttpServletResponse res2 = (HttpServletResponse) res;
        res2.setCharacterEncoding("utf-8");
        req2.setCharacterEncoding("utf-8");
        Sys_route route = Globals.RouteMap.getAs(Strings.sNull(req2.getRequestURI()).replace(Globals.AppBase, ""), Sys_route.class);
        if (route != null) {
            if ("show".equals(route.getType())) {
                res2.sendRedirect(route.getToUrl());
            } else {
                req2.getRequestDispatcher(route.getToUrl()).forward(req2, res2);
            }
        } else chain.doFilter(req2, res2);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
