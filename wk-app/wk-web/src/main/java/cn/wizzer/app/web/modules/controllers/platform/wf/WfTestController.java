package cn.wizzer.app.web.modules.controllers.platform.wf;

import cn.wizzer.app.sys.modules.models.Sys_role;
import cn.wizzer.app.sys.modules.models.Sys_user;
import cn.wizzer.app.sys.modules.services.SysRoleService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer on 2017/9/8.
 */
@IocBean
@At("/wf/test")
public class WfTestController {
    private static final Log log = Logs.get();
    @Inject
    private IdentityService identityService;
    @Inject
    private RuntimeService runtimeService;
    @Inject
    private TaskService taskService;
    @Inject
    private SysUserService sysUserService;

    @At("/start")
    @Ok("json")
    public Object startAutoCompleteFirst(@Param("flowKey") String flowKey, @Param("userId") String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (!Strings.isEmpty(userId)) {
                identityService.setAuthenticatedUserId(userId);
            }
            Sys_user sysUser = sysUserService.fetch(userId);
            sysUserService.fetchLinks(sysUser, "roles");
            List<String> roles = new ArrayList<>();
            for (Sys_role role : sysUser.getRoles()) {
                roles.add(role.getCode());
            }
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(flowKey);
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskCandidateGroupIn(roles).singleResult();
            taskService.setOwner(task.getId(), userId);
            taskService.claim(task.getId(), userId);
            taskService.complete(task.getId());
            map.put("errcode", 0);
            map.put("errmsg", "");
            map.put("processInstanceId", processInstance.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return map;
    }
}
