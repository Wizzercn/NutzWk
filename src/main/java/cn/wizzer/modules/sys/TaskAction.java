package cn.wizzer.modules.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.wizzer.common.action.BaseAction;
import cn.wizzer.common.config.Globals;
import cn.wizzer.common.filter.GlobalsFilter;
import cn.wizzer.common.filter.UserLoginFilter;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.sys.bean.Sys_task;
import cn.wizzer.modules.sys.bean.Sys_user;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.*;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import org.quartz.*;

import java.util.*;

/**
 * @author Wizzer
 * @time 2014-02-27 10:01:23
 */
@IocBean
@At("/private/sys/task")
@Filters({@By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class)})
public class TaskAction extends BaseAction {
    @Inject
    protected Dao dao;
    private final static Log log = Logs.get();

    @At("")
    @Ok("vm:template.private.sys.task")
    public void index(HttpSession session, HttpServletRequest req) {

    }

    @At
    @Ok("vm:template.private.sys.taskAdd")
    public void toadd(HttpServletRequest req) {
    }

    @At
    @Ok("raw")
    public boolean add(@Param("..") Sys_task task, HttpSession session) throws SchedulerException, ClassNotFoundException {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        int type = task.getTask_type();
        task.setTask_code(UUID.randomUUID().toString());
        task.setCreate_time(DateUtil.getCurDateTime());
        task.setUser_id(user.getId());
        Sys_task task1 = daoCtl.addT(dao, task);
        if (task != null && task.getIs_enable() == 0) {
            startTask(task1);
        }
        return task != null;
    }

    @At
    @Ok("vm:template.private.sys.taskUpdate")
    public Sys_task toupdate(@Param("task_id") int task_id, HttpServletRequest req) {
        return daoCtl.detailById(dao, Sys_task.class, task_id);//html:obj
    }

    @At
    public boolean update(@Param("..") Sys_task task, HttpSession session) throws SchedulerException, ClassNotFoundException {
        Sys_user user = (Sys_user) session.getAttribute("userSession");
        //结束之前的任务，开始新任务调度
        String oldTaskCode = task.getTask_code();
        if (!Strings.isBlank(oldTaskCode)) {
            log.info("Update End..." + oldTaskCode);
            endTask(oldTaskCode);
        }
        UUID uuid = UUID.randomUUID();
        task.setTask_code(uuid.toString());
        task.setUser_id(user.getId());
        boolean res = daoCtl.update(dao, task);
        if (res && task.getIs_enable() == 0) {
            log.info("Update Satrt...");
            startTask(task);
        }
        return res;
    }

    @At
    public boolean delete(@Param("task_id") int task_id) throws SchedulerException, ClassNotFoundException {
        Sys_task task = daoCtl.detailById(dao, Sys_task.class, task_id);
        boolean res = daoCtl.deleteById(dao, Sys_task.class, task_id);
        if (res) {
            endTask(task.getTask_code());
        }
        return res;
    }

    @At
    public boolean deleteIds(@Param("ids") String ids) throws SchedulerException, ClassNotFoundException {
        String[] id = StringUtils.split(ids, ",");
        List<Sys_task> list = daoCtl.list(dao, Sys_task.class, Cnd.where("task_id", "in", id));
        for (Sys_task task : list) {
            endTask(task.getTask_code());
        }
        return daoCtl.deleteByIds(dao, Sys_task.class, id);
    }

    @At
    @Ok("raw")
    public String list(@Param("page") int curPage, @Param("rows") int pageSize, HttpServletRequest req) {
        Sql sql = Sqls.create("SELECT A.*,B.REALNAME FROM SYS_TASK A,SYS_USER B WHERE A.USER_ID=B.ID ORDER BY A.TASK_ID");
        Sql sql1 = Sqls.create("SELECT COUNT(*) FROM SYS_TASK A,SYS_USER B WHERE A.USER_ID=B.ID");
        return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize, daoCtl.getIntRowValue(dao, sql1));
    }

    /**
     * 开始任务调度
     *
     * @param task 任务
     */
    private void startTask(Sys_task task) {
        try {
            String cronExpression = getCronExpressionFromDB(task);
            if (StringUtils.isNotBlank(cronExpression)) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("param_value", task.getParam_value());
                JobBuilder jobBuilder = JobBuilder.newJob(getClassByTask(task.getJob_class()));
                jobBuilder.setJobData(getJobDataMap(map));
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
                log.info("task start code:" + task.getTask_code());
                String cronExpressionFromDB = getCronExpressionFromDB(task);
                log.info(cronExpressionFromDB);
                triggerBuilder.withSchedule(getCronScheduleBuilder(cronExpressionFromDB));
                //调度任务
                Globals.SCHEDULER.scheduleJob(jobBuilder.build(), triggerBuilder.build());
                if(Globals.SCHEDULER.isShutdown()){
                    Globals.SCHEDULER.start();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    /**
     * 结束任务调度
     *
     * @param taskName
     */
    private void endTask(String taskName) throws SchedulerException {
        if (Globals.SCHEDULER.checkExists(JobKey.jobKey(taskName, Scheduler.DEFAULT_GROUP)))
            Globals.SCHEDULER.deleteJob(JobKey.jobKey(taskName, Scheduler.DEFAULT_GROUP));
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
}