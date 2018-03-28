package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_task;
import cn.wizzer.app.sys.modules.services.SysTaskService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/sys/task")
public class SysTaskController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysTaskService sysTaskService;
    @Inject
    private PubSubService pubSubService;

    @At("")
    @Ok("beetl:/platform/sys/task/index.html")
    @RequiresPermissions("sys.manager.task")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.task")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return sysTaskService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/platform/sys/task/add.html")
    @RequiresPermissions("sys.manager.task")
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建任务", msg = "任务名:${args[0].name}")
    @RequiresPermissions("sys.manager.task.add")
    public Object addDo(@Param("..") Sys_task task, HttpServletRequest req) {
        try {
            sysTaskService.insert(task);
            //通过redis通知后台任务执行初始化
            NutMap map = NutMap.NEW();
            map.addv("method", "add");
            map.addv("jobName", task.getId());
            map.addv("jobGroup", task.getId());
            map.addv("className", task.getJobClass());
            map.addv("cron", task.getCron());
            map.addv("comment", task.getNote());
            map.addv("dataMap", task.getData());
            pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/task/edit.html")
    @RequiresPermissions("sys.manager.task")
    public Object edit(String id) {
        return sysTaskService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改任务", msg = "任务名:${args[0].name}")
    @RequiresPermissions("sys.manager.task.edit")
    public Object editDo(@Param("..") Sys_task task, HttpServletRequest req) {
        try {
            task.setOpBy(StringUtil.getPlatformUid());
            task.setOpAt(Times.getTS());
            sysTaskService.updateIgnoreNull(task);
            //通过redis通知后台任务执行初始化
            NutMap map = NutMap.NEW();
            map.addv("method", "update");
            map.addv("jobName", task.getId());
            map.addv("jobGroup", task.getId());
            map.addv("className", task.getJobClass());
            map.addv("cron", task.getCron());
            map.addv("comment", task.getNote());
            map.addv("dataMap", task.getData());
            pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
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
                List<Sys_task> taskList = sysTaskService.query(Cnd.where("id", "in", ids));
                for (Sys_task sysTask : taskList) {
                    try {
                        NutMap map = NutMap.NEW();
                        map.addv("method", "delete");
                        map.addv("jobName", sysTask.getId());
                        map.addv("jobGroup", sysTask.getId());
                        pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                sysTaskService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                Sys_task sysTask = sysTaskService.fetch(id);
                try {
                    NutMap map = NutMap.NEW();
                    map.addv("method", "delete");
                    map.addv("jobName", sysTask.getId());
                    map.addv("jobGroup", sysTask.getId());
                    pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                sysTaskService.delete(id);
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
            Sys_task sysTask = sysTaskService.fetch(id);
            req.setAttribute("name", sysTask.getName());
            sysTaskService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
            //通过redis通知后台任务执行初始化
            NutMap map = NutMap.NEW();
            map.addv("method", "enable");
            map.addv("jobName", sysTask.getId());
            map.addv("jobGroup", sysTask.getId());
            map.addv("className", sysTask.getJobClass());
            map.addv("cron", sysTask.getCron());
            map.addv("comment", sysTask.getNote());
            map.addv("dataMap", sysTask.getData());
            pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
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
            Sys_task sysTask = sysTaskService.fetch(id);
            req.setAttribute("name", sysTask.getName());
            sysTaskService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
            //通过redis通知后台任务执行初始化
            NutMap map = NutMap.NEW();
            map.addv("method", "disable");
            map.addv("jobName", sysTask.getId());
            map.addv("jobGroup", sysTask.getId());
            pubSubService.fire("nutzwk:task:platform", Json.toJson(map));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
