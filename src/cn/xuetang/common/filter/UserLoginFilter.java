package cn.xuetang.common.filter;

import java.util.Hashtable;

import cn.xuetang.common.config.Globals;
import cn.xuetang.modules.sys.bean.Sys_user;
import org.apache.commons.lang.StringUtils;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;


/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class UserLoginFilter implements ActionFilter {
    private final Log log= Logs.get();

	@Override
	public View match(ActionContext context) {
		Sys_user user = (Sys_user) context.getRequest().getSession()
				.getAttribute("userSession");
		if (user == null) {
			ServerRedirectView view = new ServerRedirectView(
					"/include/error/nologin.jsp");
			return view;

		}
		
		Hashtable<String, String> btnmap= user.getBtnmap();
		if(btnmap!=null){
			String initBtn="";
			String bts=btnmap.get(Strings.sNull(context.getRequest().getRequestURI().replace(Globals.APP_BASE_NAME, "")));
			if(bts!=null&&bts.indexOf(",")>0){
				String[] tb= StringUtils.split(bts,",");
				for(int i=0;i<tb.length;i++){
					initBtn+="$Z(\""+tb[i]+"\").enable();\r\n"; 
				}
				
			} 
			context.getRequest().setAttribute("initBtn", initBtn);
		}
		context.getRequest().setAttribute("curuser", user);
		return null;
	}

}
