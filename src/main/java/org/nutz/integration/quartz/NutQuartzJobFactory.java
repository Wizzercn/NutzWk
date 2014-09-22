package org.nutz.integration.quartz;

import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * 用NutIoc接管Quartz的JobFactory,实现用户需要的注入功能
 * <p/>
 * <p/>
 * 当ioc不存在或从ioc获取失败的时候,将降级为默认的SimpleJobFactory进行处理
 * <p/>
 * 使用方法: 在quartz的配置文件中加入
 * org.quartz.scheduler.jobFactory.class=org.nutz.integration.quartz.NutQuartzJobFactory
 * <p/> 当然,你可以用一行语句解决: return Mvcs.ctx.getDefaultIoc().get(bundle.getJobDetail().getJobClass());
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class NutQuartzJobFactory implements JobFactory {

	private static final Log log = Logs.get();

	protected SimpleJobFactory simple = new SimpleJobFactory();

	public Ioc ioc;

	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		if (ioc == null)
			try {
				ioc = Mvcs.ctx.getDefaultIoc();
			}
			catch (Exception e) {
				log.warn("Without Ioc!! fallback to SimpleJobFactory", e);
				return simple.newJob(bundle, scheduler);
			}
		
		try {
			return ioc.get(bundle.getJobDetail().getJobClass());
		}
		catch (Exception e) {
			log.warn("Not ioc bean? fallback to SimpleJobFactory", e);
			return simple.newJob(bundle, scheduler);
		}
	}

}