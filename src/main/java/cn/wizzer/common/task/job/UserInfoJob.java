package cn.wizzer.common.task.job;

import cn.wizzer.common.action.BaseAction;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.*;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 获取微信用户资料
 * Created by Wizzer on 14-4-2.
 */
@IocBean
public class UserInfoJob extends BaseAction implements Job {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();
    private static String task_code = "";
    private static String param_value = "";
    private static int task_interval = 0;
    private static int task_id = 0;
    private static int task_threadnum = 1;
    private static int pageSize = 20;
    private ExecutorService pool;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jdm = context.getJobDetail().getJobDataMap();
            param_value = Strings.sNull(jdm.get("param_value"));
            task_code = Strings.sNull(jdm.get("task_code"));
            task_interval = NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_interval"))) : 1000;
            task_id = NumberUtils.toInt(Strings.sNull(jdm.get("task_id")));
            task_threadnum = NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) > 0 ? NumberUtils.toInt(Strings.sNull(jdm.get("task_threadnum"))) : 1;
            log.info("PushDataJob param_value:" + param_value + " task_code:" + task_code + " task_interval:" + task_interval + " task_threadnum:" + task_threadnum + " task_id:" + task_id);
            pageSize = NumberUtils.toInt(param_value) > 0 ? NumberUtils.toInt(param_value) : 20;
            init();
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void init() {
        pool = Executors.newFixedThreadPool(task_threadnum);

    }

}
