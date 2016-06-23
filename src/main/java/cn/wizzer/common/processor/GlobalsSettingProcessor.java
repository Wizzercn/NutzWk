package cn.wizzer.common.processor;

import cn.wizzer.common.shiro.view.Permission;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import cn.wizzer.common.base.Globals;

/**
 * Created by wizzer on 2016/6/22.
 */
public class GlobalsSettingProcessor extends AbstractProcessor {

	@SuppressWarnings("rawtypes")
	public void process(ActionContext ac) throws Throwable {
		ac.getRequest().setAttribute("AppRoot", Globals.AppRoot);
		ac.getRequest().setAttribute("AppBase", Globals.AppBase);
		ac.getRequest().setAttribute("AppName", Globals.AppName);
		ac.getRequest().setAttribute("AppDomain", Globals.AppDomain);
		ac.getRequest().setAttribute("AppShrotName", Globals.AppShrotName);
		ac.getRequest().setAttribute("shiro", Mvcs.ctx().getDefaultIoc().get(Permission.class));
		// 如果Cookies中有语言属性则设置
		String lang=ac.getRequest().getParameter("lang");
		if (!Strings.isEmpty(lang)) {
			Mvcs.setLocalizationKey(lang);
		}else{
			// Mvcs.getLocalizationKey()  1.r.56 版本是null,所以要做两次判断, 1.r.57已修复为默认值 Nutz:Fix issue 1072
			lang=Strings.isBlank(Mvcs.getLocalizationKey())?Mvcs.getDefaultLocalizationKey():Mvcs.getLocalizationKey();
		}
		ac.getRequest().setAttribute("lang", lang);
		doNext(ac);
	}

}
