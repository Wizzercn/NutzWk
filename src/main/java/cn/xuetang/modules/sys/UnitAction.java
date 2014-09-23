package cn.xuetang.modules.sys;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Globals;
import cn.xuetang.common.filter.GlobalsFilter;
import cn.xuetang.common.filter.UserLoginFilter;
import cn.xuetang.modules.sys.bean.Sys_role_resource;
import cn.xuetang.modules.sys.bean.Sys_user;

import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
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

import cn.xuetang.modules.sys.bean.Sys_unit;

/**
 * @author Wizzer.cn
 * @time 2012-9-14 上午11:45:52
 */
@IocBean
@At("/private/sys/unit")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class UnitAction extends BaseAction {
    @Inject
    protected Dao dao;

    @At("")
    @Ok("vm:template.private.sys.unit")
    public void unit(HttpServletRequest req, HttpSession session) {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        String[] mp = StringUtils.split(user.getBtnmap().get("/private/sys/unit"), ";");
        req.setAttribute("btnmap", mp);

        List<Sys_role_resource> reslist = daoCtl.list(dao, Sys_role_resource.class, Cnd.wrap("resourceid = '000100010001'"));
        HashSet<String> set = new HashSet<String>();
        for (Sys_role_resource resource : reslist) {
            if (user.getRolelist().contains(resource.getRoleid())) {
                String button = resource.getButton();
                if (!"".equals(button) && button != null) {
                    String[] buttons = StringUtils.split(button, ",");
                    for (int i = 0; i < buttons.length; i++) {
                        set.add(buttons[i]);
                    }
                }
            }
        }
        req.setAttribute("buttonset", set);
    }

    @At
    @Ok("raw")
    public String tree(@Param("id") String id, HttpSession session)
            throws Exception {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        id = Strings.sNull(id);
        List<Object> array = new ArrayList<Object>();
        if ("".equals(id)) {
            Map<String, Object> jsonroot = new HashMap<String, Object>();
            jsonroot.put("id", "");
            jsonroot.put("pId", "0");
            jsonroot.put("name", "机构列表");
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
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("id", u.getId());
            obj.put("pId", pid);
            obj.put("name", u.getName());
            obj.put("url", "javascript:view(\"" + u.getId() + "\")");
            obj.put("isParent", num > 0);
            obj.put("target", "_self");
            array.add(obj);
            i++;
        }
        return Json.toJson(array);
    }

    @At
    @Ok("json")
    public Sys_unit view(@Param("id") String id) {
        return daoCtl.detailByName(dao, Sys_unit.class, id);
    }

    @At
    @Ok("vm:template.private.sys.unitAdd")
    public void add(@Param("id") String id, HttpServletRequest req) {
        Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, id);
        req.setAttribute("unit", unit);
    }

    @At
    @Ok("raw")
    public String addSave(@Param("..") Sys_unit unit) {
        String id = daoCtl.getSubMenuId(dao, "sys_unit", "id",
                Strings.sNull(unit.getId()));
        unit.setId(id);
        int location = daoCtl.getIntRowValue(dao, Sqls.create("select max(location) from sys_unit"));
        unit.setLocation(location);
        if (!daoCtl.add(dao, unit))
            return "";
        return id;
    }

    @At
    @Ok("raw")
    public boolean del(@Param("id") String id) {
        boolean res;
        Sql sql = Sqls.create("delete from sys_unit where id like @id");
        sql.params().set("id", id + "%");
        res = daoCtl.exeUpdateBySql(dao, sql);
        if (res) {
            daoCtl.exeUpdateBySql(dao, Sqls.create("delete from sys_role_resource where roleid in(" +
                    "select id from sys_role where unitid like '" + id + "%')"));
            daoCtl.exeUpdateBySql(dao, Sqls.create("delete from sys_user_role where userid in(" +
                    "select uid from sys_user where unitid like '" + id + "%')"));
            daoCtl.exeUpdateBySql(dao, Sqls.create("delete from sys_user where unitid like '" + id + "%'"));
            daoCtl.exeUpdateBySql(dao, Sqls.create("delete from sys_role where unitid like '" + id + "%'"));
        }

        return res;
    }

    @At
    @Ok("vm:template.private.sys.unitUpdate")
    public void update(@Param("id") String id, HttpServletRequest req) {
        Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, id);
        req.setAttribute("obj", unit);
    }

    @At
    @Ok("raw")
    public String updateSave(@Param("..") Sys_unit unit) {
        return daoCtl.update(dao, unit) == true ? unit.getId() : "";

    }

    @At
    @Ok("vm:template.private.sys.unitSort")
    public void sort(@Param("id") String id, HttpServletRequest req, HttpSession session) throws Exception {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        Condition c;
        if (user.getSysrole())  //判断是否为系统管理员角色
        {
            c = Cnd.where("id", "is not", null).asc("location,id");
        } else {
            c = Cnd.where("id", "like", user.getUnitid() + "%'").asc("location,id");
        }
        List<Sys_unit> list = daoCtl.list(dao, Sys_unit.class, c);
        List<Object> array = new ArrayList<Object>();
        Map<String, Object> jsonroot = new HashMap<String, Object>();
        jsonroot.put("id", "");
        jsonroot.put("pId", "0");
        jsonroot.put("name", "机构列表");
        jsonroot.put("open", true);
        jsonroot.put("childOuter", false);
        jsonroot.put("icon", Globals.APP_BASE_NAME
                + "/images/icons/icon042a1.gif");
        array.add(jsonroot);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> jsonobj = new HashMap<String, Object>();
            Sys_unit obj = list.get(i);
            jsonobj.put("id", obj.getId());
            String p = obj.getId().substring(0, obj.getId().length() - 4);
            jsonobj.put("pId", p == "" ? "0" : p);
            jsonobj.put("name", obj.getName());
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

    @At
    @Ok("raw")
    public boolean sortSave(@Param("checkids") String checkids, HttpSession session) {

        Sys_user user = (Sys_user) session.getAttribute("userSession");
        String[] ids = StringUtils.split(checkids, ",");
        int initvalue = 0;
        if (!user.getSysrole())  //判断是否为系统管理员角色
        {
            initvalue = daoCtl.getIntRowValue(dao, Sqls.create("select min(location) from sys_unit where id in " + StringUtils.split(checkids, ",")));
        }
        return daoCtl.updateSortRow(dao, "sys_unit", ids, "location", initvalue);

    }
}
