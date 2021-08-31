package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.sys.models.Sys_task;
import com.budwk.app.sys.services.SysTaskService;
import com.budwk.app.task.services.TaskPlatformService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

;

@IocBean
@At("/platform/sys/task")
public class SysTaskController {
    private static final Log log = Logs.get();
    @Inject
    private SysTaskService sysTaskService;
    @Inject
    private TaskPlatformService taskPlatformService;

    @At("")
    @Ok("beetl:/platform/sys/task/index.html")
    @SaCheckPermission("sys.manager.task")
    public void index() {

    }

    @At
    @Ok("beetl:/platform/sys/task/cron.html")
    @SaCheckPermission("sys.manager.task")
    public void cron() {

    }

    @At
    @Ok("json")
    @SaCheckPermission("sys.manager.task")
    public Object cronExp(@Param("cron") String cron) {
        try {
            return Result.success().addData(taskPlatformService.getCronExeTimes(cron));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("sys.manager.task")
    public Object data(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysTaskService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SLog(tag = "新建任务", msg = "任务名称:${args[0].name}")
    @SaCheckPermission("sys.manager.task.add")
    public Object addDo(@Param("..") Sys_task task, HttpServletRequest req) {
        try {
            task.setCreatedBy(SecurityUtil.getUserId());
            Sys_task sysTask = sysTaskService.insert(task);
            taskPlatformService.add(sysTask.getId(), sysTask.getId(), sysTask.getJobClass(), sysTask.getCron(),
                    sysTask.getNote(), sysTask.getData());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.task")
    public Object edit(String id) {
        try {
            return Result.success().addData(sysTaskService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SLog(tag = "修改任务", msg = "任务名称:${args[0].name}")
    @SaCheckPermission("sys.manager.task.edit")
    public Object editDo(@Param("..") Sys_task task, HttpServletRequest req) {
        try {
            task.setUpdatedBy(SecurityUtil.getUserId());
            sysTaskService.updateIgnoreNull(task);
            if (taskPlatformService.isExist(task.getId(), task.getId())) {
                taskPlatformService.delete(task.getId(), task.getId());
            }
            if (!task.isDisabled()) {
                taskPlatformService.add(task.getId(), task.getId(), task.getJobClass(), task.getCron(),
                        task.getNote(), task.getData());
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At("/delete/?")
    @Ok("json")
    @SLog(tag = "删除任务", msg = "任务名称:${args[1].getAttribute('name')}")
    @SaCheckPermission("sys.manager.task.delete")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_task sysTask = sysTaskService.fetch(id);
            try {
                if (taskPlatformService.isExist(sysTask.getId(), sysTask.getId())) {
                    taskPlatformService.delete(sysTask.getId(), sysTask.getId());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            sysTaskService.delete(id);
            req.setAttribute("name", sysTask.getName());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/enable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.task.edit")
    @SLog(tag = "启用任务", msg = "任务名:${args[1].getAttribute('name')}")
    public Object enable(String id, HttpServletRequest req) {
        try {
            Sys_task sysTask = sysTaskService.fetch(id);
            req.setAttribute("name", sysTask.getName());
            sysTaskService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            if (!taskPlatformService.isExist(sysTask.getId(), sysTask.getId())) {
                taskPlatformService.add(sysTask.getId(), sysTask.getId(), sysTask.getJobClass(), sysTask.getCron(),
                        sysTask.getNote(), sysTask.getData());
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/disable/?")
    @Ok("json")
    @SaCheckPermission("sys.manager.task.edit")
    @SLog(tag = "禁用任务", msg = "任务名:${args[1].getAttribute('name')}")
    public Object disable(String id, HttpServletRequest req) {
        try {
            Sys_task sysTask = sysTaskService.fetch(id);
            req.setAttribute("name", sysTask.getName());
            sysTaskService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            if (taskPlatformService.isExist(sysTask.getId(), sysTask.getId())) {
                taskPlatformService.delete(sysTask.getId(), sysTask.getId());
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
