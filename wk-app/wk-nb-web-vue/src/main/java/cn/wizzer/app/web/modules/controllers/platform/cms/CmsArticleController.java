package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_article;
import cn.wizzer.app.cms.modules.models.Cms_channel;
import cn.wizzer.app.cms.modules.models.Cms_site;
import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.app.cms.modules.services.CmsChannelService;
import cn.wizzer.app.cms.modules.services.CmsSiteService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
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
import java.util.List;

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

    @At("/tree/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article")
    public Object tree(String siteid, @Param("pid") String pid) {
        try {
            List<Cms_channel> list = cmsChannelService.query(Cnd.where("siteid", "=", siteid).asc("location").asc("path"));
            NutMap menuMap = NutMap.NEW();
            for (Cms_channel channel : list) {
                List<Cms_channel> list1 = menuMap.getList(channel.getParentId(), Cms_channel.class);
                if (list1 == null) {
                    list1 = new ArrayList<>();
                }
                list1.add(channel);
                menuMap.put(channel.getParentId(), list1);
            }
            return Result.success().addData(getTree(menuMap, ""));
        } catch (Exception e) {
            return Result.error();
        }
    }

    private List<NutMap> getTree(NutMap menuMap, String pid) {
        List<NutMap> treeList = new ArrayList<>();
        if (Strings.isBlank(pid)) {
            NutMap root = NutMap.NEW().addv("value", "root").addv("label", "所有栏目");
            treeList.add(root);
        }
        List<Cms_channel> subList = menuMap.getList(pid, Cms_channel.class);
        for (Cms_channel channel : subList) {
            NutMap map = Lang.obj2nutmap(channel);
            map.put("label", channel.getName());
            if (channel.isHasChildren() || (menuMap.get(channel.getId()) != null)) {
                map.put("children", getTree(menuMap, channel.getId()));
            }
            treeList.add(map);
        }
        return treeList;
    }

    @At("/data/?")
    @Ok("json:full")
    @RequiresPermissions("cms.content.article")
    public Object data(String siteid, @Param("channelId") String channelId,
                       @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            cnd.and("siteid", "=", siteid);
            if (!Strings.isBlank(channelId) && !"root".equals(channelId)) {
                cnd.and("channelId", "=", channelId);
            }
            if (!Strings.isBlank(searchKeyword)) {
                cnd.and("title", "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(cmsArticleService.listPageLinks(pageNumber, pageSize, cnd, "unit"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/addDo/?")
    @Ok("json")
    @RequiresPermissions("cms.content.article.add")
    @SLog(tag = "添加文章", msg = "文章标题:${args[1].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(String siteid, @Param("..") Cms_article article, @Param("time_param") long[] time, HttpServletRequest req) {
        try {
            article.setPublishAt(time[0] / 1000);
            article.setEndAt(time[1] / 1000);
            article.setSiteid(siteid);
            article.setStatus(0);
            article.setOpBy(StringUtil.getPlatformUid());
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
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
