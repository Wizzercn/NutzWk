package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.common.util.StringUtil;
import cn.wizzer.modules.back.cms.services.CmsArticleService;
import cn.wizzer.modules.back.cms.services.CmsChannelService;
import cn.wizzer.modules.back.sys.models.Sys_menu;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_menu;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxMenuService;
import cn.wizzer.modules.back.wx.services.WxReplyService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.bean.WxMenu;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

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
    @Inject
    WxReplyService wxReplyService;
    @Inject
    CmsChannelService cmsChannelService;
    @Inject
    CmsArticleService cmsArticleService;

    @At({"", "/index/?"})
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
        req.setAttribute("menus", wxMenuService.query(Cnd.where("wxid", "=", wxid).and("parentId", "=", "").asc("location")));
        req.setAttribute("config", wxConfigService.fetch(wxid));
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object checkDo(@Param("wxid") String wxid, @Param("parentId") String parentId, HttpServletRequest req) {
        int count = wxMenuService.count(Cnd.where("wxid", "=", Strings.sBlank(wxid)).and("parentId", "=", Strings.sBlank(parentId)));
        if (Strings.isBlank(parentId) && count > 2) {
            return Result.error("只可设置三个一级菜单");
        }
        if (!Strings.isBlank(parentId) && count > 4) {
            return Result.error("只可设置五个二级菜单");
        }
        return Result.success("");
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.add")
    @SLog(tag = "添加菜单", msg = "菜单名称:${args[0].menuName}")
    public Object addDo(@Param("..") Wx_menu menu, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            if (Strings.isBlank(menu.getWxid())) {
                return Result.error("请选择公众号");
            }
            wxMenuService.save(menu, parentId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.sort")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            wxMenuService.dao().execute(Sqls.create("update wx_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    wxMenuService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/menu/edit.html")
    @RequiresAuthentication
    public Object edit(String id, HttpServletRequest req) {
        Wx_menu menu = wxMenuService.fetch(id);
        req.setAttribute("config", wxConfigService.fetch(menu.getWxid()));
        return wxMenuService.fetchLinks(menu,"wxConfig");
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.edit")
    @SLog(tag = "修改菜单", msg = "菜单名称:${args[0].menuName}")
    public Object editDo(@Param("..") Wx_menu menu, HttpServletRequest req) {
        try {
            wxMenuService.updateIgnoreNull(menu);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.delete")
    @SLog(tag = "删除菜单", msg = "菜单名称:${args[1].getAttribute('menuName')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Wx_menu menu = wxMenuService.fetch(id);
            req.setAttribute("menuName", menu.getMenuName());
            wxMenuService.deleteAndChild(menu);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/pushMenu/?")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.push")
    @SLog(tag = "推送菜单", msg = "公众号名称:${args[1].getAttribute('name')}")
    public Object pushMenu(String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            List<Wx_menu> list = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location"));
            req.setAttribute("name", config.getAppname());
            List<Wx_menu> firstMenus = new ArrayList<>();
            Map<String, List<Wx_menu>> secondMenus = new HashMap<>();
            for (Wx_menu menu : list) {
                if (menu.getPath().length() > 4) {
                    List<Wx_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                    if (s == null) s = new ArrayList<>();
                    s.add(menu);
                    secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
                } else if (menu.getPath().length() == 4) {
                    firstMenus.add(menu);
                }
            }
            List<WxMenu> m1 = new ArrayList<>();
            for (Wx_menu firstMenu : firstMenus) {
                WxMenu xm1 = new WxMenu();
                if (firstMenu.isHasChildren()) {
                    List<WxMenu> m2 = new ArrayList<>();
                    xm1.setName(firstMenu.getMenuName());
                    if (secondMenus.get(firstMenu.getPath()).size() > 0) {
                        for (Wx_menu secondMenu : secondMenus.get(firstMenu.getPath())) {
                            WxMenu xm2 = new WxMenu();
                            if ("view".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setUrl(secondMenu.getUrl());
                                xm2.setName(secondMenu.getMenuName());
                            } else if ("click".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setKey(secondMenu.getMenuKey());
                                xm2.setName(secondMenu.getMenuName());
                            } else {
                                xm2.setName(secondMenu.getMenuName());
                                xm2.setType("click");
                                xm2.setKey(secondMenu.getMenuName());
                            }
                            m2.add(xm2);
                        }
                        xm1.setSubButtons(m2);
                    }
                    m1.add(xm1);
                } else {
                    WxMenu xm2 = new WxMenu();
                    if ("view".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setUrl(firstMenu.getUrl());
                        xm2.setName(firstMenu.getMenuName());
                    } else if ("click".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setKey(firstMenu.getMenuKey());
                        xm2.setName(firstMenu.getMenuName());
                    } else {
                        xm2.setName(firstMenu.getMenuName());
                        xm2.setType("click");
                        xm2.setKey(firstMenu.getMenuName());
                    }
                    m1.add(xm2);
                }
            }
            WxResp wxResp = wxApi2.menu_create(m1);
            if(wxResp.errcode()!=0){
                return Result.error(wxResp.errmsg());
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @At("/keyword/?")
    @Ok("beetl:/private/wx/menu/keyword.html")
    @RequiresAuthentication
    public void keyword(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
    }

    @At("/keywordData")
    @Ok("json:full")
    @RequiresAuthentication
    public Object keywordData(@Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
            cnd.and("type", "=", "keyword");
        }
        return wxReplyService.data(length, start, draw, order, columns, cnd, null);
    }


    @At("/cms/?")
    @Ok("beetl:/private/wx/menu/cms.html")
    @RequiresAuthentication
    public void cms(String type, HttpServletRequest req) {
        req.setAttribute("type", type);
    }

    @At("/cmsData/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object cmsData(String type, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if ("channel".equals(type)) {
            return cmsChannelService.data(length, start, draw, order, columns, cnd, null);
        } else {
            return cmsArticleService.data(length, start, draw, order, columns, cnd, null);
        }
    }
}
