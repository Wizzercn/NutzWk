package cn.xuetang.common.task;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Globals;
import cn.xuetang.modules.sys.bean.Sys_task;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.quartz.NutQuartzJobFactory;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.*;

import java.util.*;

/**
 * Created by Wizzer on 14-3-31.
 */
@IocBean
public class LoadTask extends BaseAction implements Runnable {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();

    public void run() {
        List<Sys_task> tasks = daoCtl.list(dao, Sys_task.class, Cnd.where("is_enable", "=", 0));
        try {
            log.info("tasks.size:" + tasks.size());
            Globals.SCHEDULER.setJobFactory(new NutQuartzJobFactory());
            for (int i = 0; i < tasks.size(); i++) {
                Sys_task task = tasks.get(i);

                Map<String, String> map = new HashMap<String, String>();
                map.put("param_value", task.getParam_value());
                map.put("task_interval", String.valueOf(task.getTask_interval()));
                JobBuilder jobBuilder = JobBuilder.newJob(getClassByTask(task.getJob_class()));
                TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
                if (StringUtils.isNotBlank(task.getTask_code())) {
                    jobBuilder.withIdentity(task.getTask_code(), Scheduler.DEFAULT_GROUP);
                    triggerBuilder.withIdentity(task.getTask_code(), Scheduler.DEFAULT_GROUP);
                } else {
                    UUID uuid = UUID.randomUUID();
                    jobBuilder.withIdentity(uuid.toString(), Scheduler.DEFAULT_GROUP);
                    triggerBuilder.withIdentity(uuid.toString(), Scheduler.DEFAULT_GROUP);
                    task.setTask_code(uuid.toString());
                    daoCtl.update(dao, task);
                }
                map.put("task_code", String.valueOf(task.getTask_code()));
                map.put("task_id", String.valueOf(task.getTask_id()));
                map.put("task_threadnum", String.valueOf(task.getTask_threadnum()));
                jobBuilder.setJobData(getJobDataMap(map));
                String cronExpressionFromDB = getCronExpressionFromDB(task);
                log.info(cronExpressionFromDB);
                triggerBuilder.withSchedule(getCronScheduleBuilder(cronExpressionFromDB));
                //调度任务
                Globals.SCHEDULER.scheduleJob(jobBuilder.build(), triggerBuilder.build());
            }
            if (tasks.size() > 0) {
                Globals.SCHEDULER.start();
            }
        } catch (SchedulerException e) {
            log.error(e);
        } catch (ClassNotFoundException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public static CronScheduleBuilder getCronScheduleBuilder(String cronExpression) {
        return CronScheduleBuilder.cronSchedule(cronExpression);
    }

    public String getCronExpressionFromDB(Sys_task task) {
        if (task.getExecycle() == 2) {
            return task.getCron_expression();
        } else {
            int execycle = task.getTask_interval_unit();
            String excep = "";
            if (execycle == 5) {//月
                excep = "0  " + task.getMinute() + " " + task.getHour() + " " + task.getDay_of_month() + " * ?";
            } else if (execycle == 4) {//周
                excep = "0  " + task.getMinute() + " " + task.getHour() + " " + " ? " + " * " + task.getDay_of_week();
            } else if (execycle == 3) {//日
                excep = "0  " + task.getMinute() + " " + task.getHour() + " " + " * * ?";
            } else if (execycle == 2) {//时
                excep = "0 0 */" + task.getInterval_hour() + " * * ?";
            } else if (execycle == 1) {//分
                excep = "0  */" + task.getInterval_minute() + " * * * ?";
            }
            return excep;
        }
    }

    /**
     * @param params 任务参数
     * @return
     */
    private JobDataMap getJobDataMap(Map<String, String> params) {
        JobDataMap jdm = new JobDataMap();
        Set<String> keySet = params.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            jdm.put(key, params.get(key));
        }
        return jdm;
    }

    /**
     * @param taskClassName 任务执行类名
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private Class getClassByTask(String taskClassName) throws ClassNotFoundException {
        return Class.forName(taskClassName);
    }
}
