package cn.wizzer.app.task.commons.base;

import cn.wizzer.app.sys.modules.models.Sys_task;
import cn.wizzer.app.sys.modules.services.SysTaskService;
import cn.wizzer.app.task.modules.services.TaskPlatformService;
import org.nutz.dao.Cnd;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.List;

/**
 * Created by wizzer on 2018/3/19.
 */
@IocBean
public class Globals {
    private static final Log log = Logs.get();
    @Inject
    private QuartzManager quartzManager;
    @Inject
    private TaskPlatformService taskPlatformService;

    public void init(SysTaskService sysTaskService) {
        List<Sys_task> taskList = sysTaskService.query();
        for (Sys_task sysTask : taskList) {
            try {
                if (sysTask.isDisabled()) {//禁用
                    boolean isExist = taskPlatformService.isExist(sysTask.getId(), sysTask.getId());
                    if (isExist)//存在则删除
                        taskPlatformService.delete(sysTask.getId(), sysTask.getId());
                } else {//启用
                    boolean isExist = taskPlatformService.isExist(sysTask.getId(), sysTask.getId());
                    if (!isExist)//不存在则新建
                        taskPlatformService.add(sysTask.getId(), sysTask.getId(), sysTask.getJobClass(), sysTask.getCron()
                                , sysTask.getNote(), sysTask.getData());
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
