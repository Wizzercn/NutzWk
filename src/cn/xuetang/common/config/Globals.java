package cn.xuetang.common.config;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.modules.sys.bean.Sys_config;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.filepool.FilePool;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.quartz.Scheduler;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 */
public class Globals extends BaseAction {
    private final static Log log = Logs.get();
    //虚拟目录路径
    public static String APP_BASE_PATH = "";
    //虚拟目录名称
    public static String APP_BASE_NAME = "";
    //应用中文名
    public static String APP_NAME = "";
    //系统配置
    public static Map<String, String> SYS_CONFIG;
    //数据字典，根据ID分别初始化
    public static Map<String, Object> DATA_DICT;
    //定时任务实例
    public static Scheduler SCHEDULER;
    //文件池
    public static FilePool FILE_POOL;

    public static void InitSysConfig(Dao dao) {
        try {
            if (SYS_CONFIG == null) {
                SYS_CONFIG = new HashMap<String, String>();
            }
            List<Sys_config> configList = dao.query(Sys_config.class, Cnd.orderBy().asc("ID"));
            for (Sys_config sysConfig : configList) {
                Globals.SYS_CONFIG.put(sysConfig.getCname(), sysConfig.getCvalue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    public static void InitDataDict(Dao dao) {
        try {
            if (DATA_DICT == null) {
                DATA_DICT = new HashMap<String, Object>();
            }
            DATA_DICT.put(Dict.APP_TYPE, daoCtl.getHashMap(dao, Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.APP_TYPE + "____'")));
            DATA_DICT.put(Dict.DIVSION, daoCtl.getHashMap(dao, Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));
            DATA_DICT.put(Dict.FORM_TYPE, daoCtl.getHashMap(dao, Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.FORM_TYPE + "_%' order by location asc,id asc")));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }
}
