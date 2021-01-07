package com.budwk.app.web.controllers.platform.sys;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.sys.models.Sys_app_conf;
import com.budwk.app.sys.models.Sys_app_list;
import com.budwk.app.sys.models.Sys_app_task;
import com.budwk.app.sys.services.SysAppConfService;
import com.budwk.app.sys.services.SysAppListService;
import com.budwk.app.sys.services.SysAppTaskService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.web.commons.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelCommand;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelProperty;
import org.nutz.boot.starter.logback.exts.loglevel.LoglevelService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.lang.stream.StringInputStream;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2019/2/27.
 */
@IocBean
@At("/platform/sys/app")
public class SysAppController {
    private static final Log log = Logs.get();
    @Inject
    private LoglevelService loglevelService;
    @Inject
    private SysAppListService sysAppListService;
    @Inject
    private SysAppConfService sysAppConfService;
    @Inject
    private SysAppTaskService sysAppTaskService;
    @Inject
    private RedisService redisService;
    @Inject
    private PropertiesProxy conf;

    @At("")
    @Ok("beetl:/platform/sys/app/index.html")
    @RequiresPermissions("sys.operation.app")
    public void index() {
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.operation.app")
    @SuppressWarnings("unchecked")
    public Object data(@Param("hostName") String hostName) {
        try {
            List<NutMap> hostList = new ArrayList<>();
            NutMap map = loglevelService.getData();
            List<LoglevelProperty> dataList = new ArrayList<>();
            //对数据进行整理,获得左侧主机列表及右侧实例数据
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                List<LoglevelProperty> list = (List) entry.getValue();
                for (LoglevelProperty property : list) {
                    NutMap nutMap = NutMap.NEW().addv("hostName", property.getHostName()).addv("hostAddress", property.getHostAddress());
                    if (!hostList.contains(nutMap))
                        hostList.add(nutMap);
                    if (Strings.isBlank(hostName) || (Strings.isNotBlank(hostName) && property.getHostName().equals(hostName))) {
                        dataList.add(property);
                    }
                }
            }
            return Result.success().addData(NutMap.NEW().addv("hostList", hostList).addv("appList", dataList));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.operation.app")
    @SuppressWarnings("unchecked")
    public Object osData(@Param("hostName") String hostName) {
        try {
            List<String> list = new ArrayList<>();
            ScanParams match = new ScanParams().match(RedisConstant.PLATFORM_REDIS_PREFIX+"logback:deploy:" + hostName + ":*");
            ScanResult<String> scan = null;
            do {
                scan = redisService.scan(scan == null ? ScanParams.SCAN_POINTER_START : scan.getStringCursor(), match);
                list.addAll(scan.getResult());//增量式迭代查询,可能还有下个循环,应该是追加
            } while (!scan.isCompleteIteration());
            Collections.sort(list);
            List<NutMap> dataList = new ArrayList<>();
            for (String key : list) {
                dataList.add(Json.fromJson(NutMap.class, redisService.get(key)));
            }
            return Result.success().addData(dataList);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/version")
    @Ok("json")
    @RequiresPermissions("sys.operation.app")
    public Object version(@Param("name") String name) {
        try {
            List<Sys_app_list> appVerList = sysAppListService.query(Cnd.where("disabled", "=", false).and("appName", "=", name).desc("createdAt"), new Pager().setPageNumber(1).setPageSize(10));
            List<Sys_app_conf> confVerList = sysAppConfService.query(Cnd.where("disabled", "=", false).and("confName", "=", name).desc("createdAt"), new Pager().setPageNumber(1).setPageSize(10));
            return Result.success().addData(NutMap.NEW().addv("appVerList", appVerList).addv("confVerList", confVerList));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/jar")
    @Ok("beetl:/platform/sys/app/jar.html")
    @RequiresPermissions("sys.operation.app")
    public void jar() {

    }

    @At("/jar/data")
    @Ok("json:{locked:'password|salt',ignoreNull:false}")
    @RequiresPermissions("sys.operation.app.jar")
    public Object jarData(@Param("appName") String appName, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(appName)) {
                cnd.and("appName", "like", "%" + appName + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysAppListService.listPageLinks(pageNumber, pageSize, cnd, "^(user)$"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/jar/addDo")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.jar")
    @SLog(tag = "添加安装包", msg = "应用名称:${sysAppList.appName}")
    public Object jarAddDo(@Param("..") Sys_app_list sysAppList, HttpServletRequest req) {
        try {
            int num = sysAppListService.count(Cnd.where("appName", "=", Strings.trim(sysAppList.getAppName())).and("appVersion", "=", Strings.trim(sysAppList.getAppVersion())));
            if (num > 0) {
                return Result.error("版本号已存在");
            }
            sysAppList.setAppName(Strings.trim(sysAppList.getAppName()));
            sysAppList.setAppVersion(Strings.trim(sysAppList.getAppVersion()));
            sysAppList.setCreatedBy(ShiroUtil.getPlatformUid());
            sysAppListService.insert(sysAppList);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/jar/search")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.jar")
    public Object jarSearch(@Param("appName") String appName) {
        return Result.NEW().addData(sysAppListService.getAppNameList());
    }

    @At("/jar/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.jar")
    @SLog(tag = "删除Jar包", msg = "ID:${id}")
    public Object jarDelete(String id, HttpServletRequest req) {
        try {
            Sys_app_list appList = sysAppListService.fetch(id);
            String staticPath = conf.get("jetty.staticPath", "/files");
            Files.deleteFile(new File(staticPath + appList.getFilePath()));
            sysAppListService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/jar/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.jar")
    @SLog(tag = "启用Jar包", msg = "ID:${id}")
    public Object jarEnable(String id, HttpServletRequest req) {
        try {
            sysAppListService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/jar/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.jar")
    @SLog(tag = "禁用Jar包", msg = "ID:${id}")
    public Object jarDisable(String id, HttpServletRequest req) {
        try {
            sysAppListService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf")
    @Ok("beetl:/platform/sys/app/conf.html")
    @RequiresPermissions("sys.operation.app")
    public void conf() {

    }


    @At("/conf/data")
    @Ok("json:{locked:'confData|password|salt',ignoreNull:false}")
    @RequiresPermissions("sys.operation.app.conf")
    public Object confData(@Param("confName") String confName, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(confName)) {
                cnd.and("confName", "like", "%" + confName + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysAppConfService.listPageLinks(pageNumber, pageSize, cnd, "^(user)$"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf/addDo")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    @SLog(tag = "添加配置文件", msg = "应用名称:${sysAppConf.confName}")
    public Object confAddDo(@Param("..") Sys_app_conf sysAppConf, HttpServletRequest req) {
        try {
            int num = sysAppConfService.count(Cnd.where("confName", "=", Strings.trim(sysAppConf.getConfName())).and("confVersion", "=", Strings.trim(sysAppConf.getConfVersion())));
            if (num > 0) {
                return Result.error("版本号已存在");
            }
            sysAppConf.setConfName(Strings.trim(sysAppConf.getConfName()));
            sysAppConf.setConfVersion(Strings.trim(sysAppConf.getConfVersion()));
            sysAppConf.setCreatedBy(ShiroUtil.getPlatformUid());
            sysAppConfService.insert(sysAppConf);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf/search")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    public Object confSearch(@Param("confName") String confName) {
        return Result.NEW().addData(sysAppConfService.getConfNameList());
    }

    @At("/conf/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    @SLog(tag = "删除配置文件", msg = "ID:${id}")
    public Object confDelete(String id, HttpServletRequest req) {
        try {
            sysAppConfService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf/enable/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    @SLog(tag = "启用配置文件", msg = "ID:${id}")
    public Object confEnable(String id, HttpServletRequest req) {
        try {
            sysAppConfService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf/disable/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    @SLog(tag = "禁用配置文件", msg = "ID:${id}")
    public Object confDisable(String id, HttpServletRequest req) {
        try {
            sysAppConfService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/conf/download/?")
    @Ok("void")
    @RequiresPermissions("sys.operation.app.conf")
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

    @At("/conf/edit/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    public Object confEdit(@Param("id") String id) {
        return Result.NEW().addData(sysAppConfService.fetch(id));
    }

    @At("/conf/editDo")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.conf")
    @SLog(tag = "修改配置文件", msg = "应用名称:${sysAppConf.confName}")
    public Object confEditDo(@Param("..") Sys_app_conf sysAppConf, HttpServletRequest req) {
        try {
            sysAppConf.setUpdatedBy(ShiroUtil.getPlatformUid());
            sysAppConfService.updateIgnoreNull(sysAppConf);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/task/addDo")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.instance")
    @SLog(tag = "创建任务", msg = "应用名称:${appTask.getName()} 动作:${appTask.getAction()}")
    public Object taskAddDo(@Param("..") Sys_app_task appTask, HttpServletRequest req) {
        try {
            Cnd cnd = Cnd.where("name", "=", appTask.getName()).and("action", "=", "stop")
                    .and("appVersion", "=", appTask.getAppVersion())
                    .and("confVersion", "=", appTask.getConfVersion())
                    .and("hostName", "=", appTask.getHostName())
                    .and("hostAddress", "=", appTask.getHostAddress())
                    .and(Cnd.exps("status", "=", 0).or("status", "=", 1));
            if ("stop".equals(appTask.getAction())) {
                cnd.and("processId", "=", appTask.getProcessId());
            }
            int num = sysAppTaskService.count(cnd);
            if (num > 0) {
                return Result.error("任务已存在，请耐心等待执行结果");
            }
            appTask.setUpdatedBy(ShiroUtil.getPlatformUid());
            appTask.setStatus(0);
            sysAppTaskService.insert(appTask);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/task/data")
    @Ok("json:{locked:'confData',ignoreNull:false}")
    @RequiresPermissions("sys.operation.app")
    public Object taskData(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysAppTaskService.listPageLinks(pageNumber, pageSize, cnd, "^(user)$"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/task/cannel/?")
    @Ok("json")
    @RequiresPermissions("sys.operation.app.instance")
    @SLog(tag = "取消任务", msg = "任务ID:${id}")
    public Object taskAddDo(String id, HttpServletRequest req) {
        try {
            //加上status条件,防止执行前状态已变更
            sysAppTaskService.update(Chain.make("status", 4), Cnd.where("id", "=", id).and("status", "=", 0));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.operation.app.loglevel")
    public Object loglevel(@Param("action") String action, @Param("name") String name, @Param("processId") String processId, @Param("loglevel") String loglevel) {
        try {
            LoglevelCommand loglevelCommand = new LoglevelCommand();
            loglevelCommand.setAction(action);
            loglevelCommand.setLevel(loglevel);
            if ("processId".equals(action)) {
                loglevelCommand.setProcessId(processId);
            }
            loglevelCommand.setName(name);
            loglevelService.changeLoglevel(loglevelCommand);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
