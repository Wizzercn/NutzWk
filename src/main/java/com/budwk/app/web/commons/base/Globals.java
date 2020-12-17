package com.budwk.app.web.commons.base;

import com.budwk.app.sys.models.Sys_config;
import com.budwk.app.sys.models.Sys_route;
import com.budwk.app.sys.models.Sys_task;
import com.budwk.app.sys.services.SysConfigService;
import com.budwk.app.sys.services.SysRouteService;
import com.budwk.app.sys.services.SysTaskService;
import com.budwk.app.task.services.TaskPlatformService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * Created by wizzer on 2016/12/19.
 */
@IocBean(create = "init")
public class Globals {
    //项目路径
    public static String AppRoot = "";
    //项目目录
    public static String AppBase = "";
    //项目名称
    public static String AppName = "BudCMS内容管理系统";
    //项目短名称
    public static String AppShrotName = "BUDCMS";
    //项目域名
    public static String AppDomain = "http://127.0.0.1";
    //文件访问域名
    public static String AppFileDomain = "";
    //文件上传路径
    public static String AppUploadBase = "/upload";
    //系统自定义参数
    public static NutMap MyConfig = NutMap.NEW();
    //自定义路由
    public static NutMap RouteMap = NutMap.NEW();
    //微信map
    public static NutMap WxMap = NutMap.NEW();
    @Inject
    private SysConfigService sysConfigService;
    @Inject
    private SysRouteService sysRouteService;
    @Inject
    private TaskPlatformService taskPlatformService;
    @Inject
    private SysTaskService sysTaskService;

    public void init() {
        initSysConfig(sysConfigService);
        initRoute(sysRouteService);
        initTask(sysTaskService);
    }

    public void initTask(SysTaskService sysTaskService) {
        taskPlatformService.clear();
        List<Sys_task> taskList = sysTaskService.query();
        for (Sys_task sysTask : taskList) {
            try {
                if (!sysTask.isDisabled())//不存在则新建
                    taskPlatformService.add(sysTask.getId(), sysTask.getId(), sysTask.getJobClass(), sysTask.getCron()
                            , sysTask.getNote(), sysTask.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void initSysConfig(SysConfigService sysConfigService) {
        Globals.MyConfig.clear();
        List<Sys_config> configList = sysConfigService.query();
        for (Sys_config sysConfig : configList) {
            switch (Strings.sNull(sysConfig.getConfigKey())) {
                case "AppName":
                    Globals.AppName = sysConfig.getConfigValue();
                    break;
                case "AppShrotName":
                    Globals.AppShrotName = sysConfig.getConfigValue();
                    break;
                case "AppDomain":
                    Globals.AppDomain = sysConfig.getConfigValue();
                    break;
                case "AppFileDomain":
                    Globals.AppFileDomain = sysConfig.getConfigValue();
                    break;
                case "AppUploadBase":
                    Globals.AppUploadBase = sysConfig.getConfigValue();
                    break;
                default:
                    Globals.MyConfig.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
                    break;
            }
        }
    }

    public static void initRoute(SysRouteService sysRouteService) {
        Globals.RouteMap.clear();
        List<Sys_route> routeList = sysRouteService.query(Cnd.where("disabled", "=", false));
        for (Sys_route route : routeList) {
            Globals.RouteMap.put(route.getUrl(), route);
        }
    }

    public static void initWx() {
        Globals.WxMap.clear();
    }
}
