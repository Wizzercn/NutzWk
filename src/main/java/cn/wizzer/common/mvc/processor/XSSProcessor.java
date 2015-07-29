package cn.wizzer.common.mvc.processor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.processor.AbstractProcessor;

public class XSSProcessor extends AbstractProcessor {

	@Override
	public void process(ActionContext ac) throws Throwable {
		HttpServletRequest req = ac.getRequest();
		Map<?,?> requestParams=req.getParameterMap(); 
		Method method=requestParams.getClass().getMethod("setLocked",new Class[]{boolean.class}); 
		method.invoke(requestParams,new Object[]{new Boolean(false)}); 
		Enumeration<?> reqs = req.getParameterNames();
		while (reqs.hasMoreElements()) {
			String strKey = (String) reqs.nextElement();
			String value = req.getParameter(strKey);
			req.setAttribute(strKey, StringEscapeUtils.escapeHtml4(value));
		}
		method.invoke(requestParams,new Object[]{new Boolean(true)}); 
		doNext(ac);
	}

}
