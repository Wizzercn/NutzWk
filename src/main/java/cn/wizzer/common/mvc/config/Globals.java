package cn.wizzer.common.mvc.config;

import cn.wizzer.modules.sys.bean.Sys_config;
import cn.wizzer.modules.sys.service.ConfigService;
import cn.wizzer.modules.sys.service.DictService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wizzer.cn on 2015/7/3.
 */
@IocBean
public class Globals {
    private final static Log log = Logs.get();

    static DictService dictService= Mvcs.ctx().getDefaultIoc().get(DictService.class);
    static ConfigService configService= Mvcs.ctx().getDefaultIoc().get(ConfigService.class);
    //虚拟目录路径
    public static String APP_BASE_PATH = "";
    //虚拟目录名称
    public static String APP_BASE_NAME = "";
    //应用中文名
    public static String APP_NAME = "";
    public static Map<String,String> SYS_CONFIG;
    public static Map<String,Object> DATA_DICT;

    public static void InitSysConfig() {
        try {
            if (SYS_CONFIG == null) {
                SYS_CONFIG = new HashMap<String, String>();
            }
            List<Sys_config> configList =configService.query(Cnd.orderBy().asc("location"),null);
            for (Sys_config sysConfig : configList) {
                Globals.SYS_CONFIG.put(sysConfig.getCname(), sysConfig.getCvalue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void InitDataDict() {
        try {
            if (DATA_DICT == null) {
                DATA_DICT = new HashMap<String, Object>();
            }
            DATA_DICT.put(Dict.DIVSION, dictService.list(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
