package cn.wizzer.common.mvc.config;

import cn.wizzer.common.util.CacheUtils;
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
public class Globals {
    //虚拟目录路径
    public static String APP_BASE_PATH = "";
    //虚拟目录名称
    public static String APP_BASE_NAME = "";
    //应用中文名
    public static String APP_NAME = "";
    //系统配置 缓存名称
    public static String SYS_CONFIG_KEY="sys_config";
    //数据字典 缓存名称
    public static String SYS_DICT_KEY="sys_dict";
}
