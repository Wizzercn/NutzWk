package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_article;
import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.models.Cms_site;
import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.cms.modules.services.CmsSiteService;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/article")
public class CmsArticleController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private CmsArticleService cmsArticleService;
    @Inject
    @Reference
    private CmsChannelService cmsChannelService;
    @Inject
    @Reference
    private CmsSiteService cmsSiteService;

    @At(value = {"", "/?"})
    @Ok("beetl:/platform/cms/article/index.html")
    @RequiresPermissions("cms.content.article")
    public void index(String siteid, @Param("channelId") String channelId, HttpServletRequest req) {
        List<Cms_site> siteList = cmsSiteService.query();
        if (Strings.isBlank(siteid) && siteList.size() > 0) {
            siteid = siteList.get(0).getId();
        }
        req.setAttribute("siteList", siteList);
        req.setAttribute("siteid", Strings.sNull(siteid));
        req.setAttribute("channelId", Strings.sNull(channelId));
    }

    @At("/tree/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article")
    public Object tree(String siteid, @Param("pid") String pid) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).and("siteid", "=", siteid).asc("location").asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        if (Strings.isBlank(pid)) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", "0");
            obj.put("text", "所有栏目");
            obj.put("children", false);
            tree.add(obj);
        }
        for (Cms_channel channel : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", channel.getId());
            obj.put("text", channel.getName());
            obj.put("children", channel.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At("/data/?")
    @Ok("json:full")
    @RequiresPermissions("cms.content.article")
    public Object data(String siteid, @Param("channelId") String channelId, @Param("title") String title, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        cnd.and("siteid", "=", siteid);
        if (!Strings.isBlank(channelId) && !"0".equals(channelId)) {
            cnd.and("channelId", "like", "%" + channelId + "%");
        }
        if (!Strings.isBlank(title)) {
            cnd.and("title", "like", "%" + title + "%");
        }
        return cmsArticleService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/add/?")
    @Ok("beetl:/platform/cms/article/add.html")
    @RequiresPermissions("cms.content.article")
    public void add(String siteid, @Param("channelId") String channelId, HttpServletRequest req) {
        req.setAttribute("channel", channelId != null && !"0".equals(channelId) ? cmsChannelService.fetch(channelId) : null);
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        req.setAttribute("username", user == null ? "" : user.getUsername());
        req.setAttribute("siteid", siteid);
        req.setAttribute("channelId", channelId);
    }

    @At("/addDo/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article.add")
    @SLog(tag = "添加文章", msg = "文章标题:${args[1].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(String siteid, @Param("..") Cms_article article, @Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            article.setPublishAt(Times.parse(sdf, beginDate).getTime() / 1000);
            article.setEndAt(Times.parse(sdf, endDate).getTime() / 1000);
            article.setSiteid(siteid);
            article.setStatus(0);
            cmsArticleService.insert(article);
            cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/cms/article/edit.html")
    @RequiresPermissions("cms.content.article")
    public Object edit(String id, HttpServletRequest req) {
        Cms_article article = cmsArticleService.fetch(id);
        req.setAttribute("channel", article != null ? cmsChannelService.fetch(article.getChannelId()) : null);
        req.setAttribute("siteid", article != null ? article.getSiteid() : "");
        return article;
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.article.edit")
    @SLog(tag = "修改文章", msg = "文章标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_article article, @Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            article.setPublishAt(Times.parse(sdf, beginDate).getTime() / 1000);
            article.setEndAt(Times.parse(sdf, endDate).getTime() / 1000);
            article.setStatus(0);
            cmsArticleService.updateIgnoreNull(article);
            cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }


    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article.edit")
    @SLog(tag = "发布文章", msg = "文章标题:${args[1].getAttribute('title')}")
    public Object enable(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", cmsArticleService.fetch(id).getTitle());
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", false).add("status", 0), Cnd.where("id", "=", id));
            cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article.edit")
    @SLog(tag = "取消发布", msg = "文章标题:${args[1].getAttribute('title')}")
    public Object disable(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", cmsArticleService.fetch(id).getTitle());
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", true).add("status", 0), Cnd.where("id", "=", id));
            cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("cms.content.article.delete")
    @SLog(tag = "删除文章", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String oneId, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                cmsArticleService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                cmsArticleService.delete(oneId);
                req.setAttribute("id", oneId);
            }
            cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
