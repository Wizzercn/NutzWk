package com.budwk.app.web.controllers.open.deploy;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.result.Result;
import com.budwk.app.sys.models.Sys_app_conf;
import com.budwk.app.sys.models.Sys_app_list;
import com.budwk.app.sys.models.Sys_app_task;
import com.budwk.app.sys.services.SysAppConfService;
import com.budwk.app.sys.services.SysAppListService;
import com.budwk.app.sys.services.SysAppTaskService;
import com.budwk.app.web.commons.filter.DeployFilter;
import org.nutz.boot.starter.ftp.FtpService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用管理服务端接口
 *
 * @author wizzer(wizzer @ qq.com) on 2019/3/8.
 */
@IocBean(create = "init")
@At("/open/api/deploy")
@Filters({@By(type = DeployFilter.class)})
public class ApiDeployController {
    private final static Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;
    @Inject
    private SysAppTaskService sysAppTaskService;
    @Inject
    private SysAppListService sysAppListService;
    @Inject
    private SysAppConfService sysAppConfService;
    @Inject
    private FtpService ftpService;
    @Inject
    private RedisService redisService;
    private boolean ftpEnabled;
    private String filePath;

    public void init() {
        filePath = conf.get("upload.path", conf.get("jetty.staticPath"));
        ftpEnabled = conf.getBoolean("ftp.enabled", false);
    }

    @At("/task")
    @Ok("json")
    @POST
    public Object task(@Param("apps") String[] apps, @Param("hostname") String hostname, @Param("timestamp") long timestamp,
                       @Param("mem_total") long memTotal, @Param("mem_used") long memUsed,
                       @Param("mem_free") long memFree, @Param("mem_percent") double memPercent,
                       @Param("cpu_percent") double cpuPercent, @Param("net_sent") long netSent, @Param("net_recv") long netRecv,
                       @Param("net_tcp") long netTcp, @Param("hdd_total") long hddTotal, @Param("hdd_used") long hddUsed,
                       @Param("hdd_free") long hddFree, @Param("hdd_percent") double hddPercent) {
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
                    Cnd.where("name", "in", apps).and("hostName", "=", hostname).and("status", "=", 1).and("createdAt", "<", now3));
            NutMap map = NutMap.NEW().addv("mem_total", memTotal)
                    .addv("mem_used", memUsed)
                    .addv("mem_free", memFree)
                    .addv("mem_percent", memPercent)
                    .addv("cpu_percent", cpuPercent)
                    .addv("net_sent", netSent)
                    .addv("net_recv", netRecv)
                    .addv("net_tcp", netTcp)
                    .addv("hdd_total", hddTotal)
                    .addv("hdd_used", hddUsed)
                    .addv("hdd_free", hddFree)
                    .addv("hdd_percent", hddPercent)
                    .addv("timestamp", timestamp);
            redisService.setex(RedisConstant.PLATFORM_REDIS_PREFIX + "logback:deploy:" + hostname + ":" + timestamp, 10 * 60, Json.toJson(map, JsonFormat.compact()));
            return Result.success("获取成功").addData(list);
        } catch (Exception e) {
            log.error(e);
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
                if (ftpEnabled) {
                    ftpService.download(sysAppList.getFilePath(), response.getOutputStream());
                } else {
                    FileInputStream fileInputStream = new FileInputStream(new File(filePath + "/" + sysAppList.getFilePath()));
                    Streams.writeAndClose(response.getOutputStream(), fileInputStream);
                }
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
