package cn.wizzer.common.service.log;

import cn.wizzer.common.util.DateUtils;
import cn.wizzer.common.util.StringUtils;
import cn.wizzer.modules.sys.bean.Sys_log;
import org.apache.commons.lang.math.NumberUtils;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@IocBean(create="init", depose="close")
public class SysLogService implements Runnable {

    private static final Log log = Logs.get();

    ExecutorService es;

    LinkedBlockingQueue<Sys_log> queue;

    @Inject
    protected Dao dao;

    public void async(Sys_log syslog) {
        LinkedBlockingQueue<Sys_log> queue = this.queue;
        if (queue != null)
            try {
                boolean re = queue.offer(syslog, 50, TimeUnit.MILLISECONDS);
                if (!re) {
                    log.info("syslog queue is full, drop it ...");
                }
            } catch (InterruptedException e) {
            }
    }

    public static final SimpleDateFormat ym = new SimpleDateFormat("yyyy");

    public void sync(Sys_log syslog) {
        try {
            log.debug(syslog.getCreateTime());
            // TODO syslog需要改成更高效的方式
            Dao dao = Daos.ext(this.dao, ((DateFormat)ym.clone()).format(syslog.getCreateTime()));
            dao.fastInsert(syslog);
        } catch (Throwable e) {
            log.info("insert syslog sync fail", e);
        }
    }

    public void run() {
        while (true) {
            LinkedBlockingQueue<Sys_log> queue = this.queue;
            if (queue == null)
                break;
            try {
                Sys_log sysLog = queue.poll(1, TimeUnit.SECONDS);
                if (sysLog != null) {
                    sync(sysLog);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void init() {
        queue = new LinkedBlockingQueue<Sys_log>();
        int c = Runtime.getRuntime().availableProcessors();
        es = Executors.newFixedThreadPool(c);
        for (int i = 0; i < c; i++) {
            es.submit(this);
        }
        int y= NumberUtils.toInt(DateUtils.getYear());
        for (int i = y; i < y+3; i++) {
            Dao dao = Daos.ext(this.dao, String.valueOf(i));
            dao.create(Sys_log.class,false);
        }
    }

    public void close() throws InterruptedException {
        queue = null; // 触发关闭
        if (es != null && !es.isShutdown()) {
            es.shutdown();
            es.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
