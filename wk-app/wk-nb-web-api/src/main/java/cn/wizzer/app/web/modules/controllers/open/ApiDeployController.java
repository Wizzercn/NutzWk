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
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Streams;
import org.nutz.lang.stream.StringInputStream;
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

    @At("/task")
    @Ok("json")
    @POST
    public Object task(@Param("hosts") String[] hosts, @Param("hostname") String hostname) {
        try {
            List<Sys_app_task> list = sysAppTaskService.query(Cnd.where("name", "in", hosts).and("hostName", "=", hostname).and("status", "=", 0));
            List<String> ids = new ArrayList<>();
            for (Sys_app_task task : list) {
                ids.add(task.getId());
            }
            //sysAppTaskService.update(Chain.make("status", 1), Cnd.where("id", "in", ids));
            return Result.success("获取成功").addData(list);
        } catch (Exception e) {
            return Result.error("获取失败");
        }
    }

    @At("/jar/download/?")
    @Ok("void")
    public void jarDownload(String id, HttpServletResponse response) {
        try {
            Sys_app_list sysAppList = sysAppListService.fetch(id);
            String fileName = sysAppList.getAppName() + ".jar";
            response.setHeader("Content-Type", "application/java-archive");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLengthLong(sysAppList.getFileSize());
            ftpService.download(sysAppList.getFilePath(), response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @At("/conf/download/?")
    @Ok("void")
    public void confDownload(String id, HttpServletResponse response) {
        try {
            Sys_app_conf conf = sysAppConfService.fetch(id);
            String fileName = conf.getConfName() + "-" + conf.getConfVersion() + ".properties";
            response.setHeader("Content-Type", "text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            try (InputStream in = new StringInputStream(conf.getConfData())) {
                Streams.writeAndClose(response.getOutputStream(), in);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
