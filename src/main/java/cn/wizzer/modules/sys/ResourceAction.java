package cn.wizzer.modules.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.wizzer.common.action.BaseAction;
import cn.wizzer.common.config.Globals;
import cn.wizzer.common.filter.GlobalsFilter;
import cn.wizzer.common.filter.UserLoginFilter;
import cn.wizzer.modules.sys.bean.Sys_resource;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private/sys/res")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class ResourceAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("vm:template.private.sys.resource")
	public void user(HttpSession session, HttpServletRequest req) {
	}

	@At
	@Ok("json")
	public List<Sys_resource> list(@Param("subtype") int subtype) {
		Criteria cri = Cnd.cri();
		cri.where().and("subtype", "=", subtype);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		return daoCtl.list(dao, Sys_resource.class, cri);
	}

	/**
	 * 查询全部
	 * */
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id, @Param("subtype") int subtype) {
		return Json.toJson(getJSON(dao, id, subtype));
	}

	private List getJSON(Dao dao, String id, int subtype) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		if (subtype >= 0) {
			cri.where().and("subtype", "=", subtype);
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = daoCtl.list(dao, Sys_resource.class, cri);
        List<Object> array = new ArrayList<Object>();
		for (int i = 0; i < list.size(); i++) {
			Sys_resource res = list.get(i);
			Map<String,Object> jsonobj = new HashMap<String, Object>();
			String pid = res.getId().substring(0, res.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = daoCtl.getRowCount(dao, Sys_resource.class,
					Cnd.where("id", "like", res.getId() + "____"));
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			jsonobj.put("descript", res.getDescript());
			jsonobj.put("url", res.getUrl());
			jsonobj.put("_parentId", pid);
			String bts = Strings.sNull(res.getButton());
			String[] bt;
			String temp = "";
			if (!"".equals(bts)) {
				bt = StringUtils.split(bts,";");
				for (int j = 0; j < bt.length; j++)
					temp += bt[j].substring(0, bt[j].indexOf("/")) + ";";
			}
			jsonobj.put("bts", temp);
			if (num > 0) {
				jsonobj.put("children", getJSON(dao, res.getId(), subtype));
			}
			array.add(jsonobj);
		}
		return array;
	}

	/***
	 * 修改前查找
	 * */
	@At
	@Ok("vm:template.private.sys.resourceUpdate")
	public Sys_resource toupdate(@Param("id") String id, HttpServletRequest req) {
		return daoCtl.detailByName(dao, Sys_resource.class, id);
	}

	/****
	 * 修改
	 * */
	@At
	@Ok("raw")
	public String updateSave(@Param("..") Sys_resource res,
			@Param("button2") String button, HttpServletRequest req) {
		res.setButton(button);
		return daoCtl.update(dao, res) ? res.getId() : "";
	}

	/****
	 * 新建菜单，查找单位。
	 * */
	@At
	@Ok("vm:template.private.sys.resourceAdd")
	public void toAdd() {

	}

	/***
	 * 新建资源
	 * */
	@At
	@Ok("raw")
	public boolean addSave(@Param("..") Sys_resource res,
			@Param("button2") String button) {

		Sql sql = Sqls
				.create("select max(location)+1 from Sys_resource where id like  @id");
		sql.params().set("id", res.getId() + "_%");
		int location = daoCtl.getIntRowValue(dao, sql);
		res.setLocation(location);
		res.setId(daoCtl.getSubMenuId(dao, "Sys_resource", "id", res.getId()));
		res.setButton(button);
		return daoCtl.add(dao, res);
	}

	/**
	 * 删除
	 * */
	@At
	@Ok("raw")
	public boolean del(@Param("id") String ids) {
		String[] id = StringUtils.split(ids, ",");
		try{
			for (int i = 0; i < id.length; i++) {
				daoCtl.exeUpdateBySql(
						dao,
						Sqls.create("delete from Sys_resource where id like '"
								+ Strings.sNull(id[i]) + "%'"));
				daoCtl.exeUpdateBySql(
						dao,
						Sqls.create("delete from Sys_role_resource where resourceid like '"
								+ Strings.sNull(id[i]) + "%'"));
			}
		}catch (Exception e){
            return false;
        }
		return true;
	}

	/**
	 * 转到排序页面
	 * */
	@At
	@Ok("vm:template.private.sys.resourceSort")
	public void toSort(HttpServletRequest req) throws Exception {
        List<Object> array = new ArrayList<Object>();
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = daoCtl.list(dao, Sys_resource.class, cri);
		Map<String,Object> jsonroot = new HashMap<String, Object>();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "资源列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
            Map<String,Object> jsonobj = new HashMap<String, Object>();
			Sys_resource obj = list.get(i);
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", p == "" ? "0" : p);
			String name = obj.getName();
			jsonobj.put("name", name);
			jsonobj.put("childOuter", false);
			if (obj.getId().length() < 12) {
				jsonobj.put("open", true);
			} else {
				jsonobj.put("open", false);
			}
			array.add(jsonobj);
		}
		req.setAttribute("str", Json.toJson(array));
	}

	/***
	 * 确认排序
	 * */
	@At
	@Ok("raw")
	public boolean sort(@Param("checkids") String[] checkids) {
        return daoCtl.updateSortRow(dao, "Sys_resource", checkids,
				"location", 0);

	}
	@At
	@Ok("vm:template.private.sys.resourceButton")
	public void resourceButton(){

	}

}
