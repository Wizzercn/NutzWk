package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.common.util.StringUtil;
import cn.wizzer.modules.back.sys.models.Sys_menu;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_menu;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxMenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/private/wx/conf/menu")
@Filters({@By(type = PrivateFilter.class)})
public class WxMenuController {
    private static final Log log = Logs.get();
    @Inject
    WxMenuService wxMenuService;
    @Inject
    WxConfigService wxConfigService;

    @At({"", "/?"})
    @Ok("beetl:/private/wx/menu/index.html")
    @RequiresAuthentication
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        List<Wx_menu> menus = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location").asc("path"));
        List<Wx_menu> firstMenus = new ArrayList<>();
        Map<String, List<Wx_menu>> secondMenus = new HashMap<>();
        for (Wx_menu menu : menus) {
            if (menu.getPath().length() > 4) {
                List<Wx_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));
    }

    @At("/add/?")
    @Ok("beetl:/private/wx/menu/add.html")
    @RequiresAuthentication
    public void add(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        req.setAttribute("menus", wxMenuService.fetch(Cnd.where("wxid", "=", wxid).and("parentId", "=", "").asc("location")));
        req.setAttribute("config", wxConfigService.fetch(wxid));
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object checkDo(@Param("wxid") String wxid, @Param("parentId") String parentId, HttpServletRequest req) {
        int count = wxMenuService.count(Cnd.where("wxid", "=", Strings.sBlank(wxid)).and("parentId", "=", Strings.sBlank(parentId)));
        if (Strings.isBlank(parentId) && count > 2) {
            return Result.error("只可设置三个一级菜单", req);
        }
        if (!Strings.isBlank(parentId) && count > 4) {
            return Result.error("只可设置五个二级菜单", req);
        }
        return Result.success("", req);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.add")
    @SLog(tag = "添加菜单", msg = "菜单名称:${args[0].menuName}")
    public Object addDo(@Param("..") Wx_menu menu, HttpServletRequest req) {
        try {
            if(Strings.isBlank(menu.getWxid())){
                return Result.error("",req);
            }
            wxMenuService.insert(menu);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/menu/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return wxMenuService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.edit")
    @SLog(tag = "修改菜单", msg = "菜单名称:${args[0].menuName}")
    public Object editDo(@Param("..") Wx_config conf, HttpServletRequest req) {
        try {
            wxMenuService.updateIgnoreNull(conf);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.delete")
    @SLog(tag = "删除菜单", msg = "菜单名称:${args[1].getAttribute('menuName')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("menuName", wxMenuService.fetch(id).getMenuName());
            wxMenuService.delete(id);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

}
