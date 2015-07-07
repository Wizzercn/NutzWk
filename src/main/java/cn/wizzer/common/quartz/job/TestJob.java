package cn.wizzer.common.quartz.job;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
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
    private int i;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Test Job ::"+(i++));
    }
}
