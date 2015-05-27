package cn.wizzer.common.file;

import org.nutz.filepool.NutFilePool;
import org.nutz.mvc.Mvcs;

/** 
 * 类描述： 设置文件池路径及大小
 * 创建人：Wizzer 
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-12-16 上午9:54:21 
 * @version 
 */
public class FilePool extends NutFilePool {

	public FilePool(String homePath, long size) {
		super(webinfPath(homePath), size);
	}

	private static final String webinfPath(String str) {
		return Mvcs.getServletContext().getRealPath("/WEB-INF")+str;
	}

}
