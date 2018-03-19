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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void index(String siteid, HttpServletRequest req) {
        List<Cms_site> siteList = cmsSiteService.query();
        if (Strings.isBlank(siteid) && siteList.size() > 0) {
            siteid = siteList.get(0).getId();
        }
        Cnd cnd = Cnd.NEW();
        cnd.and("siteid", "=", siteid);
        cnd.and(Cnd.exps("parentId", "=", "").or("parentId", "is", null));
        cnd.asc("location").asc("path");
        req.setAttribute("list", cmsChannelService.query(cnd));
        req.setAttribute("siteList", siteList);
        req.setAttribute("siteid", siteid);
    }

    @At("/add/?")
    @Ok("beetl:/platform/cms/channel/add.html")
    @RequiresPermissions("cms.content.channel")
    public Object add(String siteid, @Param("pid") String pid, HttpServletRequest req) {
        req.setAttribute("siteid", siteid);
        return Strings.isBlank(pid) ? null : cmsChannelService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel.add")
    @SLog(tag = "新建栏目", msg = "栏目名称:${args[0].name}")
    public Object addDo(@Param("..") Cms_channel channel, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            cmsChannelService.save(channel, parentId);
            cmsChannelService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/cms/channel/edit.html")
    @RequiresPermissions("cms.content.channel")
    public Object edit(String id, HttpServletRequest req) {
        Cms_channel channel = cmsChannelService.fetch(id);
        if (channel != null) {
            req.setAttribute("parentMenu", cmsChannelService.fetch(channel.getParentId()));
            req.setAttribute("siteid", channel.getSiteid());
        }
        return channel;
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel")
    public Object tree(@Param("pid") String pid) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Cms_channel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("children", channel.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At("/child/?")
    @Ok("beetl:/platform/cms/channel/child.html")
    @RequiresPermissions("cms.content.channel")
    public Object child(String id) {
        return cmsChannelService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
    }

    @At("/sort/?")
    @Ok("beetl:/platform/cms/channel/sort.html")
    @RequiresPermissions("cms.content.channel")
    public void sort(String siteid, HttpServletRequest req) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("siteid", "=", siteid).asc("location").asc("path"));
        List<Cms_channel> firstMenus = new ArrayList<>();
        Map<String, List<Cms_channel>> secondMenus = new HashMap<>();
        for (Cms_channel menu : list) {
            if (menu.getPath().length() > 4) {
                List<Cms_channel> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        req.setAttribute("firstMenus", firstMenus);
        req.setAttribute("secondMenus", secondMenus);
        req.setAttribute("siteid", siteid);
        cmsChannelService.clearCache();
    }

    @At("/sortDo/?")
    @Ok("json")
    @RequiresPermissions("cms.content.channel.sort")
    public Object sortDo(String siteid, @Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            cmsChannelService.dao().execute(Sqls.create("update cms_channel set location=0 where siteid=@siteid").setParam("siteid", siteid));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    cmsChannelService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            cmsChannelService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
