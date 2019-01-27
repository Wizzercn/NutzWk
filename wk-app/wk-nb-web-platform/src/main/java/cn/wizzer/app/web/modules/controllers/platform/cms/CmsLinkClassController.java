package cn.wizzer.app.web.modules.controllers.platform.cms;

import cn.wizzer.app.cms.modules.models.Cms_link_class;
import cn.wizzer.app.cms.modules.services.CmsLinkClassService;
import cn.wizzer.app.cms.modules.services.CmsLinkService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wizzer on 2016/6/28.
 */
@IocBean
@At("/platform/cms/link/class")
public class CmsLinkClassController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private CmsLinkClassService cmsLinkClassService;
    @Inject
    @Reference
    private CmsLinkService cmsLinkService;


    @At("")
    @Ok("beetl:/platform/cms/link/class/index.html")
    @RequiresPermissions("cms.link.class")
    public void index() {

    }

    @At
    @Ok("beetl:/platform/cms/link/class/add.html")
    @RequiresPermissions("cms.link.class")
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.link.class.add")
    @SLog(tag = "添加链接分类", msg = "分类名称:${args[0].name}")
    public Object addDo(@Param("..") Cms_link_class linkClass, HttpServletRequest req) {
        try {
            linkClass.setOpBy(StringUtil.getPlatformUid());
            cmsLinkClassService.insert(linkClass);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/cms/link/class/edit.html")
    @RequiresPermissions("cms.link.class")
    public Object edit(String id) {
        return cmsLinkClassService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("cms.link.class.edit")
    @SLog(tag = "修改链接分类", msg = "分类名称:${args[0].name}")
    public Object editDo(@Param("..") Cms_link_class linkClass, HttpServletRequest req) {
        try {
            cmsLinkClassService.updateIgnoreNull(linkClass);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("cms.link.class.delete")
    @SLog(tag = "删除链接分类", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String oneId, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                cmsLinkClassService.delete(ids);
                cmsLinkService.clear(Cnd.where("classId", "in", ids));
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                cmsLinkClassService.delete(oneId);
                cmsLinkService.clear(Cnd.where("classId", "=", oneId));
                req.setAttribute("id", oneId);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("cms.link.class")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return cmsLinkClassService.data(length, start, draw, order, columns, cnd, null);
    }


}
