package cn.wizzer.modules.sys;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.wizzer.common.action.BaseAction;
import cn.wizzer.common.filter.GlobalsFilter;
import cn.wizzer.common.filter.UserLoginFilter;
import cn.wizzer.modules.sys.bean.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import cn.wizzer.common.config.Globals;
import cn.wizzer.common.util.SortHashtable;
import cn.wizzer.common.util.StringUtil;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */

@IocBean
@At("/private/sys/role")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class RoleAction extends BaseAction {
	@Inject
	protected Dao dao;
	@At
	@Ok("raw")
	public String list(@Param("roleid") int roleid,
			@Param("page") int curPage, @Param("rows") int pageSize,
			HttpSession session) {
		Criteria cri = Cnd.cri();
		cri.where().andInBySql("id",
				"select userid from sys_user_role where roleid=%s", roleid);
		cri.getOrderBy().asc("loginname");
		return daoCtl.listPageJson(dao, Sys_user.class, curPage, pageSize, cri);
	}

	@At
	@Ok("raw")
	public String userlist(@Param("unitid") String unitid,
			@Param("page") int curPage,@Param("rows") int pageSize,
			@Param("SearchUserName") String SearchUserName,@Param("lock") String lock,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Criteria cri = Cnd.cri();
		 
		if(!"".equals(unitid)){
			cri.where().and("unitid","like",unitid+"%");
		}else{
			if (user.getSysrole()){ // 判断是否为系统管理员角色
				cri.where().and("1","=",1);
			}else{
				cri.where().and("unitid","like",user.getUnitid()+"%");
			}
		}
		if("1".equals(lock)){
			cri.where().and("state","=",1);
		}
		if (!"".equals(SearchUserName)) {
			SqlExpressionGroup exp = Cnd.exps("loginname", "LIKE", "%"+SearchUserName+"%").or("realname", "LIKE", "%"+SearchUserName+"%");
			cri.where().and(exp);
		} 
		cri.getOrderBy().asc("loginname");
		return daoCtl.listPageJson(dao, Sys_user.class, curPage,
				pageSize, cri);
	}


	/**
	 * 增加角色页面
	 */
	@At
	@Ok("vm:template.private.sys.roleAdd")
	public void toadd( HttpServletRequest req) {

    }

	/**
	 * 增加角色处理
	 */
	@At
	public int add(@Param("..") Sys_role role,
			@Param("checkids") String checkids, HttpServletRequest req) {

		String ids = Strings.sNull(checkids).replace(",", "");
		role.setUnitid(ids);

		return daoCtl.addT(dao, role).getId();
	}

	/**
	 * 修改角色页面
	 */
	@At
	@Ok("vm:template.private.sys.roleUpdate")
	public void toupdate(@Param("id") int id, HttpServletRequest req) {
		Sys_role role = daoCtl.detailById(dao, Sys_role.class, id);
		req.setAttribute("obj", role);

    }

	/**
	 * 修改角色处理
	 */
	@At
	public boolean update(@Param("..") Sys_role role) {
		return daoCtl.update(dao, role);
	}

	/**
	 * 删除角色
	 */
	@At
	public boolean delete(@Param("id") int id) {
		boolean res;
		res=daoCtl.deleteById(dao, Sys_role.class, id);
		if(res){
            daoCtl.exeUpdateBySql(dao,Sqls.create("delete from weixin_channel_role where roleid="+id));
            daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_role_resource where roleid="+id));
			daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_user_role where roleid="+id));
		}
		
		return res; 
	}

	@At
	@Ok("raw")
	public String ajaxunit(HttpSession session, @Param("id") String id,
			HttpServletResponse res) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (user == null) {
			return null;
		}

		String pId = Strings.sNull(id);
		List array = new ArrayList();
		if ("".equals(pId)) {
			pId = "";
		}

		List<Sys_unit> unitlist = null;
		Condition cnd;
		int sysrole = 0;
		if (user.getRolelist().contains(2)) // 判断是否为系统管理员角色
		{
			sysrole = 1;
			cnd = Cnd.where("id", "like", pId + "____").asc("location")
					.asc("id");
		} else {
			if ("".equals(pId)) {
				cnd = Cnd.where("id", "=", user.getUnitid()).asc("location")
						.asc("id");
			} else {
				cnd = Cnd.where("id", "like", pId + "____").asc("location")
						.asc("id");
			}
		}

		unitlist = daoCtl.list(dao, Sys_unit.class, cnd);
		if ("".equals(pId)) {
			Map<String,Object> jsonroot = new HashMap<String, Object>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "机构列表");
			jsonroot.put("icon", Globals.APP_BASE_NAME
					+ "/images/icons/icon042a1.gif");
			jsonroot.put("nocheck", true);
			array.add(jsonroot);
			if (sysrole == 1) {
				Map<String,Object> jsonroot1 = new HashMap<String, Object>();
				jsonroot1.put("id", "");
				jsonroot1.put("pId", "0");
				jsonroot1.put("name", "不属于任何机构");
				jsonroot1.put("checked", true);
				array.add(jsonroot1);
			}
		}
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			String pid = unitid.substring(0, unitid.length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			String unitname = unitobj.getName();
			boolean sign = false;
			Condition sqlm = Cnd.wrap("id like '" + unitobj.getId() + "____'");
			int num = daoCtl.getRowCount(dao, Sys_unit.class, sqlm);
			if (num > 0)
				sign = true;
			Map<String,Object> obj = new HashMap<String, Object>();
			obj.put("id", unitid);
			obj.put("pId", pid);
			obj.put("name", unitname);
			obj.put("isParent", sign);
			if (sysrole == 0 && user.getUnitid().equals(unitid)) {
				obj.put("checked", true);
			}
			array.add(obj);
		}
		return Json.toJson(array);
	}

	/**
	 * 增加角色用户页面
	 */
	@At
	@Ok("vm:template.private.sys.roleUser")
	public void toaddroleuser(@Param("roleid") String roleid,
			HttpServletRequest req, HttpSession session) {

		Sys_user user = (Sys_user) session.getAttribute("userSession");
		roleid = Strings.sNull(roleid);
		if (user.getRolelist().contains(2 )) // 判断是否为系统管理员角色
		{
			req.setAttribute("uid", "");
		} else {
			req.setAttribute("uid", user.getUnitid());
		}
		req.setAttribute("roleid", roleid);
	}

	@At
	@Ok("raw")
	public String ajaxroleuser(HttpSession session, @Param("id") String id,
			@Param("roleid") int roleid, HttpServletResponse res) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		id = Strings.sNull(id);
		List array = new ArrayList();
		if ("".equals(id)) {
			Map<String,Object> jsonroot = new HashMap<String, Object>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "机构列表");
			jsonroot.put("url", "javascript:list(\"\")");
			jsonroot.put("target", "_self");
			jsonroot.put("icon", Globals.APP_BASE_NAME
					+ "/images/icons/icon042a1.gif");
			array.add(jsonroot);
		}
		Criteria cri = Cnd.cri();
		if (user.getSysrole()) // 判断是否为系统管理员角色
		{
			cri.where().and("id", "like", id + "____");
			cri.getOrderBy().asc("location");
			cri.getOrderBy().asc("id");
		} else {
			if ("".equals(id)) {
				cri.where().and("id", "=", user.getUnitid());
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			} else {
				cri.where().and("id", "like", id + "____");
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			}
		}
		List<Sys_unit> unitlist = daoCtl.list(dao, Sys_unit.class, cri);
		int i = 0;
		for (Sys_unit u : unitlist) {
			String pid = u.getId().substring(0, u.getId().length() - 4);
			int num = daoCtl.getRowCount(dao, Sys_unit.class,
					Cnd.wrap("id like '" + u.getId() + "____'"));
			if (i == 0 || "".equals(pid))
				pid = "0";
			Map<String,Object> obj = new HashMap<String, Object>();
			obj.put("id", u.getId());
			obj.put("pId", pid);
			obj.put("name", u.getName());
			obj.put("url", "javascript:list(\"" + u.getId() + "\")");
			obj.put("target", "_self");
			obj.put("isParent", num > 0 );
			array.add(obj);
			i++;
		}
		return Json.toJson(array);
	}

	@At
	public String ajaxname(@Param("name") String name)
			throws UnsupportedEncodingException {
		String value = "";
		name = Strings.sNull(name);

		Sys_role role = daoCtl.detailByName(dao, Sys_role.class, "name", name);

		if (role != null) {
			value = "true";
		}
		return value;
	}

	/**
	 * 权限分配页面
	 */
	@At
	@Ok("vm:template.private.sys.roleMenu")
	public void tomenu(@Param("roleid") int roleid, HttpSession session,
			HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			Condition sql;
			if (user.getRolelist().contains(2)) // 判断是否为系统管理员角色
			{
				sql = Cnd.wrap("order by location,id asc");
			} else {
				String rids = "";
				for (int i = 0; i < user.getRolelist().size(); i++) {
					if (i == user.getRolelist().size() - 1) {
						rids += user.getRolelist().get(i) + "";
					} else {
						rids += user.getRolelist().get(i) + ",";
					}
				}
				sql = Cnd
						.wrap("id in(select DISTINCT RESOURCEID from SYS_ROLE_RESOURCE where roleid in("
								+ rids + ")) order by location,id asc");
			}
			Hashtable<String, String> rh = new Hashtable<String, String>();
			Sql htsql = Sqls
					.create("select RESOURCEID,BUTTON from SYS_ROLE_RESOURCE where roleid="
							+ roleid);
			rh = daoCtl.getHTable(dao, htsql);
			List<Sys_resource> list = daoCtl.list(dao, Sys_resource.class, sql);
            List array=new ArrayList();
            Map<String,Object> jsonroot=new HashMap<String, Object>();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "资源列表");
			jsonroot.put("checked", false);
			jsonroot.put("nocheck", true);
			jsonroot.put("button", "");
			jsonroot.put("open", true);
			jsonroot.put("icon", Globals.APP_BASE_NAME
					+ "/images/icons/icon042a1.gif");
			array.add(jsonroot);
			for (int i = 0; i < list.size(); i++) {
				Sys_resource obj = list.get(i);
				Map<String,Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", obj.getId());
				jsonobj.put("pId",
						obj.getId().substring(0, obj.getId().length() - 4));
				jsonobj.put("name", obj.getName());
				if (rh != null && null != rh.get(String.valueOf(obj.getId()))) {
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				jsonobj.put("res_button", obj.getButton());
				jsonobj.put("button", rh.get(String.valueOf(obj.getId())));
				array.add(jsonobj);
			}
			req.setAttribute("str", Json.toJson(array));
			req.setAttribute("roleid", roleid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 权限分配处理
	 */
	@At
	public boolean menu(@Param("checkids") String checkids,
			@Param("roleid") int roleid) {
		try {
			if (!"".equals(checkids)) {
				String[] ids =StringUtils.split(checkids, ";") ;
				Condition c = Cnd.where("roleid", "=", roleid);
				daoCtl.delete(dao, "SYS_ROLE_RESOURCE", c);
				for (int i = 0; i < ids.length; i++) {
					Sys_role_resource resource = new Sys_role_resource();
					String[] id_Button = StringUtils.split(ids[i], ":");
					resource.setRoleid(roleid);
					resource.setResourceid(id_Button[0]);
					if (id_Button.length < 2) {
						resource.setButton("");
					} else {
						resource.setButton(id_Button[1]);
					}
					daoCtl.add(dao, resource);
				}
			} else {
				Condition c = Cnd.where("roleid", "=", roleid);
				daoCtl.delete(dao, "SYS_ROLE_RESOURCE", c);
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return false;
	}


	/**
	 * 分配用户角色处理
	 */
	@At
	public String addroleuser(@Param("roleid") int roleid,
			@Param("checkids") String checkids, HttpSession session) {
		checkids = Strings.sNull(checkids);
		String msg = "";
		try {
			int result = 0;
			String[] ids = StringUtils.split(checkids, ",");
			for (int i = 0; i < ids.length; i++) {
				Sys_user_role s = new Sys_user_role();
				s.setUserid(NumberUtils.toLong(Strings.sNull(ids[i])));
				s.setRoleid(roleid);
				daoCtl.add(dao, s);
				result++;
			}
			if (result > 0) {
				msg = "true";
			}
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		} finally {
		}
		return msg;
	}

	/**
	 * 删除角色中的用户
	 */
	@At
	public String delroleuser(@Param("checkids") String checkids,
			@Param("roleid") int roleid) {
		String[] userids = StringUtils.split(checkids, ",");
		for (int i = 0; i < userids.length; i++) {
			Condition cnd = Cnd.where("roleid", "=", roleid).and("userid",
					"=", userids[i]);
			daoCtl.delete(dao, "sys_user_role", cnd);
		}

		return "true";
	}

	@At("")
	@Ok("vm:template.private.sys.role")
	public void role(HttpSession session, @Param("unitid") String unitid1,
			HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Sql sql = Sqls
				.create("select roleid from sys_user_role where userid=@userid");
		sql.params().set("userid", user.getId());
		req.setAttribute("roleid", daoCtl.getIntRowValue(dao, sql));
	}

	@At
	@Ok("raw")
	public String tree(HttpSession session, @Param("id") String unitid1,
			HttpServletRequest req) throws Exception {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		unitid1 = Strings.sNull(unitid1);

		Condition sql;
		Condition sqlunit;
		Condition sqlrole;
		List<Sys_role> list = null;
		List<Sys_role> rolelist = null;
		List<Sys_unit> unitlist = null;
		int sysrole = 0;
		if (user.getRolelist().contains(2)) // 判断是否为系统管理员角色
		{
			sqlunit = Cnd
					.wrap("id in (select unitid from sys_role ) order by location,id asc");
			sqlrole = Cnd
					.wrap("unitid is null or unitid='' order by location,id asc");
			sysrole = 1;
		} else {
			sqlunit = Cnd
					.wrap("id in (select unitid from sys_role where unitid like '"
							+ user.getUnitid() + "%') order by location,id asc");
			sqlrole = Cnd
					.wrap("(unitid is null or unitid='') and id in (select roleid from sys_user_role where userid='"
							+ user.getId()
							+ "') order by location,id asc");
		}
		Sys_unit u = daoCtl.detailByName(dao, Sys_unit.class, unitid1);
		req.setAttribute("unit", u);
		unitlist = daoCtl.list(dao, Sys_unit.class, sqlunit);
		req.setAttribute("sysrole", sysrole);
		Hashtable<String, String> hm = new Hashtable<String, String>();
        List<Object> array=new ArrayList<Object>();
		Map<String,Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "角色列表");
		jsonroot.put("checked", false);
		jsonroot.put("nocheck", true);
		jsonroot.put("open", true);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		if (!"".equals(sqlrole)) {
			rolelist = daoCtl.list(dao, Sys_role.class, sqlrole);
			for (int j = 0; j < rolelist.size(); j++) {

				Sys_role roleobj = rolelist.get(j);
				Map<String,Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", "");
				jsonobj.put("name", roleobj.getName());
				jsonobj.put("checked", false);
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId()
						+ "\")");
				jsonobj.put("target", "_self");
				array.add(jsonobj);

			}

		}
		array.add(jsonroot);
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			sql = Cnd.wrap("unitid='" + unitid + "' order by id");
			list = daoCtl.list(dao, Sys_role.class, sql);
			String tunit = "";
			int t = 1;
			hm.put(unitid, unitid);
			for (int k = unitid.length(); k > 4; k = k - 4) { // 计算单位的上级（递归）
				t = k / 4 - 1;
				if (t != 0) {
					tunit = unitid.substring(0, unitid.length() - 4 * t);
				} else {
					tunit = unitid;
				}
				hm.put(tunit, tunit);
			}
			String name = "";

			for (int j = 0; j < list.size(); j++) { // 得到单位列表下的角色

				Sys_role roleobj = list.get(j);
				name = roleobj.getName();
				Map<String,Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", unitid);
				jsonobj.put("name", name);
				jsonobj.put("checked", false);
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId()
						+ "\")");
				jsonobj.put("target", "_self");

				array.add(jsonobj);

			}
		}
		String temp = "";

		Map.Entry[] set = SortHashtable.getSortedHashtableByKey(hm);
		for (int i = 0; i < set.length; i++) {

			String key = set[i].getKey().toString();
			Sys_unit su = daoCtl.detailByName(dao, Sys_unit.class,
					String.valueOf(key));
			if (su != null) {
				String tempUid = String.valueOf(key);
				String tempName = su.getName();
				if (tempUid.length() == 4) {
					temp = "0";
				} else {
					temp = tempUid.substring(0, tempUid.length() - 4);
				}
				Map<String,Object> jsonobj = new HashMap<String, Object>();
				jsonobj.put("id", tempUid);
				jsonobj.put("pId", temp);
				jsonobj.put("name", StringUtil.substr(tempName, 12));
				jsonobj.put("isParent", true);
				jsonobj.put("open", true);
				jsonobj.put("nocheck", true);
				jsonobj.put("icon", Globals.APP_BASE_NAME
						+ "/images/icons/icon042a1.gif");
				array.add(jsonobj);
			}
		}

		return Json.toJson(array);
	}

	@At
	@Ok("json")
	public Sys_role getrole(@Param("roleid") int roleid) {
		return daoCtl.detailById(dao, Sys_role.class, roleid);
	}

}
