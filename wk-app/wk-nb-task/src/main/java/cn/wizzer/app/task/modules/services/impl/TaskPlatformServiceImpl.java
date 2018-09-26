package cn.wizzer.app.task.modules.services.impl;

import cn.wizzer.app.task.modules.services.TaskPlatformService;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.JobKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wizzer on 2018/3/19.
 */
@IocBean
@Service(interfaceClass = TaskPlatformService.class)
public class TaskPlatformServiceImpl implements TaskPlatformService {
    @Inject
    private QuartzManager quartzManager;

    /**
     * 判断任务是否存在
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    public boolean isExist(String jobName, String jobGroup) {
        return quartzManager.exist(new JobKey(jobName, jobGroup));
    }

    /**
     * 添加新任务
     *
     * @param jobName
     * @param jobGroup
     * @param className
     * @param cron
     * @param comment
     * @param dataMap
     */
    public void add(String jobName, String jobGroup, String className, String cron, String comment, String dataMap) {
        QuartzJob qj = new QuartzJob();
        qj.setJobName(jobName);
        qj.setJobGroup(jobGroup);
        qj.setClassName(className);
        qj.setCron(cron);
        qj.setComment(comment);
        qj.setDataMap(dataMap);
        quartzManager.add(qj);
    }

    /**
     * 删除任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    public boolean delete(String jobName, String jobGroup) {
        QuartzJob qj = new QuartzJob();
        qj.setJobName(jobName);
        qj.setJobGroup(jobGroup);
        return quartzManager.delete(qj);
    }

    /**
     * 清除任务
     */
    public void clear() {
        quartzManager.clear();
    }

    /**
     * 获取cron表达式最近执行时间
     *
     * @param cronExpression
     * @return
     */
    public List<String> getCronExeTimes(String cronExpression) throws Exception {
        List<String> list = new ArrayList<>();
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(cronExpression);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(2, 1);
        List<Date> dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, now, calendar.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < dates.size() && i <= 4; ++i) {
            list.add(dateFormat.format((Date) dates.get(i)));
        }
        return list;
    }
}
