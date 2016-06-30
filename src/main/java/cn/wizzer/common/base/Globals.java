package cn.wizzer.common.base;

import cn.wizzer.modules.gm.sys.models.Sys_config;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/22.
 */
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
    public static String AppDomain = "127.0.0.1";
    //文件上传路径
    public static String AppUploadPath = "/upload";
    //系统自定义参数
    public static Map<String, String> MyConfig;

    public static void init(Dao dao) {
        Globals.MyConfig = new HashMap<>();
        List<Sys_config> configList = dao.query(Sys_config.class, Cnd.NEW());
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
                default:
                    Globals.MyConfig.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
                    break;
            }
        }
    }
}
