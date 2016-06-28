package cn.wizzer.modules.controllers.sys;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.models.sys.Sys_role;
import cn.wizzer.modules.models.sys.Sys_unit;
import cn.wizzer.modules.services.sys.MenuService;
import cn.wizzer.modules.services.sys.RoleService;
import cn.wizzer.modules.services.sys.UnitService;
import cn.wizzer.modules.services.sys.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/sys/role")
@Filters({@By(type = PrivateFilter.class)})
public class RoleController {
    private static final Log log = Logs.get();
    @Inject
    UserService userService;
    @Inject
    MenuService menuService;
    @Inject
    UnitService unitService;
    @Inject
    RoleService roleService;

    @At("")
    @Ok("beetl:/private/sys/role/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/sys/role/add.html")
    @RequiresAuthentication
    public Object add(@Param("unitid") String unitid, HttpServletRequest req) {
        List<Sys_menu> list = menuService.query(Cnd.orderBy().asc("location").asc("path"));
        List<Sys_menu> datas = roleService.getDatas();
        List<NutMap> menus = new ArrayList<>();
        for (Sys_menu menu : list) {
            NutMap map=new NutMap();
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            map.put("id",menu.getId());
            map.put("text",menu.getName());
            map.put("icon",Strings.sBlank(menu.getIcon()));
            map.put("parent",menu.getParentId().equals("")?"#":menu.getParentId());
            map.put("data",menu.getHref());
            menus.add(map);
        }
        req.setAttribute("menus", Json.toJson(menus));
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.role.add")
    public Object addDo(@Param("menuIds") String menuIds, @Param("..") Sys_role role, HttpServletRequest req) {
        try {
            int num = roleService.count(Cnd.where("code", "=", role.getCode().trim()));
            if (num > 0) {
                return Result.error("sys.role.code", req);
            }
            String[] ids = StringUtils.split(menuIds, ",");
            Sys_role r=roleService.insert(role);
            for (String s : ids) {
                if (!Strings.isEmpty(s)) {
                    roleService.insert("sys_role_menu", org.nutz.dao.Chain.make("roleId", r.getId()).add("menuId", s));
                }
            }
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("unitid") String unitid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        return roleService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("id", "root");
        obj.put("text", "系统角色");
        obj.put("children", false);
        tree.add(obj);
        for (Sys_unit unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

}
