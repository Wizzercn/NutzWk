package cn.wizzer.common.filter;

import cn.wizzer.common.base.Message;
import cn.wizzer.common.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.UTF8JsonView;

/**
 * Created by wizzer on 2016/6/23.
 */
public class PrivateFilter implements ActionFilter {
    public View match(ActionContext context) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser == null) {
            //判断AJAX请求
            if (!"XMLHttpRequest".equalsIgnoreCase(context.getRequest().getHeader("x-requested-with"))) {
                return new ServerRedirectView("/private/login");
            } else {
                return new UTF8JsonView().setData(Message.error("登录失效", context.getRequest()));
            }
        }
        return null;
    }
}
