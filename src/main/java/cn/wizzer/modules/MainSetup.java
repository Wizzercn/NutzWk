package cn.wizzer.modules;

import cn.wizzer.common.mvc.config.Dict;
import cn.wizzer.common.util.CacheUtils;
import cn.wizzer.modules.sys.bean.*;
import cn.wizzer.common.mvc.config.Globals;
import cn.wizzer.modules.sys.service.ConfigService;
import cn.wizzer.modules.sys.service.DictService;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.velocity.app.Velocity;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.nutz.integration.quartz.NutQuartzCronJobFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
public class MainSetup implements Setup {
    private static final Log log = Logs.get();
    static DictService dictService= Mvcs.ctx().getDefaultIoc().get(DictService.class);
    static ConfigService configService= Mvcs.ctx().getDefaultIoc().get(ConfigService.class);
    public void init(NutConfig config) {
        try {
            Ioc ioc = config.getIoc();
            Dao dao = ioc.get(Dao.class);
            //初始化数据表
            initSysData(config, dao);
            //初始化Velocity
            velocityInit(config);
            // 获取NutQuartzCronJobFactory从而触发计划任务的初始化与启动
            ioc.get(NutQuartzCronJobFactory.class);
            // 检查一下Ehcache CacheManager 是否正常.
            CacheManager cacheManager = ioc.get(CacheManager.class);
            log.debug("Ehcache CacheManager = " + cacheManager);
            //初始化系统变量
            initSysSetting(config, dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化系统变量
     * @param config
     * @param dao
     */
    private void initSysSetting(NutConfig config, Dao dao) {
        Map<String,String> map=new HashMap<>();
        List<Sys_config> configList =dao.query(Sys_config.class,Cnd.orderBy().asc("location"));
        for (Sys_config sysConfig : configList) {
            map.put(sysConfig.getCname(), sysConfig.getCvalue());
        }
        Map<String,Object> dictMap=new HashMap<>();
        dictMap.put(Dict.DIVSION, dictService.list(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));
        CacheUtils.put(Globals.SYS_CONFIG_KEY,map);
        CacheUtils.put(Globals.SYS_DICT_KEY, dictMap);
        Globals.APP_BASE_PATH = Strings.sNull(config.getAppRoot());//项目路径
        Globals.APP_BASE_NAME = Strings.sNull(config.getServletContext().getContextPath());//部署名
        Globals.APP_NAME = Strings.sNull(map.get("app_name"));//项目名称
    }

    /**
     * 初始化数据库
     * @param config
     * @param dao
     */
    private void initSysData(NutConfig config, Dao dao) {
        Daos.createTablesInPackage(dao, "cn.wizzer.modules", true);
        // 若必要的数据表不存在，则初始化数据库
        if (0==dao.count(Sys_user.class)) {
            //初始化数据字典表
            FileSqlManager fm = new FileSqlManager("init_sys_dict.sql");
            List<Sql> sqlList = fm.createCombo(fm.keys());
            dao.execute(sqlList.toArray(new Sql[sqlList.size()]));
            //初始化配置表
            Sys_config conf = new Sys_config();
            conf.setCname("app_name");
            conf.setCvalue("NutzWk 2.0");
            conf.setNote("系统名称");
            dao.insert(conf);
            Sys_config conf2 = new Sys_config();
            conf2.setCname("app_copyright");
            conf2.setCvalue("<a href=\"http://www.wizzer.cn\" target=\"_blank\">Wizzer.cn</a>");
            conf2.setNote("版权信息");
            dao.insert(conf2);
            //初始化单位
            Sys_unit unit = new Sys_unit();
            unit.setId("0001");
            unit.setName("系统管理");
            unit.setLocation(0);
            dao.insert(unit);
            //初始化菜单
            List<Sys_menu> menuList=new ArrayList<Sys_menu>();
            Sys_menu menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("0001");
            menu.setName("系统管理");
            menu.setAliasName("System");
            menu.setIcon("ti-settings");
            menu.setLocation(0);
            menu.setHref("");
            menu.setIs_show(true);
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010001");
            menu.setName("组织结构");
            menu.setAliasName("Units");
            menu.setLocation(0);
            menu.setHref("/private/sys/unit");
            menu.setIs_show(true);
            menu.setPermission("sys:unit");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010002");
            menu.setName("用户管理");
            menu.setAliasName("Users");
            menu.setLocation(0);
            menu.setHref("/private/sys/user");
            menu.setIs_show(true);
            menu.setPermission("sys:user");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("000100020001");
            menu.setName("添加用户");
            menu.setAliasName("Add");
            menu.setLocation(0);
            menu.setIs_show(false);
            menu.setPermission("sys:user:add");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("000100020002");
            menu.setName("修改用户");
            menu.setAliasName("Update");
            menu.setLocation(0);
            menu.setIs_show(false);
            menu.setPermission("sys:user:update");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("000100020003");
            menu.setName("删除用户");
            menu.setAliasName("Delete");
            menu.setLocation(0);
            menu.setIs_show(false);
            menu.setPermission("sys:user:delete");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("000100020004");
            menu.setName("更新资料");
            menu.setAliasName("Profile");
            menu.setLocation(0);
            menu.setIs_show(false);
            menu.setPermission("sys:user:profile");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010003");
            menu.setName("角色管理");
            menu.setAliasName("Roles");
            menu.setLocation(0);
            menu.setHref("/private/sys/role");
            menu.setIs_show(true);
            menu.setPermission("sys:role");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010004");
            menu.setName("菜单管理");
            menu.setAliasName("Menus");
            menu.setLocation(0);
            menu.setHref("/private/sys/menu");
            menu.setIs_show(true);
            menu.setPermission("sys:menu");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010005");
            menu.setName("参数配置");
            menu.setAliasName("Params");
            menu.setLocation(0);
            menu.setHref("/private/sys/config");
            menu.setIs_show(true);
            menu.setPermission("sys:config");
            menuList.add(menu);
            menu=new Sys_menu();
            menu.setIs_enabled(true);
            menu.setId("00010006");
            menu.setName("数据字典");
            menu.setAliasName("Dicts");
            menu.setLocation(0);
            menu.setHref("/private/sys/dict");
            menu.setIs_show(true);
            menu.setPermission("sys:dict");
            menuList.add(menu);
            //初始化角色
            Sys_role role=new Sys_role();
            role.setName("公共角色");
            role.setAlias("public");
            role.setDescription("All user's role.");
            role.setLocation(0);
            role.setUnitid("");
            dao.insert(role);
            role=new Sys_role();
            role.setName("超级管理员");
            role.setAlias("superadmin");
            role.setDescription("Super Admin");
            role.setLocation(1);
            role.setUnitid("");
            role.setMenus(menuList);
            dao.insertWith(role,"menus");
            //初始化用户
            Sys_user user = new Sys_user();
            user.setUsername("superadmin");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("1", salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            Sys_user_profile profile = new Sys_user_profile();
            profile.setUnitid(unit.getId());
            profile.setNickname("超级管理员");
            profile.setEmail("wizzer@qq.com");
            profile.setLinkWebsite("http://www.Wizzer.cn");
            profile.setGender("男");
            profile.setLinkQq("11624317");
            user.setProfile(profile);
            dao.insertWith(user, "profile");
            dao.execute(Sqls.create("insert into `sys_user_role` (`user_id`, `role_id`) values('1','1')"));
            dao.execute(Sqls.create("insert into `sys_user_role` (`user_id`, `role_id`) values('1','2')"));

        }
    }

    /**
     * 初始化Velocity
     * @param config
     * @throws IOException
     */
    private void velocityInit(NutConfig config) throws IOException {
        log.info("Veloctiy Init Start...");
        Properties p = new Properties();
        p.setProperty("resource.loader", "file,classloader");
        p.setProperty("file.resource.loader.path", config.getAppRoot());
        p.setProperty("file", "org.apache.velocity.tools.view.WebappResourceLoader");
        p.setProperty("classloader.resource.loader.class", "cn.wizzer.common.mvc.view.VelocityResourceLoader");
        p.setProperty("classloader.resource.loader.path", config.getAppRoot());
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        p.setProperty("velocimacro.library.autoreload", "false");
        p.setProperty("classloader.resource.loader.root", config.getAppRoot());
        p.setProperty("velocimarco.library.autoreload", "true");
        p.setProperty("runtime.log.error.stacktrace", "false");
        p.setProperty("runtime.log.warn.stacktrace", "false");
        p.setProperty("runtime.log.info.stacktrace", "false");
        p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        p.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");
        p.setProperty("velocimacro.library","/WEB-INF/template/common/globals.html");
        Velocity.init(p);
        log.info("Veloctiy Init End.");
    }

    public void destroy(NutConfig config) {
    }
}
