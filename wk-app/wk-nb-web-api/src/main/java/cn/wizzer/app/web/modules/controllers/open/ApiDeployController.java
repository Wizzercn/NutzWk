package cn.wizzer.app.web.modules.controllers.open;

import cn.wizzer.app.sys.modules.models.Sys_app_conf;
import cn.wizzer.app.sys.modules.models.Sys_app_list;
import cn.wizzer.app.sys.modules.models.Sys_app_task;
import cn.wizzer.app.sys.modules.services.SysAppConfService;
import cn.wizzer.app.sys.modules.services.SysAppListService;
import cn.wizzer.app.sys.modules.services.SysAppTaskService;
import cn.wizzer.app.web.commons.filter.ApiDeploySignFilter;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.boot.starter.ftp.FtpService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Streams;
import org.nutz.lang.Times;
import org.nutz.lang.stream.StringInputStream;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用管理服务端接口
 * Created by wizzer on 2019/3/8.
 */
@IocBean
@At("/open/api/deploy")
@Filters({@By(type = ApiDeploySignFilter.class)})
public class ApiDeployController {
    private final static Log log = Logs.get();
    @Inject
    @Reference
    private SysAppTaskService sysAppTaskService;
    @Inject
    @Reference
    private SysAppListService sysAppListService;
    @Inject
    @Reference
    private SysAppConfService sysAppConfService;
    @Inject
    private FtpService ftpService;
    @Inject
    private RedisService redisService;

    @At("/task")
    @Ok("json")
    @POST
    public Object task(@Param("apps") String[] apps, @Param("hostname") String hostname, @Param("timestamp") long timestamp,
                       @Param("mem_total") long mem_total, @Param("mem_used") long mem_used,
                       @Param("mem_free") long mem_free, @Param("mem_percent") double mem_percent,
                       @Param("cpu_percent") double cpu_percent, @Param("net_sent") long net_sent, @Param("net_recv") long net_recv,
                       @Param("net_tcp") long net_tcp, @Param("hdd_total") long hdd_total, @Param("hdd_used") long hdd_used,
                       @Param("hdd_free") long hdd_free, @Param("hdd_percent") double hdd_percent) {
        try {
            List<Sys_app_task> list = sysAppTaskService.query(Cnd.where("name", "in", apps).and("hostName", "=", hostname).and("status", "=", 0));
            List<String> ids = new ArrayList<>();
            for (Sys_app_task task : list) {
                ids.add(task.getId());
            }
            sysAppTaskService.update(Chain.make("status", 1), Cnd.where("id", "in", ids));
            //大于3分钟将任务设置为超时
            long now3 = Times.getTS() - 3 * 60;
            sysAppTaskService.update(Chain.make("status", 3).add("pushAt", Times.getTS()).add("pushResult", "任务超时"),
                    Cnd.where("name", "in", apps).and("hostName", "=", hostname).and("status", "=", 1).and("opAt", "<", now3));
            NutMap map = NutMap.NEW().addv("mem_total", mem_total)
                    .addv("mem_used", mem_used)
                    .addv("mem_free", mem_free)
                    .addv("mem_percent", mem_percent)
                    .addv("cpu_percent", cpu_percent)
                    .addv("net_sent", net_sent)
                    .addv("net_recv", net_recv)
                    .addv("net_tcp", net_tcp)
                    .addv("hdd_total", hdd_total)
                    .addv("hdd_used", hdd_used)
                    .addv("hdd_free", hdd_free)
                    .addv("hdd_percent", hdd_percent)
                    .addv("timestamp", timestamp);
            redisService.setex("logback:deploy:" + hostname + ":" + timestamp, 10 * 60, Json.toJson(map, JsonFormat.compact()));
            return Result.success("获取成功").addData(list);
        } catch (Exception e) {
            return Result.error("获取失败");
        }
    }

    @At("/report")
    @Ok("json")
    @POST
    public Object report(@Param("hostname") String hostname, @Param("taskid") String taskid, @Param("status") int status, @Param("msg") String msg) {
        try {
            sysAppTaskService.update(Chain.make("status", status).add("pushResult", msg).add("pushAt", Times.getTS()), Cnd.where("id", "=", taskid));
            return Result.success("执行成功");
        } catch (Exception e) {
            return Result.error("执行失败");
        }
    }

    @At("/jar/download")
    @Ok("void")
    public void jarDownload(@Param("name") String name, @Param("version") String version, HttpServletResponse response) {
        try {
            Sys_app_list sysAppList = sysAppListService.fetch(Cnd.where("appName", "=", name).and("appVersion", "=", version).and("disabled", "=", false));
            if (sysAppList != null) {
                String fileName = sysAppList.getAppName() + ".jar";
                response.setHeader("Content-Type", "application/java-archive");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentLengthLong(sysAppList.getFileSize());
                ftpService.download(sysAppList.getFilePath(), response.getOutputStream());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @At("/conf/download")
    @Ok("void")
    public void confDownload(@Param("name") String name, @Param("version") String version, HttpServletResponse response) {
        try {
            Sys_app_conf conf = sysAppConfService.fetch(Cnd.where("confName", "=", name).and("confVersion", "=", version).and("disabled", "=", false));
            if (conf != null) {
                String fileName = conf.getConfName() + "-" + conf.getConfVersion() + ".properties";
                response.setHeader("Content-Type", "text/plain");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                try (InputStream in = new StringInputStream(conf.getConfData())) {
                    Streams.writeAndClose(response.getOutputStream(), in);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
