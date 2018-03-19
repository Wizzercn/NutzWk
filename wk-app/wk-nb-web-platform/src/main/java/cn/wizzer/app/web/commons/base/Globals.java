package cn.wizzer.app.web.commons.base;

import cn.wizzer.app.sys.modules.models.Sys_config;
import cn.wizzer.app.sys.modules.models.Sys_route;
import cn.wizzer.app.sys.modules.services.SysConfigService;
import cn.wizzer.app.sys.modules.services.SysRouteService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static String AppName = "NutzWk 开发框架";
    //项目短名称
    public static String AppShrotName = "NutzWk";
    //项目域名
    public static String AppDomain = "http://127.0.0.1";
    //文件上传路径
    public static String AppUploadPath = "D://upload";
    //文件上传路径
    public static String AppUploadBase = "/upload";
    // 是否启用了队列
    public static boolean RabbitMQEnabled = false;
    //系统自定义参数
    public static Map<String, String> MyConfig = new HashMap<>();
    //自定义路由
    public static Map<String, Sys_route> RouteMap = new HashMap<>();
    //微信map
    @Inject
    @Reference
    private SysConfigService sysConfigService;
    @Inject
    @Reference
    private SysRouteService sysRouteService;

    public void init() {
        initSysConfig(sysConfigService);
        initRoute(sysRouteService);
    }

    public static void initSysConfig(SysConfigService sysConfigService) {
        Globals.MyConfig.clear();
        List<Sys_config> configList = sysConfigService.query();
        for (Sys_config sysConfig : configList) {
            switch (sysConfig.getConfigKey()) {
                case "AppName":
                    Globals.AppName = sysConfig.getConfigValue();
                    break;
                case "AppShrotName":
                    Globals.AppShrotName = sysConfig.getConfigValue();
                    break;
                case "AppDomain":
                    Globals.AppDomain = sysConfig.getConfigValue();
                    break;
                case "AppUploadPath":
                    Globals.AppUploadPath = sysConfig.getConfigValue();
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
}
