package cn.wizzer.modules.back.cms.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.cms.models.Cms_channel;
import cn.wizzer.modules.back.cms.models.Cms_site;
import cn.wizzer.modules.back.cms.services.CmsArticleService;
import cn.wizzer.modules.back.cms.services.CmsChannelService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/cms/article")
@Filters({@By(type = PrivateFilter.class)})
public class CmsArticleController {
    private static final Log log = Logs.get();
    @Inject
    CmsArticleService cmsArticleService;
    @Inject
    CmsChannelService cmsChannelService;

    @At("")
    @Ok("beetl:/private/cms/article/index.html")
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
}
