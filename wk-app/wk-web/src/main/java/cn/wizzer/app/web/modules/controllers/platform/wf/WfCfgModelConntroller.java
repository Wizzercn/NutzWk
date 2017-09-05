package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import java.util.List;

/**
 * Created by Wizzer on 2017/9/5.
 */
@IocBean
@At("/platform/wf/cfg/model")
public class WfCfgModelConntroller {
    private static final Log log = Logs.get();
    @Inject
    private ProcessEngine processEngine;
    @Inject
    private RepositoryService repositoryService;

    @At("")
    @Ok("beetl:/platform/wf/model/index.html")
    @RequiresPermissions("wf.cfg.model")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wf.cfg.model")
    public Object data(@Param("categoryId") String categoryId, @Param("keyword") String keyword, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        List<Model> list;
        long total;
        ModelQuery modelQuery = repositoryService.createModelQuery();
        ModelQuery modelQuery2;
        if (!Strings.isEmpty(categoryId) && !Strings.isEmpty(keyword)) {
            modelQuery2 = modelQuery.modelCategory(categoryId).modelNameLike(keyword);
        } else if (!Strings.isEmpty(categoryId)) {
            modelQuery2 = modelQuery.modelCategory(categoryId);
        } else if (!Strings.isEmpty(keyword)) {
            modelQuery2 = modelQuery.modelNameLike(keyword);
        } else {
            modelQuery2 = modelQuery;
        }
        total = modelQuery2.count();
        list = modelQuery2.orderByCreateTime().desc().listPage(start, length);
        NutMap re = new NutMap();
        re.put("recordsFiltered", total);
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

}
