package com.budwk.app.web.controllers.platform.cms;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.cms.models.Cms_article;
import com.budwk.app.cms.models.Cms_channel;
import com.budwk.app.cms.models.Cms_site;
import com.budwk.app.cms.services.CmsArticleService;
import com.budwk.app.cms.services.CmsChannelService;
import com.budwk.app.cms.services.CmsSiteService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

;

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
    @Inject
    private CmsSiteService cmsSiteService;

    @At(value = {"", "/?"})
    @Ok("beetl:/platform/cms/article/index.html")
    @SaCheckPermission("cms.content.article")
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
    @SaCheckPermission("cms.content.article")
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
    @SaCheckPermission("cms.content.article")
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
            return Result.success().addData(cmsArticleService.listPage(pageNumber, pageSize, cnd, "^(id|siteid|title|author|disabled|publishAt|endAt|location|view_num)$"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/addDo/?")
    @Ok("json")
    @SaCheckPermission("cms.content.article.add")
    @SLog(tag = "添加文章", msg = "文章标题:${args[1].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(String siteId, @Param("..") Cms_article article, @Param("time_param") long[] time, HttpServletRequest req) {
        try {
            article.setPublishAt(time[0] / 1000);
            article.setEndAt(time[1] / 1000);
            article.setSiteId(siteId);
            article.setStatus(0);
            article.setCreatedBy(SecurityUtil.getUserId());
            cmsArticleService.insert(article);
            cmsArticleService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("cms.content.article")
    public Object edit(String id, HttpServletRequest req) {
        try {
            Cms_article article = cmsArticleService.fetch(id);
            return Result.success().addData(article);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("cms.content.article.edit")
    @SLog(tag = "修改文章", msg = "文章标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_article article, @Param("time_param") long[] time, HttpServletRequest req) {
        try {
            article.setPublishAt(time[0] / 1000);
            article.setEndAt(time[1] / 1000);
            article.setStatus(0);
            article.setUpdatedBy(SecurityUtil.getUserId());
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
    @SaCheckPermission("cms.content.article.edit")
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
    @SaCheckPermission("cms.content.article.edit")
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
    @SaCheckPermission("cms.content.article.delete")
    @SLog(tag = "删除文章", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String oneId, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                cmsArticleService.delete(ids);
                req.setAttribute("id", Arrays.toString(ids));
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
