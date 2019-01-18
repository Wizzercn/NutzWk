package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.web.commons.ext.wx.WxService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_menu;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxMenuService;
import cn.wizzer.app.wx.modules.services.WxReplyService;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
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
@At("/platform/wx/conf/menu")
public class WxMenuController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxMenuService wxMenuService;
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    @Reference
    private WxReplyService wxReplyService;
    @Inject
    @Reference
    private CmsChannelService cmsChannelService;
    @Inject
    @Reference
    private CmsArticleService cmsArticleService;
    @Inject
    private WxService wxService;

    @At({"", "/index/?"})
    @Ok("beetl:/platform/wx/menu/index.html")
    @RequiresPermissions("wx.conf.menu")
    public void index(String wxid, HttpServletRequest req) {
        Wx_config wxConfig = null;
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxConfig = list.get(0);
        }
        if (Strings.isNotBlank(wxid)) {
            wxConfig = wxConfigService.fetch(wxid);
        }
        req.setAttribute("wxConfig", wxConfig);
        req.setAttribute("wxList", list);
    }

    @At("/child")
    @Ok("json")
    @RequiresAuthentication
    public Object child(@Param("pid") String pid,@Param("wxid") String wxid, HttpServletRequest req) {
        List<Wx_menu> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.and("wxid","=",wxid);
        cnd.asc("location").asc("path");
        list = wxMenuService.query(cnd);
        for (Wx_menu menu : list) {
            if (wxMenuService.count(Cnd.where("parentId", "=", menu.getId())) > 0) {
                menu.setHasChildren(true);
            }
            NutMap map = Lang.obj2nutmap(menu);
            map.addv("expanded", false);
            map.addv("children", new ArrayList<>());
            treeList.add(map);
        }
        return Result.success().addData(treeList);
    }

    @At("/tree")
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid,@Param("wxid") String wxid, HttpServletRequest req) {
        try {
            List<NutMap> treeList = new ArrayList<>();
            if (Strings.isBlank(pid)) {
                NutMap root = NutMap.NEW().addv("value", "root").addv("label", "不选择菜单");
                treeList.add(root);
            }
            Cnd cnd = Cnd.NEW();
            if (Strings.isBlank(pid)) {
                cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
            } else {
                cnd.and("parentId", "=", pid);
            }
            cnd.and("wxid","=",wxid);
            cnd.asc("location").asc("path");
            List<Wx_menu> list = wxMenuService.query(cnd);
            for (Wx_menu menu : list) {
                NutMap map = NutMap.NEW().addv("value", menu.getId()).addv("label", menu.getMenuName());
                if (menu.isHasChildren()) {
                    map.addv("children", new ArrayList<>());
                }
                treeList.add(map);
            }
            return Result.success().addData(treeList);
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.add")
    @SLog(tag = "添加菜单", msg = "菜单名称:${args[0].menuName}")
    public Object addDo(@Param("..") Wx_menu menu, @Param(value = "parentId",df = "") String parentId, HttpServletRequest req) {
        try {
            if (Strings.isBlank(menu.getWxid())) {
                return Result.error("请选择公众号");
            }
            int count = wxMenuService.count(Cnd.where("wxid", "=", Strings.sBlank(menu.getWxid())).and("parentId", "=", Strings.sBlank(parentId)));
            if (Strings.isBlank(parentId) && count > 2) {
                return Result.error("只可设置三个一级菜单");
            }
            if (!Strings.isBlank(parentId) && count > 4) {
                return Result.error("只可设置五个二级菜单");
            }
            menu.setOpBy(StringUtil.getPlatformUid());
            wxMenuService.save(menu, parentId);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.sort")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            wxMenuService.execute(Sqls.create("update wx_menu set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    wxMenuService.update(Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu")
    public Object edit(String id, HttpServletRequest req) {
        try {
            Wx_menu menu = wxMenuService.fetch(id);
            return Result.success().addData(menu);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.edit")
    @SLog(tag = "修改菜单", msg = "菜单名称:${args[0].menuName}")
    public Object editDo(@Param("..") Wx_menu menu, HttpServletRequest req) {
        try {
            wxMenuService.updateIgnoreNull(menu);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/pushMenu")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu.push")
    @SLog(tag = "推送菜单", msg = "公众号名称:${args[1].getAttribute('name')}")
    public Object pushMenu(@Param("wxid") String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxService.getWxApi2(wxid);
            List<Wx_menu> list = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location"));
            req.setAttribute("name", config.getAppname());
            List<Wx_menu> firstMenus = new ArrayList<>();
            Map<String, List<Wx_menu>> secondMenus = new HashMap<>();
            for (Wx_menu menu : list) {
                if (menu.getPath().length() > 4) {
                    List<Wx_menu> s = secondMenus.get(wxMenuService.getParentPath(menu.getPath()));
                    if (s == null) s = new ArrayList<>();
                    s.add(menu);
                    secondMenus.put(wxMenuService.getParentPath(menu.getPath()), s);
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
                            } else if ("miniprogram".equals(secondMenu.getMenuType())) {
                                xm2.setType(secondMenu.getMenuType());
                                xm2.setName(secondMenu.getMenuName());
                                xm2.setUrl(secondMenu.getUrl());
                                xm2.setAppid(secondMenu.getAppid());
                                xm2.setPagepath(secondMenu.getPagepath());
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
                    } else if ("miniprogram".equals(firstMenu.getMenuType())) {
                        xm2.setType(firstMenu.getMenuType());
                        xm2.setName(firstMenu.getMenuName());
                        xm2.setUrl(firstMenu.getUrl());
                        xm2.setAppid(firstMenu.getAppid());
                        xm2.setPagepath(firstMenu.getPagepath());
                    } else {
                        xm2.setName(firstMenu.getMenuName());
                        xm2.setType("click");
                        xm2.setKey(firstMenu.getMenuName());
                    }
                    m1.add(xm2);
                }
            }
            WxResp wxResp = wxApi2.menu_create(m1);
            if (wxResp.errcode() != 0) {
                return Result.error(wxResp.errmsg());
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error("请检查白名单IP是否配置");
        }
    }

    @At("/keywordData")
    @Ok("json:full")
    @RequiresPermissions("wx.conf.menu")
    public Object data(@Param("wxid") String wxid) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        cnd.and("type", "=", "keyword");
        return Result.success().addData(wxReplyService.query(cnd));
    }

    @At("/sort")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu")
    public Object sort(@Param("wxid") String wxid, HttpServletRequest req) {
        try {
            List<Wx_menu> list = wxMenuService.query(Cnd.where("wxid", "=", wxid).asc("location").asc("path"));
            NutMap menuMap = NutMap.NEW();
            for (Wx_menu unit : list) {
                List<Wx_menu> list1 = menuMap.getList(unit.getParentId(), Wx_menu.class);
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
                list1.add(unit);
                menuMap.put(unit.getParentId(), list1);
            }
            return Result.success().addData(getTree(menuMap, ""));
        } catch (Exception e) {
            return Result.error();
        }
    }

    private List<NutMap> getTree(NutMap menuMap, String pid) {
        List<NutMap> treeList = new ArrayList<>();
        List<Wx_menu> subList = menuMap.getList(pid, Wx_menu.class);
        for (Wx_menu menu : subList) {
            NutMap map = Lang.obj2nutmap(menu);
            map.put("label", menu.getMenuName());
            if (menu.isHasChildren() || (menuMap.get(menu.getId()) != null)) {
                map.put("children", getTree(menuMap, menu.getId()));
            }
            treeList.add(map);
        }
        return treeList;
    }

    @At("/sortDo")
    @Ok("json")
    @RequiresPermissions("wx.conf.menu")
    public Object sortDo(@Param("wxid") String wxid, @Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            wxMenuService.execute(Sqls.create("update wx_menu set location=0 where wxid=@wxid").setParam("wxid", wxid));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    wxMenuService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

}
