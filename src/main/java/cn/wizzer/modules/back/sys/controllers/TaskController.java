package cn.wizzer.modules.back.sys.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.sys.models.Sys_task;
import cn.wizzer.modules.back.sys.services.TaskService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.*;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.annotation.Chain;
import org.quartz.JobKey;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/private/sys/task")
@Filters({@By(type = PrivateFilter.class)})
public class TaskController {
    private static final Log log = Logs.get();
    @Inject
    private TaskService taskService;
    @Inject
    private QuartzManager quartzManager;

    @At("")
    @Ok("beetl:/private/sys/task/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return taskService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/private/sys/task/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建任务", msg = "任务名:${args[0].name}")
    @RequiresPermissions("sys.manager.task.add")
    public Object addDo(@Param("..") Sys_task task, HttpServletRequest req) {
        try {
            Sys_task sysTask = taskService.insert(task);
            try {
                if (!sysTask.isDisabled()) {
                    QuartzJob qj = new QuartzJob();
                    qj.setJobName(sysTask.getId());
                    qj.setJobGroup(sysTask.getId());
                    qj.setClassName(sysTask.getJobClass());
                    qj.setCron(sysTask.getCron());
                    qj.setComment(sysTask.getNote());
                    qj.setDataMap(sysTask.getData());
                    quartzManager.add(qj);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/sys/task/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return taskService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改任务", msg = "任务名:${args[0].name}")
    @RequiresPermissions("sys.manager.task.edit")
    public Object editDo(@Param("..") Sys_task sysTask, HttpServletRequest req) {
        try {
            //int count=quartzManager.query(sysTask.getId(), sysTask.getId(), null).size();
            try {
                boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
                if (isExist) {
                    QuartzJob qj = new QuartzJob();
                    qj.setJobName(sysTask.getId());
                    qj.setJobGroup(sysTask.getId());
                    quartzManager.delete(qj);
                }
                if (!sysTask.isDisabled()) {
                    QuartzJob qj = new QuartzJob();
                    qj.setJobName(sysTask.getId());
                    qj.setJobGroup(sysTask.getId());
                    qj.setClassName(sysTask.getJobClass());
                    qj.setCron(sysTask.getCron());
                    qj.setComment(sysTask.getNote());
                    qj.setDataMap(sysTask.getData());
                    quartzManager.add(qj);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            sysTask.setOpBy(Strings.sNull(req.getAttribute("uid")));
            sysTask.setOpAt((int) (System.currentTimeMillis() / 1000));
            taskService.updateIgnoreNull(sysTask);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @At({"/delete", "/delete/?"})
    @Ok("json")
    @SLog(tag = "删除任务", msg = "任务ID:${args[2].getAttribute('id')}")
    @RequiresPermissions("sys.manager.task.delete")
    public Object delete(String id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                List<Sys_task> taskList = taskService.query(Cnd.where("id", "in", ids));
                for (Sys_task sysTask : taskList) {
                    try {
                        QuartzJob qj = new QuartzJob();
                        qj.setJobName(sysTask.getId());
                        qj.setJobGroup(sysTask.getId());
                        quartzManager.delete(qj);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                taskService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                Sys_task sysTask = taskService.fetch(id);
                try {
                    boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
                    if (isExist) {
                        QuartzJob qj = new QuartzJob();
                        qj.setJobName(sysTask.getId());
                        qj.setJobGroup(sysTask.getId());
                        quartzManager.delete(qj);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                taskService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.task.edit")
    @SLog(tag = "启用任务", msg = "任务名:${args[1].getAttribute('name')}")
    public Object enable(String id, HttpServletRequest req) {
        try {
            Sys_task sysTask = taskService.fetch(id);
            try {
                boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
                if (!isExist) {
                    QuartzJob qj = new QuartzJob();
                    qj.setJobName(sysTask.getId());
                    qj.setJobGroup(sysTask.getId());
                    qj.setClassName(sysTask.getJobClass());
                    qj.setCron(sysTask.getCron());
                    qj.setComment(sysTask.getNote());
                    qj.setDataMap(sysTask.getData());
                    quartzManager.add(qj);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            req.setAttribute("name", sysTask.getName());
            taskService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.task.edit")
    @SLog(tag = "禁用任务", msg = "任务名:${args[1].getAttribute('name')}")
    public Object disable(String id, HttpServletRequest req) {
        try {
            Sys_task sysTask = taskService.fetch(id);
            try {
                boolean isExist = quartzManager.exist(new JobKey(sysTask.getId(), sysTask.getId()));
                if (isExist) {
                    QuartzJob qj = new QuartzJob();
                    qj.setJobName(sysTask.getId());
                    qj.setJobGroup(sysTask.getId());
                    quartzManager.delete(qj);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            req.setAttribute("name", sysTask.getName());
            taskService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
