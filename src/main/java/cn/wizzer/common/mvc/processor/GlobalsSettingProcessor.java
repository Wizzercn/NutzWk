package cn.wizzer.common.mvc.processor;

import java.util.Map;

import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import cn.wizzer.common.mvc.config.Globals;
import cn.wizzer.common.util.CacheUtils;
import cn.wizzer.common.util.CookieUtils;

/**
 * 全局变量设置 Created by Wizzer.cn on 2015/7/2.
 */
public class GlobalsSettingProcessor extends AbstractProcessor {

	@SuppressWarnings("rawtypes")
	public void process(ActionContext ac) throws Throwable {
		String language = Mvcs.getLocalizationKey();
		ac.getRequest().setAttribute("app_name", Globals.APP_NAME);
		ac.getRequest().setAttribute("app_base_name", Globals.APP_BASE_NAME);
		ac.getRequest().setAttribute("app_base_path", Globals.APP_BASE_PATH);
		ac.getRequest().setAttribute("app_copyright", ((Map) CacheUtils.get(Globals.SYS_CONFIG_KEY)).get("app_copyright"));
		// 如果Cookies中有语言属性则设置
		if (!Strings.isEmpty(CookieUtils.getCookie(ac.getRequest(), "language"))) {
			language = CookieUtils.getCookie(ac.getRequest(), "language");
			Mvcs.setLocalizationKey(language);
		}
		ac.getRequest().setAttribute("app_language", language);
		doNext(ac);
	}

}
