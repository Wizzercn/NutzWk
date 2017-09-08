package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.wf.modules.models.Wf_category;
import cn.wizzer.app.wf.modules.services.WfCategoryService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import cn.wizzer.framework.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/wf/cfg/deploy")
public class WfCfgDeployController {
    private static final Log log = Logs.get();
    @Inject
    private WfCategoryService wfCategoryService;

    @At("")
    @Ok("beetl:/platform/wf/deploy/index.html")
    @RequiresPermissions("wf.cfg.deploy")
    public void index() {
    }

    @At("/data")
    @Ok("json")
    @RequiresPermissions("wf.cfg.category")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return wfCategoryService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/add")
    @Ok("beetl:/platform/wf/category/add.html")
    @RequiresPermissions("wf.cfg.category")
    public void add() {

    }

    @At("/addDo")
    @Ok("json")
    @RequiresPermissions("wf.cfg.category")
    @SLog(tag = "Wf_category", msg = "${args[0].id}")
    public Object addDo(@Param("..") Wf_category wfCategory, HttpServletRequest req) {
        try {
            wfCategoryService.insert(wfCategory);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/wf/category/edit.html")
    @RequiresPermissions("wf.cfg.category")
    public void edit(String id, HttpServletRequest req) {
        req.setAttribute("obj", wfCategoryService.fetch(id));
    }

    @At("/editDo")
    @Ok("json")
    @RequiresPermissions("wf.cfg.category")
    @SLog(tag = "Wf_category", msg = "${args[0].id}")
    public Object editDo(@Param("..") Wf_category wfCategory, HttpServletRequest req) {
        try {
            wfCategory.setOpBy(StringUtil.getUid());
            wfCategory.setOpAt((int) (System.currentTimeMillis() / 1000));
            wfCategoryService.updateIgnoreNull(wfCategory);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("wf.cfg.category")
    @SLog(tag = "Wf_category", msg = "${req.getAttribute('id')}")
    public Object delete(String id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                wfCategoryService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                wfCategoryService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
