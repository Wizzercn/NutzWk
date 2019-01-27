package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_site;
import cn.wizzer.app.cms.modules.services.CmsSiteService;
import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/site")
public class CmsSiteController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private CmsSiteService cmsSiteService;

    @At("")
    @Ok("beetl:/platform/cms/site/index.html")
    @RequiresPermissions("cms.site.settings")
    public Object index() {
        Cms_site site = cmsSiteService.fetch("site");
        if (site == null) {
            site = new Cms_site();
            site.setId("site");
            site.setSite_name("Demo");
            site.setSite_domain("https://wizzer.cn");
            cmsSiteService.insert(site);
        }
        return site;
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("cms.content.article")
    public Object data(@Param("siteName") String siteName,@Param("siteDomain")String siteDomain,
            @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize,
            @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.where("delFlag","=",false);
        if(Strings.isNotBlank(siteName)){
            cnd.and("site_name","like","%"+siteName+"%");
        }
        if(Strings.isNotBlank(siteDomain)){
            cnd.and("site_domain","like","%"+siteDomain+"%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(cmsSiteService.listPage(pageNumber,pageSize, cnd));
    }

    @At("/add")
    @Ok("beetl:/platform/cms/site/add.html")
    @RequiresPermissions("cms.site.settings")
    public void add() {

    }

    @At("/edit/?")
    @Ok("json:full")
    @RequiresPermissions("cms.site.settings")
    public Object edit(String id) {
        try {
            return Result.success("system.success", cmsSiteService.fetch(id));
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.site.settings.save")
    @SLog(tag = "添加站点", msg = "${args[0].site_name}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(@Param("..") Cms_site site) {
        try {
            site.setOpAt(Times.getTS());
            site.setOpBy(StringUtil.getPlatformUid());
            cmsSiteService.insert(site);
            cmsSiteService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.site.settings.save")
    @SLog(tag = "修改站点", msg = "${args[0].site_name}")
    @AdaptBy(type = WhaleAdaptor.class)
    public Object editDo(@Param("..") Cms_site site) {
        try {
            site.setOpAt(Times.getTS());
            site.setOpBy(StringUtil.getPlatformUid());
            cmsSiteService.updateIgnoreNull(site);
            cmsSiteService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.conf.delete")
    @SLog(tag = "删除站点", msg = "站点:${args[0]}")
    public Object delete(String id) {
        try {
            cmsSiteService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
