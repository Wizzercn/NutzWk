package cn.wizzer.app.task.modules.services;

/**
 * Created by wizzer on 2018/3/19.
 */
public interface TaskPlatformService {
    /**
     * 判断任务是否存在
     * @param jobName
     * @param jobGroup
     * @return
     */
    boolean isExist(String jobName,String jobGroup);

    /**
     * 添加新任务
     * @param jobName
     * @param jobGroup
     * @param className
     * @param cron
     * @param comment
     * @param dataMap
     */
    void add(String jobName,String jobGroup,String className,String cron,String comment,String dataMap);

    /**
     * 删除任务
     * @param jobName
     * @param jobGroup
     * @return
     */
    boolean delete(String jobName,String jobGroup);

    /**
     * 清除所有任务
     */
    void clear();
}
