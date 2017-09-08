package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.app.web.commons.activiti.cmd.ProcessDefinitionDiagramCmd;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.wf.modules.models.Wf_category;
import cn.wizzer.app.wf.modules.services.WfCategoryService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import cn.wizzer.framework.util.StringUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

@IocBean
@At("/platform/wf/cfg/deploy")
public class WfCfgDeployController {
    private static final Log log = Logs.get();
    @Inject
    private WfCategoryService wfCategoryService;
    @Inject
    private RepositoryService repositoryService;
    @Inject
    private ProcessEngine processEngine;

    @At("")
    @Ok("beetl:/platform/wf/deploy/index.html")
    @RequiresPermissions("wf.cfg.deploy")
    public void index(HttpServletRequest req) {
        req.setAttribute("list", wfCategoryService.query(Cnd.orderBy().asc("location")));
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wf.cfg.deploy")
    public Object data(@Param("categoryId") String categoryId, @Param("keyword") String keyword, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        List<ProcessDefinition> list;
        long total;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinitionQuery processDefinitionQuery2;
        if (!Strings.isEmpty(categoryId) && !Strings.isEmpty(keyword)) {
            processDefinitionQuery2 = processDefinitionQuery.processDefinitionCategory(categoryId);
        } else if (!Strings.isEmpty(categoryId)) {
            processDefinitionQuery2 = processDefinitionQuery.processDefinitionCategory(categoryId);
        } else if (!Strings.isEmpty(keyword)) {
            processDefinitionQuery2 = processDefinitionQuery.processDefinitionNameLike(keyword);
        } else {
            processDefinitionQuery2 = processDefinitionQuery;
        }
        total = processDefinitionQuery2.count();
        list = processDefinitionQuery2.orderByProcessDefinitionId().desc().listPage(start, length);
        NutMap re = new NutMap();
        re.put("recordsFiltered", total);
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }

    @At("/suspend/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object suspend(String processDefinitionId) {
        try {
            repositoryService.suspendProcessDefinitionById(processDefinitionId,
                    true, null);
            return Result.success("挂起成功，processDefinitionId=" + processDefinitionId);
        } catch (Exception e) {
            return Result.error("挂起失败：processDefinitionId=" + processDefinitionId + "\r\n" + e.getMessage());
        }
    }

    @At("/active/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object active(String processDefinitionId) {
        try {
            repositoryService.activateProcessDefinitionById(processDefinitionId,
                    true, null);
            return Result.success("激活成功，processDefinitionId=" + processDefinitionId);
        } catch (Exception e) {
            return Result.error("激活失败：processDefinitionId=" + processDefinitionId + "\r\n" + e.getMessage());
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wf.cfg.flow")
    public Object delete(String deployId) {
        try {
            repositoryService.deleteDeployment(deployId, true);//是否级联删除实例资源
            return Result.success("删除成功，deployId=" + deployId);
        } catch (Exception e) {
            return Result.error("删除失败：deployId=" + deployId + "\r\n" + e.getMessage());
        }
    }

    @At("/graph/?")
    @Ok("void")
    @RequiresPermissions("wf.cfg.flow")
    public void graph(String processDefinitionId,
                      HttpServletResponse response) throws Exception {
        Command<InputStream> cmd = new ProcessDefinitionDiagramCmd(
                processDefinitionId);
        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        response.setContentType("image/png");
        IOUtils.copy(is, response.getOutputStream());
    }

    @At("/xml/?")
    @Ok("void")
    @RequiresPermissions("wf.cfg.flow")
    public void xml(String processDefinitionId,
                    HttpServletResponse response) throws Exception {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        String resourceName = processDefinition.getResourceName();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), resourceName);
        response.setContentType("text/xml;charset=UTF-8");
        IOUtils.copy(resourceAsStream, response.getOutputStream());
    }
}
