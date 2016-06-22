package cn.wizzer.common.processor;

import java.util.Map;

import cn.wizzer.common.util.CookieUtil;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.util.CacheUtil;

/**
 * Created by wizzer on 2016/6/22.
 */
public class GlobalsSettingProcessor extends AbstractProcessor {

	@SuppressWarnings("rawtypes")
	public void process(ActionContext ac) throws Throwable {
		ac.getRequest().setAttribute("AppRoot", Globals.AppRoot);
		ac.getRequest().setAttribute("AppBase", Globals.AppBase);
		ac.getRequest().setAttribute("AppDomain", Globals.AppDomain);
		ac.getRequest().setAttribute("AppShrotName", Globals.AppShrotName);
		// 如果Cookies中有语言属性则设置
		String lang=ac.getRequest().getParameter("lang");
		if (!Strings.isEmpty(lang)) {
			Mvcs.setLocalizationKey(lang);
		}else lang=Mvcs.getDefaultLocalizationKey();
		ac.getRequest().setAttribute("lang", lang);
		doNext(ac);
	}

}
