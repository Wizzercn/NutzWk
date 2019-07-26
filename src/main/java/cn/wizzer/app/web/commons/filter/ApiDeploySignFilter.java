package cn.wizzer.app.web.commons.filter;

import cn.wizzer.app.web.commons.utils.SignDeployUtil;
import cn.wizzer.framework.base.Result;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * 应用管理服务端接口签名
 * Created by wizzer on 2019/3/8.
 */
public class ApiDeploySignFilter implements ActionFilter {
    private static final Log log = Logs.get();

    public View match(ActionContext context) {
        try {
            SignDeployUtil signCheckUtil = context.getIoc().get(SignDeployUtil.class);
            Map<String, Object> paramMap = getParameterMap(context.getRequest());
            log.debug("paramMap:::\r\n" + Json.toJson(paramMap));
            Result result = signCheckUtil.checkSign(paramMap);
            if (result == null) {
                return new UTF8JsonView(JsonFormat.compact()).setData(Result.error("签名出错"));
            }
            if (result.getCode() == 0) {
                return null;
            }
            return new UTF8JsonView(JsonFormat.compact()).setData(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(-1, "系统异常"));
        }
    }

    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String paramName = names.nextElement();
            map.put(paramName, request.getParameter(paramName));
        }
        return map;
    }
}
