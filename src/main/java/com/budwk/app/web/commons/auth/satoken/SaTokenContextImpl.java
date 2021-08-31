package com.budwk.app.web.commons.auth.satoken;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.servlet.model.SaResponseForServlet;
import cn.dev33.satoken.servlet.model.SaStorageForServlet;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;

/**
 * @author wizzer@qq.com
 */
@IocBean
public class SaTokenContextImpl implements SaTokenContext {
    @Override
    public SaRequest getRequest() {
        return new SaRequestForServlet(Mvcs.getReq());
    }

    @Override
    public SaResponse getResponse() {
        return new SaResponseForServlet(Mvcs.getResp());
    }

    @Override
    public SaStorage getStorage() {
        return new SaStorageForServlet(Mvcs.getReq());
    }

    @Override
    public boolean matchPath(String pattern, String path) {
        return false;
    }
}
