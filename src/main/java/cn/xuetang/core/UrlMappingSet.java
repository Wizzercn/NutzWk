package cn.xuetang.core;

import java.io.File;
import java.lang.reflect.Method;

import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.mvc.ActionChainMaker;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.UrlMappingImpl;

/**
 * 用于生成URL-CLASS映射关系，方便维护  /WEB-INF/paths.txt
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-12-19 下午10:36:17
 * 
 * @version
 */
public class UrlMappingSet extends UrlMappingImpl {
	private static int count = 0;

	public void add(ActionChainMaker maker, ActionInfo ai, NutConfig config) {
		super.add(maker, ai, config);
		printActionMappingThis(ai);

	}

	protected void printActionMappingThis(ActionInfo ai) {

		String[] paths = ai.getPaths();
		StringBuilder sb = new StringBuilder();
		if (null != paths && paths.length > 0) {
			sb.append(paths[0]);
			for (int i = 1; i < paths.length; i++)
				sb.append(",").append(paths[i]);
		} else {
			throw Lang.impossible();
		}
		sb.append("\n");
		// 打印方法名
		Method method = ai.getMethod();
		String str;
		if (null != method)
			str = String.format("%-30s : %-10s", Lang.simpleMetodDesc(method),
					method.getReturnType().getSimpleName());
		else
			throw Lang.impossible();

		sb.append("\t" + ai.getModuleType().getName());
		sb.append("\n\r");
		sb.append("\t" + str);
		sb.append("\n\r");
		sb.append("\tOK:" + ai.getOkView());
		sb.append("\n\n");
		String filepath = Mvcs.getServletContext().getRealPath("/WEB-INF/")
				+ "/paths.txt";
		File f = new File(filepath);
		if (count == 0) {
			Files.write(f, sb.toString());
		} else {

			Files.appendWrite(f, sb.toString());
		}
		count++;
	}
}
