package cn.wizzer.app.web.commons.filter;

import cn.wizzer.app.web.commons.utils.SignUtil;
import cn.wizzer.framework.base.Result;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
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
 * Sign签名验证拦截器,如果不使用JWT可以直接用这个拦截器
 * Created by wizzer on 2016/8/11.
 */
public class ApiSignFilter implements ActionFilter {
    private static final Log log = Logs.get();

    public View match(ActionContext context) {
        try {
            PropertiesProxy conf = context.getIoc().get(PropertiesProxy.class, "conf");
            RedisService redisService = context.getIoc().get(RedisService.class);
            String appid_sys = conf.get("apitoken.appid", "");
            String appkey_sys = conf.get("apitoken.appkey", "");
            Map<String, Object> paramMap = getParameterMap(context.getRequest());
            String appid = Strings.sNull(paramMap.get("appid"));
            String sign = Strings.sNull(paramMap.get("sign"));
            String timestamp = Strings.sNull(paramMap.get("timestamp"));
            String nonce = Strings.sNull(paramMap.get("nonce"));
            if (!appid_sys.equals(appid)) {
                return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(1, "appid不正确"));
            }
            if (Times.getTS() - Long.valueOf(timestamp) > 60 * 1000) {//时间戳相差大于1分钟则为无效的
                return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(2, "timestamp不正确"));
            }
            String nonceCache = redisService.get("api_sign_nonce:" + appid + "_" + nonce);
            if (Strings.isNotBlank(nonceCache)) {//如果一分钟内nonce是重复的则为无效,让nonce只能使用一次
                return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(3, "nonce不正确"));

            }
            if (!SignUtil.createSign(appkey_sys, paramMap).equalsIgnoreCase(sign)) {
                return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(4, "sign签名不正确"));
            }
            //nonce保存到缓存
            redisService.set("api_sign_nonce:" + appid + "_" + nonce, nonce);
            redisService.expire("api_sign_nonce:" + appid + "_" + nonce, 60);
            return null;
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
