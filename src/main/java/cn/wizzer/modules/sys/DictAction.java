package cn.wizzer.modules.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.wizzer.common.config.Globals;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.*;
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

import cn.wizzer.common.action.BaseAction;
import cn.wizzer.common.filter.GlobalsFilter;
import cn.wizzer.common.filter.UserLoginFilter;
import cn.wizzer.modules.sys.bean.Sys_dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wizzer
 * @time 2014-03-26 12:30:22
 */
@IocBean
@At("/private/sys/dict")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class DictAction extends BaseAction {
    @Inject
    protected Dao dao;
    @At("")
    @Ok("vm:template.private.sys.dict")
    public void index(HttpSession session, HttpServletRequest req) {

    }

    @At
    @Ok("vm:template.private.sys.dictAdd")
    public void toadd(@Param("id") String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("dict", daoCtl.detailByName(dao, Sys_dict.class, id));
        }
    }

    @At
    @Ok("raw")
    public boolean add(@Param("..") Sys_dict sys_dict, @Param("treeid") String treeid) {
        Sql sql = Sqls
                .create("select max(location)+1 from Sys_dict where id like  @id");
        sql.params().set("id", sys_dict.getId() + "_%");
        int location = daoCtl.getIntRowValue(dao, sql);
        sys_dict.setLocation(location);
        sys_dict.setId(daoCtl.getSubMenuId(dao, "sys_dict", "id", Strings.sNull(treeid)));
        if (daoCtl.add(dao, sys_dict)) {
            Globals.InitDataDict(dao);
            return true;
        } else
            return false;
    }


    @At
    @Ok("vm:template.private.sys.dictUpdate")
    public Sys_dict toupdate(@Param("id") String id, HttpServletRequest req) {
        if (!Strings.isBlank(id) && id.length() > 4) {
            req.setAttribute("dict", daoCtl.detailByName(dao, Sys_dict.class, id.substring(0, 4)));
        }
        return daoCtl.detailByName(dao, Sys_dict.class, id);//html:obj
    }

    @At
    public boolean update(@Param("..") Sys_dict sys_dict) {
        if (daoCtl.update(dao, sys_dict)) {
            Globals.InitDataDict(dao);

            return true;
        } else
            return false;
    }

    @At
    public boolean delete(@Param("id") String id) {
        if (daoCtl.deleteByName(dao, Sys_dict.class, id)) {
            Globals.InitDataDict(dao);

            return true;
        } else
            return false;
    }

    @At
    public boolean deleteIds(@Param("ids") String ids) {
        String[] id = StringUtils.split(ids, ",");
        try {
            for (String d : id) {
                daoCtl.delete(dao, "sys_dict", Cnd.where("id", "like", d + "%"));
            }
            Globals.InitDataDict(dao);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @At
    public boolean getdict(@Param("id") String id, @Param("tag") int tag) {
        return true;
    }

    @At
    @Ok("raw")
    public String list(@Param("page") int curPage, @Param("rows") int pageSize, @Param("treeid") String treeid) {
        Criteria cri = Cnd.cri();
        cri.where().and("1", "=", 1);
        cri.where().and("id", "like", Strings.sNull(treeid) + "____");

        cri.getOrderBy().asc("location");
        return daoCtl.listPageJson(dao, Sys_dict.class, curPage, pageSize, cri);
    }

    @At
    @Ok("vm:template.private.sys.dictSort")
    public void toSort(@Param("id") String id, HttpServletRequest req) throws Exception {
        List<Object> array = new ArrayList<Object>();
        Criteria cri = Cnd.cri();
        cri.where().and("id", "like", id + "%");
        cri.getOrderBy().asc("location");
        List<Sys_dict> list = daoCtl.list(dao, Sys_dict.class, cri);
        Map<String, Object> jsonroot = new HashMap<String, Object>();
        jsonroot.put("id", "");
        jsonroot.put("pId", "0");
        jsonroot.put("name", "数据字典");
        jsonroot.put("open", true);
        jsonroot.put("childOuter", false);
        jsonroot.put("icon", Globals.APP_BASE_NAME
                + "/images/icons/icon042a1.gif");
        array.add(jsonroot);
        for (Sys_dict obj : list) {
            Map<String, Object> jsonobj = new HashMap<String, Object>();
            jsonobj.put("id", obj.getId());
            String p = obj.getId().substring(0, obj.getId().length() - 4);
            jsonobj.put("pId", "".equals(p) ? "0" : p);
            String name = obj.getDval();
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

    /**
     * 确认排序
     */
    @At
    @Ok("raw")
    public boolean sort(@Param("id") String id, @Param("checkids") String[] ids, HttpSession session, HttpServletRequest req) {
        boolean res = false;
        try {
            for (int i = 0; i < ids.length; i++) {
                Sql sql = Sqls.create("update Sys_dict set location =" + (i) + " where id like '" + id + "%' and id=" + ids[i]);
                dao.execute(sql);
            }
            res = true;
        } catch (Exception e) {
            res = false;
        }
        return res;

    }

    /**
     * 获取栏目树形结构
     *
     * @param id
     * @return
     */
    @At
    @Ok("raw")
    public String treelist(@Param("id") String id) {
        Condition cnd;

        List<Object> array = new ArrayList<Object>();
        if (Strings.isBlank(id)) {
            Map<String, Object> jsonroot = new HashMap<String, Object>();
            jsonroot.put("id", "");
            jsonroot.put("pId", "0");
            jsonroot.put("name", "数据字典");
            jsonroot.put("url", "javascript:list(\"\")");
            jsonroot.put("target", "_self");
            jsonroot.put("icon", Globals.APP_BASE_NAME
                    + "/images/icons/icon042a1.gif");
            array.add(jsonroot);
            cnd = Cnd.where("length(id)", "=", 4).asc("location");
        } else {
            cnd = Cnd.where("id", "like", id + "____").asc("location");
        }
        List<Sys_dict> list = daoCtl.list(dao, Sys_dict.class, cnd);
        for (int i = 0; i < list.size(); i++) {
            Sys_dict ch = list.get(i);
            boolean sign = false;
            String pid = ch.getId().substring(0, ch.getId().length() - 4);
            if (i == 0 || "".equals(pid))
                pid = "0";
            int num = daoCtl.getRowCount(dao, Sys_dict.class, Cnd.where("id", "like", ch.getId() + "____"));
            if (num > 0)
                sign = true;
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("id", ch.getId());
            obj.put("pId", pid);
            obj.put("isParent", sign);
            obj.put("name", ch.getDval());
            obj.put("url", "javascript:list(\"" + ch.getId() + "\")");
            obj.put("target", "_self");
            array.add(obj);
        }
        return Json.toJson(array);
    }
}