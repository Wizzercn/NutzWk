package cn.wizzer.common.mvc.processor;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.lang.Mirror;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.processor.AbstractProcessor;

public class XSSProcessor extends AbstractProcessor {

	/**
	 * 修改req中的参数 http://blog.csdn.net/xieyuooo/article/details/8447301
	 */
	@Override
	public void process(ActionContext ac) throws Throwable {
		/*HttpServletRequest req = ac.getRequest();
		Mirror<?> mirror = Mirror.me(req.getClass());
		Object request = mirror.getValue(req, "request");
		Mirror<?> mirror1 = Mirror.me(request.getClass());
		Object innerRequest = mirror1.getValue(request, "request");

		System.out.println(innerRequest);
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
		Mirror<?> mirror2 = Mirror.me(parametersField.getClass());
		Field[] fields = mirror2.getFields();
		for (Field $field : fields) {
			System.out.println($field.getName());
		}
		// 获取hashtable来完成对参数变量的修改
		Field hashTabArrField = parameterObject.getClass().getDeclaredField("paramHashStringArray");
		hashTabArrField.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<String, String[]> map = (Map<String, String[]>) hashTabArrField.get(parameterObject);
		map.put("fuck", new String[] { "fuck you" });*/
		doNext(ac);
	}
}
