package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.app.web.commons.activiti.cmd.ProcessDefinitionDiagramCmd;
import cn.wizzer.app.wf.modules.services.WfCategoryService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IocBean
@At("/platform/wf/cfg/flow")
public class WfCfgFlowController {
    private static final Log log = Logs.get();
    @Inject
    private WfCategoryService wfCategoryService;
    @Inject
    private RepositoryService repositoryService;
    @Inject
    private ProcessEngine processEngine;
    @Inject
    private RuntimeService runtimeService;

    @At("")
    @Ok("beetl:/platform/wf/flow/index.html")
    @RequiresPermissions("wf.cfg.flow")
    public void index(HttpServletRequest req) {
        req.setAttribute("list", wfCategoryService.query(Cnd.orderBy().asc("location")));
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wf.cfg.flow")
    public Object data(@Param("keyword") String keyword, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        List<ProcessInstance> list;
        long total;
        Context.setProcessEngineConfiguration(Mvcs.ctx().getDefaultIoc().get(null, "processEngineSpec"));
        ProcessInstanceQuery processInstanceQuery;
        if (Strings.isNotBlank(keyword)) {
            processInstanceQuery = runtimeService.createProcessInstanceQuery().processDefinitionName(keyword).orderByProcessInstanceId().desc();
        } else {
            processInstanceQuery = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().desc();
        }
        total = processInstanceQuery.count();
        list = processInstanceQuery.listPage(start, length);
        NutMap re = new NutMap();
        re.put("recordsFiltered", total);
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    @At("/end/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object end(String processInstanceId) {
        try {
            processEngine.getRuntimeService().deleteProcessInstance(
                    processInstanceId, "end");
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @At("/suspend/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object suspend(String processInstanceId) {
        try {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @At("/active/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object active(String processInstanceId) {
        try {
            runtimeService.activateProcessInstanceById(processInstanceId);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
