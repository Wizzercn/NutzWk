package cn.wizzer.app.web.commons.quartz.job;

import cn.wizzer.app.sys.modules.models.Sys_task;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
@IocBean
public class TestJob implements Job {

    private static final Log log = Logs.get();
    @Inject
    protected Dao dao;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String taskId = context.getJobDetail().getKey().getName();
        String hi = data.getString("hi");
        log.info("Test Job hi::" + hi);
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));
    }
}
