package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.models.Cms_site;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.cms.modules.services.CmsSiteService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/channel")
public class CmsChannelController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private CmsChannelService cmsChannelService;
    @Inject
    @Reference
    private CmsSiteService cmsSiteService;

    @At(value = {"", "/?"})
    @Ok("beetl:/platform/cms/channel/index.html")
    @RequiresPermissions("cms.content.channel")
    public void index(String siteId, HttpServletRequest req) {
        Cms_site site = null;
        List<Cms_site> siteList = cmsSiteService.query();
        if (Strings.isBlank(siteId) && siteList.size() > 0) {
            site = siteList.get(0);
        }
        if (Strings.isNotBlank(siteId)) {
            site = cmsSiteService.fetch(siteId);
        }
        req.setAttribute("siteList", siteList);
        req.setAttribute("site", site);
    }

    @At("/child/?")
    @Ok("json")
    @RequiresAuthentication
    public Object child(String siteId, @Param("pid") String pid, HttpServletRequest req) {
        List<Cms_channel> list = new ArrayList<>();
        List<NutMap> treeList = new ArrayList<>();
        Cnd cnd = Cnd.NEW();
        if (Strings.isBlank(pid)) {
            cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
        } else {
            cnd.and("parentId", "=", pid);
        }
        cnd.and("siteid", "=", siteId);
        cnd.asc("location").asc("path");
        list = cmsChannelService.query(cnd);
        for (Cms_channel channel : list) {
            if (cmsChannelService.count(Cnd.where("parentId", "=", channel.getId())) > 0) {
                channel.setHasChildren(true);
            }
            NutMap map = Lang.obj2nutmap(channel);
            map.addv("expanded", false);
            map.addv("children", new ArrayList<>());
            treeList.add(map);
        }
        return Result.success().addData(treeList);
    }

    @At("/tree/?")
    @Ok("json")
    @RequiresAuthentication
    public Object tree(String siteId, @Param("pid") String pid, HttpServletRequest req) {
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
            cnd.and("siteid", "=", siteId);
            cnd.asc("location").asc("path");
            List<Cms_channel> list = cmsChannelService.query(cnd);
            for (Cms_channel menu : list) {
                NutMap map = NutMap.NEW().addv("value", menu.getId()).addv("label", menu.getName());
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
    @RequiresPermissions("cms.content.channel.add")
    @SLog(tag = "新建栏目", msg = "栏目名称:${args[0].name}")
    public Object addDo(@Param("..") Cms_channel channel, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            channel.setOpBy(StringUtil.getPlatformUid());
            cmsChannelService.save(channel, parentId);
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel")
    public Object edit(String id, HttpServletRequest req) {
        try {
            Cms_channel channel = cmsChannelService.fetch(id);
            NutMap map = Lang.obj2nutmap(channel);
            map.put("parentName", "无");
            map.put("siteName", "无");
            if (Strings.isNotBlank(channel.getParentId())) {
                map.put("parentName", cmsChannelService.fetch(channel.getParentId()).getName());
            }
            if (Strings.isNotBlank(channel.getSiteid())) {
                map.put("siteName", cmsSiteService.fetch(channel.getSiteid()).getSite_name());
            }
            return Result.success().addData(map);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel.edit")
    @SLog(tag = "编辑栏目", msg = "栏目名称:${args[0].name}")
    public Object editDo(@Param("..") Cms_channel channel, HttpServletRequest req) {
        try {
            channel.setOpBy(StringUtil.getPlatformUid());
            channel.setOpAt(Times.getTS());
            cmsChannelService.updateIgnoreNull(channel);
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel.delete")
    @SLog(tag = "删除栏目", msg = "栏目名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Cms_channel channel = cmsChannelService.fetch(id);
            req.setAttribute("name", channel.getName());
            cmsChannelService.deleteAndChild(channel);
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel.edit")
    @SLog(tag = "启用栏目", msg = "栏目名称:${args[1].getAttribute('name')}")
    public Object enable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", cmsChannelService.fetch(menuId).getName());
            cmsChannelService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", menuId));
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel.edit")
    @SLog(tag = "禁用栏目", msg = "栏目名称:${args[1].getAttribute('name')}")
    public Object disable(String menuId, HttpServletRequest req) {
        try {
            req.setAttribute("name", cmsChannelService.fetch(menuId).getName());
            cmsChannelService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", menuId));
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/sort/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel")
    public Object sort(String siteid, HttpServletRequest req) {
        try {
            List<Cms_channel> list = cmsChannelService.query(Cnd.where("siteid", "=", siteid).asc("location").asc("path"));
            NutMap menuMap = NutMap.NEW();
            for (Cms_channel unit : list) {
                List<Cms_channel> list1 = menuMap.getList(unit.getParentId(), Cms_channel.class);
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
        List<Cms_channel> subList = menuMap.getList(pid, Cms_channel.class);
        for (Cms_channel menu : subList) {
            NutMap map = Lang.obj2nutmap(menu);
            map.put("label", menu.getName());
            if (menu.isHasChildren() || (menuMap.get(menu.getId()) != null)) {
                map.put("children", getTree(menuMap, menu.getId()));
            }
            treeList.add(map);
        }
        return treeList;
    }

    @At("/sortDo/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel.sort")
    public Object sortDo(String siteid, @Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            cmsChannelService.execute(Sqls.create("update cms_channel set location=0 where siteid=@siteid").setParam("siteid", siteid));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    cmsChannelService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            cmsChannelService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

}
