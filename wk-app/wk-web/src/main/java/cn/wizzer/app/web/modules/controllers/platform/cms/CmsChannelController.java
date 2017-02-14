package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
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
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/channel")
public class CmsChannelController {
    private static final Log log = Logs.get();
    @Inject
    private CmsChannelService cmsChannelService;

    @At("")
    @Ok("beetl:/platform/cms/channel/index.html")
    @RequiresAuthentication
    public void index(HttpServletRequest req) {
        req.setAttribute("list", cmsChannelService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("location").asc("path")));

    }

    @At
    @Ok("beetl:/platform/cms/channel/add.html")
    @RequiresAuthentication
    public Object add(@Param("pid") String pid, HttpServletRequest req) {
        return Strings.isBlank(pid) ? null : cmsChannelService.fetch(pid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel.add")
    @SLog(tag = "新建栏目", msg = "栏目名称:${args[0].name}")
    public Object addDo(@Param("..") Cms_channel channel, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            cmsChannelService.save(channel, parentId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/cms/channel/edit.html")
    @RequiresAuthentication
    public Object edit(String id, HttpServletRequest req) {
        Cms_channel channel = cmsChannelService.fetch(id);
        if (channel != null) {
            req.setAttribute("parentMenu", cmsChannelService.fetch(channel.getParentId()));
        }
        return channel;
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel.edit")
    @SLog(tag = "编辑栏目", msg = "栏目名称:${args[0].name}")
    public Object editDo(@Param("..") Cms_channel channel, HttpServletRequest req) {
        try {
            channel.setOpBy(StringUtil.getUid());
            channel.setOpAt((int) (System.currentTimeMillis() / 1000));
            cmsChannelService.updateIgnoreNull(channel);
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
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
    @RequiresAuthentication
    public Object child(String id) {
        return cmsChannelService.query(Cnd.where("parentId", "=", id).asc("location").asc("path"));
    }

    @At
    @Ok("beetl:/platform/cms/channel/sort.html")
    @RequiresAuthentication
    public void sort(HttpServletRequest req) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.orderBy().asc("location").asc("path"));
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
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.channel.sort")
    public Object sortDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            String[] menuIds = StringUtils.split(ids, ",");
            int i = 0;
            cmsChannelService.dao().execute(Sqls.create("update cms_channel set location=0"));
            for (String s : menuIds) {
                if (!Strings.isBlank(s)) {
                    cmsChannelService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", s));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
