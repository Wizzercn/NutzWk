package cn.wizzer.common.filter;

import cn.wizzer.common.base.Result;
import cn.wizzer.modules.services.sys.SysApiService;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;


/**
 * Created by wizzer on 2016/8/11.
 */
public class TokenFilter implements ActionFilter {
    private static final Log log = Logs.get();
    private SysApiService apiService= Mvcs.ctx().getDefaultIoc().get(SysApiService.class);

    public View match(ActionContext context) {
        String appId = Strings.sNull(context.getRequest().getParameter("appId"));
        String token = Strings.sNull(context.getRequest().getParameter("token"));
        if (!apiService.verifyToken(appId, token)) {
            return new UTF8JsonView(JsonFormat.compact()).setData(Result.error(-1,"token invalid"));
        }
        return null;
    }
}
