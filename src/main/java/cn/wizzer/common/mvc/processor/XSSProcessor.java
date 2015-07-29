package cn.wizzer.common.mvc.processor;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.processor.AbstractProcessor;

public class XSSProcessor extends AbstractProcessor {


	/**
	 * 修改req中的参数
	 * http://blog.csdn.net/xieyuooo/article/details/8447301
	 */
	@Override
	public void process(ActionContext ac) throws Throwable {
		HttpServletRequest req = ac.getRequest();
		try {
			Class<?> clazz = req.getClass();
			Field requestField = clazz.getDeclaredField("request");
			requestField.setAccessible(true);
			Object innerRequest = requestField.get(req);// 获取到request对象

			// 设置尚未初始化 (否则在获取一些参数的时候，可能会导致不一致)
			Field field = innerRequest.getClass().getDeclaredField("parametersParsed");
			field.setAccessible(true);
			field.setBoolean(innerRequest, false);
			Field coyoteRequestField = innerRequest.getClass().getDeclaredField("coyoteRequest");
			coyoteRequestField.setAccessible(true);
			Object coyoteRequestObject = coyoteRequestField.get(innerRequest);// 获取到coyoteRequest对象

			Field parametersField = coyoteRequestObject.getClass().getDeclaredField("parameters");
			parametersField.setAccessible(true);
			Object parameterObject = parametersField.get(coyoteRequestObject);// 获取到parameter的对象
			// 获取hashtable来完成对参数变量的修改
			Field hashTabArrField = parameterObject.getClass().getDeclaredField("paramHashStringArray");
			hashTabArrField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<String, String[]> map = (Map<String, String[]>) hashTabArrField.get(parameterObject);
			Enumeration<?> reqs = req.getParameterNames();
			while (reqs.hasMoreElements()) {
				String strKey = (String) reqs.nextElement();
				String value = req.getParameter(strKey);
				map.put(strKey, new String[]{ StringEscapeUtils.escapeHtml4(value) });
			}
			// 也可以通过下面的方法，不过下面的方法只能添加参数，如果有相同的key，会追加参数，即，同一个key的结果集会有多个
			// Method method =  parameterObject.getClass().getDeclaredMethod("addParameterValues" , String.class , String[].class);
			// method.invoke(parameterObject , "fuck" , new String[] {"fuck you!" , "sssss"});
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		doNext(ac);
	}
}
