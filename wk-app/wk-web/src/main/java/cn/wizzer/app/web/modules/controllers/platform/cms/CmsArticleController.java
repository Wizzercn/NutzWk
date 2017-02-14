package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_article;
import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

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
    private CmsArticleService cmsArticleService;
    @Inject
    private CmsChannelService cmsChannelService;

    @At("")
    @Ok("beetl:/platform/cms/article/index.html")
    @RequiresAuthentication
    public void index() {
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object tree(@Param("pid") String pid) {
        List<Cms_channel> list = cmsChannelService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
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

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("channelId") String channelId, @Param("title") String title, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(channelId) && !"0".equals(channelId)) {
            cnd.and("channelId", "like", "%" + channelId + "%");
        }
        if (!Strings.isBlank(title)) {
            cnd.and("title", "like", "%" + title + "%");
        }
        return cmsArticleService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/platform/cms/article/add.html")
    @RequiresAuthentication
    public void add(@Param("channelId") String channelId, HttpServletRequest req) {
        req.setAttribute("channel", channelId != null && !"0".equals(channelId) ? cmsChannelService.fetch(channelId) : null);
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        req.setAttribute("username", user == null ? "" : user.getUsername());
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.article.add")
    @SLog(tag = "添加文章", msg = "文章标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(@Param("..") Cms_article article, @Param("at") String at, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int publishAt = (int) (sdf.parse(at).getTime() / 1000);
            article.setPublishAt(publishAt);
            cmsArticleService.insert(article);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/cms/article/edit.html")
    @RequiresAuthentication
    public Object edit(String id, HttpServletRequest req) {
        Cms_article article = cmsArticleService.fetch(id);
        req.setAttribute("channel", article != null ? cmsChannelService.fetch(article.getChannelId()) : null);
        return article;
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.content.article.edit")
    @SLog(tag = "添加文章", msg = "文章标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_article article, @Param("at") String at, HttpServletRequest req) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int publishAt = (int) (sdf.parse(at).getTime() / 1000);
            article.setPublishAt(publishAt);
            cmsArticleService.updateIgnoreNull(article);
            return Result.success("system.success");
        } catch (Exception e) {
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
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
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
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
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
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
