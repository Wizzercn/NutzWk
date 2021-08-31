package com.budwk.app.web.commons.proc;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.budwk.app.base.exception.BaseException;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.result.ResultCode;
import com.budwk.app.base.utils.WebUtil;
import org.nutz.castor.FailToCastObjectException;
import org.nutz.dao.DaoException;
import org.nutz.ioc.IocException;
import org.nutz.ioc.ObjectLoadException;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.ViewProcessor;
import org.nutz.mvc.view.ForwardView;

public class WkFailProcessor extends ViewProcessor {

    private static final Log log = Logs.get();
    private String errorUri = "/platform/home/500";
    private String loginUri = "/platform/login";
    private String error403Uri = "/platform/home/403";

    @Override
    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        view = evalView(config, ai, ai.getFailView());
    }

    public void process(ActionContext ac) throws Throwable {
        if (log.isWarnEnabled()) {
            String uri = Mvcs.getRequestPath(ac.getRequest());
            log.warn(String.format("Error@%s :", uri), ac.getError());
        }
        Throwable e = ac.getError();
        // 捕获Ioc异常
        if (e instanceof IocException || e instanceof ObjectLoadException) {
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.IOC_ERROR.getCode(), !log.isDebugEnabled() ? ResultCode.IOC_ERROR.getMsg() : e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(), ResultCode.IOC_ERROR.getMsg()));
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof DaoException) {
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.DAO_ERROR.getCode(), !log.isDebugEnabled() ? ResultCode.DAO_ERROR.getMsg() : e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(), ResultCode.DAO_ERROR.getMsg()));
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof FailToCastObjectException) { // 捕获类型转换异常
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.PARAM_ERROR.getCode(), !log.isDebugEnabled() ? ResultCode.PARAM_ERROR.getMsg() : e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(), ResultCode.PARAM_ERROR.getMsg()));
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof NotLoginException) {    // 如果是未登录异常
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(), e.getMessage()));
                new ForwardView(loginUri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof NotRoleException) {        // 如果是角色异常
            if (WebUtil.isAjax(ac.getRequest())) {
                NotRoleException ee = (NotRoleException) e;
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.USER_NOT_ROLE.getCode(), ResultCode.USER_NOT_ROLE.getMsg() + ": " + ee.getRole()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(),  ResultCode.USER_NOT_ROLE.getMsg()));
                new ForwardView(error403Uri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof NotPermissionException) {    // 如果是权限异常
            if (WebUtil.isAjax(ac.getRequest())) {
                NotPermissionException ee = (NotPermissionException) e;
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.USER_NOT_PERMISSION.getCode(), ResultCode.USER_NOT_PERMISSION.getMsg() + ": " + ee.getCode()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(),  ResultCode.USER_NOT_PERMISSION.getMsg()));
                new ForwardView(error403Uri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof BaseException) {
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.FAILURE.getCode(), !log.isDebugEnabled() ? ResultCode.FAILURE.getMsg() : e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(),  ResultCode.FAILURE.getMsg()));
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(),null);
            }
            return;
        } else if (e instanceof RuntimeException) {
            if (WebUtil.isAjax(ac.getRequest())) {
                WebUtil.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(ResultCode.SERVER_ERROR.getCode(), !log.isDebugEnabled() ? ResultCode.SERVER_ERROR.getMsg() : e.getMessage()));
            } else {
                ac.getRequest().setAttribute("original_request_uri", ac.getRequest().getRequestURI());
                ac.getRequest().setAttribute("error_message", Mvcs.getMessage(ac.getRequest(),  ResultCode.SERVER_ERROR.getMsg()));
                new ForwardView(errorUri).render(ac.getRequest(), ac.getResponse(), null);
            }
            return;
        }
        super.process(ac);
    }
}
