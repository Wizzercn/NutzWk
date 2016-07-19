package cn.wizzer.modules.back.cms.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.back.cms.models.Cms_site;
import cn.wizzer.modules.back.cms.services.CmsSiteService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/private/cms/site")
@Filters({@By(type = PrivateFilter.class)})
public class CmsSiteController {
    private static final Log log = Logs.get();
    @Inject
    CmsSiteService cmsSiteService;

    @At("")
    @Ok("beetl:/private/cms/site/index.html")
    @RequiresAuthentication
    public Object index() {
        Cms_site site = cmsSiteService.fetch("site");
        if (site == null) {
            site = new Cms_site();
            site.setId("site");
            cmsSiteService.insert(site);
        }
        return site;
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.site.settings.save")
    @SLog(tag = "修改站点", msg = "${args[0].site_name}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_site site) {
        try {
            cmsSiteService.updateIgnoreNull(site);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
