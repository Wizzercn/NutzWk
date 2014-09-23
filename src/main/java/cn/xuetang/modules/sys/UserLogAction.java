package cn.xuetang.modules.sys;

import cn.xuetang.common.action.BaseAction;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.sys.bean.Sys_user_log;

/** 
 * 类描述： 
 * 创建人：Wizzer 
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-12-2 下午6:00:07 
 * @version 
 */
@IocBean
@At("/private/sys/user")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class UserLogAction extends BaseAction {
	@Inject
	protected Dao dao;
	@At
	@Ok("vm:template.private.sys.userLog")
	public void log() {
		
	}
	@At
	@Ok("raw")
	public String loglist(@Param("page") int curPage,@Param("rows") int pageSize,
			@Param("SearchUserName") String SearchUserName) {
		Criteria cri = Cnd.cri();

		if (!"".equals(SearchUserName)) {
			SqlExpressionGroup exp = Cnd.exps("LOGINNAME", "LIKE", "%"+SearchUserName+"%").or("REALNAME", "LIKE", "%"+SearchUserName+"%");
			cri.where().and(exp);
		} 
		cri.getOrderBy().desc("LOGINTIME");
		return daoCtl.listPageJson(dao, Sys_user_log.class, curPage,
				pageSize, cri);
	}
}
