package com.budwk.app.web.controllers.platform.cms;

import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.cms.models.Cms_link;
import com.budwk.app.cms.models.Cms_link_class;
import com.budwk.app.cms.services.CmsLinkClassService;
import com.budwk.app.cms.services.CmsLinkService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.base.result.Result;;
import com.budwk.app.web.commons.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/link/link")
public class CmsLinkController {
    private static final Log log = Logs.get();
    @Inject
    private CmsLinkClassService cmsLinkClassService;
    @Inject
    private CmsLinkService cmsLinkService;

    @At({"", "/?"})
    @Ok("beetl:/platform/cms/link/link/index.html")
    @RequiresPermissions("cms.link.link")
    public void index(String classId, HttpServletRequest req) {
        Cms_link_class classObj = null;
        List<Cms_link_class> list = cmsLinkClassService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(classId)) {
            classObj = list.get(0);
        }
        if (Strings.isNotBlank(classId)) {
            classObj = cmsLinkClassService.fetch(classId);
        }
        req.setAttribute("list", list);
        req.setAttribute("classObj", classObj);
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.link.link.add")
    @SLog(tag = "添加链接", msg = "分类名称:${args[0].name}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(@Param("..") Cms_link link, HttpServletRequest req) {
        try {
            link.setCreatedBy(ShiroUtil.getPlatformUid());
            cmsLinkService.insert(link);
            cmsLinkService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("cms.link.link")
    public Object edit(String id, HttpServletRequest req) {
        try {
            return Result.success().addData(cmsLinkService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.link.link.edit")
    @SLog(tag = "修改链接", msg = "分类名称:${args[0].name}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_link link, HttpServletRequest req) {
        try {
            cmsLinkService.updateIgnoreNull(link);
            cmsLinkService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("cms.link.link.delete")
    @SLog(tag = "删除链接", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String oneId, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                cmsLinkService.delete(ids);
                req.setAttribute("id", StringUtils.toString(ids));
            } else {
                cmsLinkService.delete(oneId);
                req.setAttribute("id", oneId);
            }
            cmsLinkService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/data/", "/data/?"})
    @Ok("json:full")
    @RequiresPermissions("cms.link.link")
    public Object data(String classId, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            cnd.and("classId", "=", classId);
            if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(cmsLinkService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }

}
