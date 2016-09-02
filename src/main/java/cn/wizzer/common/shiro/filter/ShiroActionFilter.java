package cn.wizzer.common.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.DefaultViewMaker;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.VoidView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import cn.wizzer.common.shiro.interceptor.ShiroAnnotationsAuthorizingMethodInterceptor;

public class ShiroActionFilter implements ActionFilter {
    private static Log log = Logs.get();

    public View match(final ActionContext actionContext) {
        try {
            log.debug("ShiroActionFilter..............");
            ShiroAnnotationsAuthorizingMethodInterceptor.DEFAULT_AUTH.assertAuthorized(new MethodInvocation() {

                public Object proceed() throws Throwable {
                    throw Lang.noImplement();
                }

                public Object getThis() {
                    return actionContext.getModule();
                }

                public Method getMethod() {
                    return actionContext.getMethod();
                }

                public Object[] getArguments() {
                    return actionContext.getMethodArgs();
                }
            });
        } catch (UnauthenticatedException e) {
            return whenAuthFail(actionContext, e);
        } catch (UnauthorizedException e) {
            return permissionFail(actionContext, e);
        } catch (AuthorizationException e) {
            return permissionFail(actionContext, e);
        }
        return null;
    }

    private View view = new ServerRedirectView("/");
    private View NOT_PERMISSION = new ServerRedirectView("/private/login");

    public ShiroActionFilter() {

    }

    public ShiroActionFilter(String view) {
        if (view.contains(":")) {
            String[] vs = view.split(":", 2);
            this.view = new DefaultViewMaker().make(null, vs[0], vs[1]);
        } else {
            this.view = new ServerRedirectView(view);
        }
    }

    private View whenAuthFail(ActionContext ctx, AuthorizationException e) {
        HttpServletRequest localHttpServletRequest = ctx.getRequest();
        HttpServletResponse localHttpServletResponse = ctx.getResponse();
        String str = localHttpServletRequest.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(str) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
            localHttpServletResponse.addHeader("loginStatus", "accessDenied");
            try {
                localHttpServletResponse.sendError(403);
            } catch (IOException exception) {
                log.error(exception.getMessage());
            }
            return new VoidView();
        }
        return view;
    }

    private View permissionFail(ActionContext ctx, AuthorizationException e) {
        HttpServletRequest localHttpServletRequest = ctx.getRequest();
        HttpServletResponse localHttpServletResponse = ctx.getResponse();
        String str = localHttpServletRequest.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(str) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
            localHttpServletResponse.addHeader("loginStatus", "unauthorized");
            try {
                localHttpServletResponse.sendError(403);
            } catch (IOException exception) {
                log.error(exception.getMessage());
            }
            return new VoidView();
        }
        return NOT_PERMISSION;
    }
}
