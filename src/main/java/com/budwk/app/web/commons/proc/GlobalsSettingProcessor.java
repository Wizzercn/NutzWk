package com.budwk.app.web.commons.proc;

import com.budwk.app.web.commons.utils.ShiroUtil;
import com.budwk.app.base.utils.DateUtil;
import com.budwk.app.base.utils.StringUtil;
import com.budwk.app.web.commons.base.Globals;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.AbstractProcessor;

/**
 * Created by wizzer on 2016/6/22.
 */
public class GlobalsSettingProcessor extends AbstractProcessor {
    private static ShiroUtil shiroUtil;
    private static DateUtil dateUtil;
    private static StringUtil stringUtil;

    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        if (shiroUtil == null) {
            shiroUtil = new ShiroUtil();
        }
        if (dateUtil == null) {
            dateUtil = new DateUtil();
        }
        if (stringUtil == null) {
            stringUtil = new StringUtil();
        }
    }

    @SuppressWarnings("rawtypes")
    public void process(ActionContext ac) throws Throwable {
        ac.getRequest().setAttribute("AppRoot", Globals.AppRoot);
        ac.getRequest().setAttribute("AppBase", Globals.AppBase);
        ac.getRequest().setAttribute("AppName", Globals.AppName);
        ac.getRequest().setAttribute("AppDomain", Globals.AppDomain);
        ac.getRequest().setAttribute("AppShrotName", Globals.AppShrotName);
        ac.getRequest().setAttribute("AppFileDomain", Globals.AppFileDomain);
        ac.getRequest().setAttribute("config", Globals.MyConfig);
        ac.getRequest().setAttribute("shiro", shiroUtil);
        ac.getRequest().setAttribute("date", dateUtil);
        ac.getRequest().setAttribute("string", stringUtil);
        // 如果url中有语言属性则设置
        String lang = ac.getRequest().getParameter("lang");
        if (!Strings.isEmpty(lang)) {
            Mvcs.setLocalizationKey(lang);
        } else {
            // Mvcs.getLocalizationKey()  1.r.56 版本是null,所以要做两次判断, 1.r.57已修复为默认值 Nutz:Fix issue 1072
            lang = Strings.isBlank(Mvcs.getLocalizationKey()) ? Mvcs.getDefaultLocalizationKey() : Mvcs.getLocalizationKey();
        }
        ac.getRequest().setAttribute("lang", lang);
        doNext(ac);
    }

}
