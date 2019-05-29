package cn.wizzer.app.web.commons.processor;

import cn.wizzer.framework.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.impl.processor.AbstractProcessor;
import org.nutz.mvc.view.ForwardView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * SQL XSS拦截
 * Created by wizzer on 2016/7/1.
 */
public class XssSqlFilterProcessor extends AbstractProcessor {

    private static final Log log = Logs.get();
    protected String lerrorUri = "/error/403.html";
    //要排除的请求路径
    private String[] ignoreUrls = new String[]{
            "/platform/sys/app/conf/addDo",
            "/platform/sys/app/conf/editDo"
    };

    public void process(ActionContext ac) throws Throwable {
        if (checkUrl(ac) && checkParams(ac)) {
            if (NutShiro.isAjax(ac.getRequest())) {
                ac.getResponse().addHeader("loginStatus", "paramsDenied");
                NutShiro.rendAjaxResp(ac.getRequest(), ac.getResponse(), Result.error(Mvcs.getMessage(ac.getRequest(), "system.paramserror")));
            } else {
                new ForwardView(lerrorUri).render(ac.getRequest(), ac.getResponse(), Mvcs.getMessage(ac.getRequest(), "system.paramserror"));
            }
            return;
        }
        doNext(ac);
    }

    protected boolean checkUrl(ActionContext ac) {
        for (String url : ignoreUrls) {
            if (ac.getRequest().getRequestURI().startsWith(url)) {
                return false;
            }
        }
        return true;
    }

    protected boolean checkParams(ActionContext ac) {
        HttpServletRequest req = ac.getRequest();
        Iterator<String[]> values = req.getParameterMap().values().iterator();// 获取所有的表单参数
        Iterator<String[]> values2 = req.getParameterMap().values().iterator();// 因为是游标所以要重新获取
        boolean isError = false;
        String regEx_sql = "select|update|and|or|delete|insert|trancate|char|chr|into|substr|ascii|declare|exec|count|master|drop|execute";
        String regEx_xss = "script|iframe";
        //SQL过滤
        while (values.hasNext()) {
            String[] valueArray = (String[]) values.next();
            for (int i = 0; i < valueArray.length; i++) {
                String value = valueArray[i].toLowerCase();
                //分拆关键字
                String[] inj_stra = StringUtils.split(regEx_sql, "|");
                for (int j = 0; j < inj_stra.length; j++) {
                    // 判断如果路径参数值中含有关键字则返回true,并且结束循环
                    if ("and".equals(inj_stra[j]) || "or".equals(inj_stra[j]) || "into".equals(inj_stra[j])) {
                        if (value.contains(" " + inj_stra[j] + " ")) {
                            isError = true;
                            log.debugf("[%-4s]URI=%s %s", req.getMethod(), req.getRequestURI(), "SQL关键字过滤:" + value);
                            break;
                        }
                    } else {
                        if (value.contains(" " + inj_stra[j] + " ")
                                || value.contains(
                                inj_stra[j] + " ")) {
                            isError = true;
                            log.debugf("[%-4s]URI=%s %s", req.getMethod(), req.getRequestURI(), "SQL关键字过滤:" + value);
                            break;
                        }
                    }
                }
                if (isError) {
                    break;
                }
            }
            if (isError) {
                break;
            }
        }
        if (!isError) {
            // XSS漏洞过滤
            while (values2.hasNext()) {
                String[] valueArray = (String[]) values2.next();
                for (int i = 0; i < valueArray.length; i++) {
                    String value = valueArray[i].toLowerCase();
                    // 分拆关键字
                    String[] inj_stra = StringUtils.split(regEx_xss, "|");
                    for (int j = 0; j < inj_stra.length; j++) {
                        // 判断如果路径参数值中含有关键字则返回true,并且结束循环
                        if (value.contains("<" + inj_stra[j] + ">")
                                || value.contains("<" + inj_stra[j])
                                || value.contains(inj_stra[j] + ">")) {
                            log.debugf("[%-4s]URI=%s %s", req.getMethod(), req.getRequestURI(), "XSS关键字过滤:" + value);
                            isError = true;
                            break;
                        }
                    }
                    if (isError) {
                        break;
                    }
                }
                if (isError) {
                    break;
                }
            }
        }
        return isError;
    }
}
