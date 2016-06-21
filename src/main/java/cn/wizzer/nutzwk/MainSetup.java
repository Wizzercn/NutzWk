package cn.wizzer.nutzwk;

import cn.wizzer.common.mvc.config.Dict;
import cn.wizzer.common.util.CacheUtils;
import cn.wizzer.modules.sys.bean.*;
import cn.wizzer.common.mvc.config.Globals;
import cn.wizzer.modules.sys.service.ConfigService;
import cn.wizzer.modules.sys.service.DictService;
import cn.wizzer.nutzwk.models.sys.Sys_menu;
import cn.wizzer.nutzwk.models.sys.Sys_role;
import cn.wizzer.nutzwk.models.sys.Sys_unit;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.velocity.app.Velocity;
import org.nutz.dao.Chain;
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
 * Created by wizzer on 2016/6/21.
 */
public class MainSetup implements Setup {
    private static final Log log = Logs.get();
    static DictService dictService = Mvcs.ctx().getDefaultIoc().get(DictService.class);
    static ConfigService configService = Mvcs.ctx().getDefaultIoc().get(ConfigService.class);
    public void init(NutConfig config) {
        try {
            Ioc ioc = config.getIoc();
            Dao dao = ioc.get(Dao.class);
            // 初始化数据表
            initSysData(config, dao);
            // 初始化Velocity
            velocityInit(config);
            // 获取NutQuartzCronJobFactory从而触发计划任务的初始化与启动
            ioc.get(NutQuartzCronJobFactory.class);
            // 检查一下Ehcache CacheManager 是否正常.
            CacheManager cacheManager = ioc.get(CacheManager.class);
            log.debug("Ehcache CacheManager = " + cacheManager);
            /* redis测试
            JedisPool jedisPool = ioc.get(JedisPool.class);
            try (Jedis jedis = jedisPool.getResource()) {
                String re = jedis.set("_big_fish", "Hello Word!!");
                log.debug("1.redis say : " + re);
                re = jedis.get("_big_fish");
                log.debug("2.redis say : " + re);
            } finally {}

            RedisService redis = ioc.get(RedisService.class);
            redis.set("hi", "wendal,rekoe hoho..");
            log.debug("redis say again : " + redis.get("hi"));
            */
            // 初始化系统变量
            initSysSetting(config, dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化数据库
     *
     * @param config
     * @param dao
     */
    private void initSysData(NutConfig config, Dao dao) {
        Daos.createTablesInPackage(dao, "cn.wizzer.modules", true);
        // 若必要的数据表不存在，则初始化数据库
        if (0 == dao.count(Sys_user.class)) {
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
            unit.setPath("0001");
            unit.setName("系统管理");
            unit.setLocation(0);
            unit.setAddress("银河-太阳系-地球");
            unit.setEmail("wizzer@qq.com");
            unit.setTelephone("");
            unit.setHasChildren(true);
            unit.setParentId("");
            unit.setWebsite("http://www.wizzer.cn");
            Sys_unit dbunit = dao.insert(unit);
            unit = new Sys_unit();
            unit.setPath("00010001");
            unit.setParentId(dbunit.getId());
            unit.setName("子单位");
            unit.setLocation(0);
            unit.setAddress("安徽-合肥-蜀山区");
            unit.setEmail("wizzer@qq.com");
            unit.setTelephone("");
            dao.insert(unit);
            //初始化菜单
            List<Sys_menu> menuList = new ArrayList<Sys_menu>();
            Sys_menu menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("0001");
            menu.setName("系统管理");
            menu.setDescription("系统管理");
            menu.setAliasName("System");
            menu.setIcon("ti-settings");
            menu.setLocation(0);
            menu.setHref("");
            menu.setShow(true);
            menu.setHasChildren(true);
            menu.setParentId("");
            menu.setType("menu");
            Sys_menu m1 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010001");
            menu.setName("组织结构");
            menu.setAliasName("Units");
            menu.setLocation(0);
            menu.setHref("/private/sys/unit");
            menu.setShow(true);
            menu.setPermission("sys:unit");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m2 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010002");
            menu.setName("用户管理");
            menu.setAliasName("Users");
            menu.setLocation(0);
            menu.setHref("/private/sys/user");
            menu.setShow(true);
            menu.setPermission("sys:user");
            menu.setHasChildren(false);
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m3 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("000100020001");
            menu.setName("添加用户");
            menu.setAliasName("Add");
            menu.setLocation(0);
            menu.setShow(false);
            menu.setPermission("sys:user:add");
            menu.setParentId(m3.getId());
            menu.setType("button");
            Sys_menu m31 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("000100020002");
            menu.setName("修改用户");
            menu.setAliasName("Update");
            menu.setLocation(0);
            menu.setShow(false);
            menu.setPermission("sys:user:update");
            menu.setParentId(m3.getId());
            menu.setType("button");
            Sys_menu m32 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("000100020003");
            menu.setName("删除用户");
            menu.setAliasName("Delete");
            menu.setLocation(0);
            menu.setShow(false);
            menu.setPermission("sys:user:delete");
            menu.setParentId(m3.getId());
            menu.setType("button");
            Sys_menu m33 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("000100020004");
            menu.setName("更新资料");
            menu.setAliasName("Profile");
            menu.setLocation(0);
            menu.setShow(false);
            menu.setPermission("sys:user:profile");
            menu.setParentId(m3.getId());
            menu.setType("button");
            Sys_menu m34 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010003");
            menu.setName("角色管理");
            menu.setAliasName("Roles");
            menu.setLocation(0);
            menu.setHref("/private/sys/role");
            menu.setShow(true);
            menu.setPermission("sys:role");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m4 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010004");
            menu.setName("菜单管理");
            menu.setAliasName("Menus");
            menu.setLocation(0);
            menu.setHref("/private/sys/menu");
            menu.setShow(true);
            menu.setPermission("sys:menu");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m5 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010005");
            menu.setName("参数配置");
            menu.setAliasName("Params");
            menu.setLocation(0);
            menu.setHref("/private/sys/config");
            menu.setShow(true);
            menu.setPermission("sys:config");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m6 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010006");
            menu.setName("数据字典");
            menu.setAliasName("Dicts");
            menu.setLocation(0);
            menu.setHref("/private/sys/dict");
            menu.setShow(true);
            menu.setPermission("sys:dict");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m7 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010007");
            menu.setName("日志管理");
            menu.setAliasName("Logs");
            menu.setLocation(0);
            menu.setHref("/private/sys/log");
            menu.setShow(true);
            menu.setPermission("sys:log");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m8 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setEnabled(true);
            menu.setPath("00010008");
            menu.setName("插件管理");
            menu.setAliasName("Plugins");
            menu.setLocation(0);
            menu.setHref("/private/sys/plugin");
            menu.setShow(true);
            menu.setPermission("sys:plugin");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m9 = dao.insert(menu);
            //初始化角色
            Sys_role role = new Sys_role();
            role.setName("公共角色");
            role.setCode("public");
            role.setAliasName("Public");
            role.setDescription("All user's role.");
            role.setLocation(0);
            role.setUnitid("_system");
            role.setEnabled(true);
            dao.insert(role);
            role = new Sys_role();
            role.setName("超级管理员");
            role.setCode("superadmin");
            role.setAliasName("Superadmin");
            role.setDescription("Super Admin");
            role.setLocation(1);
            role.setUnitid("_system");
            role.setMenus(menuList);
            role.setEnabled(true);
            Sys_role dbrole = dao.insert(role);
            //初始化用户
            Sys_user user = new Sys_user();
            user.setUsername("superadmin");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("1", salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            user.setLoginIp("127.0.0.1");
            Sys_user dbuser = dao.insert(user);
            Sys_user_profile profile = new Sys_user_profile();
            profile.setNickname("超级管理员");
            profile.setEmail("wizzer@qq.com");
            profile.setLinkWebsite("http://www.Wizzer.cn");
            profile.setGender("男");
            profile.setLinkQq("11624317");
            profile.setUserId(dbuser.getId());
            dao.insert(profile);
            //不同的插入数据方式(安全)
            dao.insert("sys_user_unit", Chain.make("user_id", dbuser.getId()).add("unit_id", dbunit.getId()));
            dao.insert("sys_user_role", Chain.make("user_id", dbuser.getId()).add("role_id", dbrole.getId()));
            //执行自定义SQL插入
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m1.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m2.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m3.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m31.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m32.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m33.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m34.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m4.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m5.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m6.getId() + "')"));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values('" + dbrole.getId() + "','" + m7.getId() + "')"));
            //另外一种写法(安全)
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values(@a,@b)").setParam("a", dbrole.getId()).setParam("b", m8.getId()));
            dao.execute(Sqls.create("insert into `sys_role_menu` (`role_id`, `menu_id`) values(@a,@b)").setParam("a", dbrole.getId()).setParam("b", m9.getId()));

        }
    }

    /**
     * 初始化系统变量
     *
     * @param config
     * @param dao
     */
    private void initSysSetting(NutConfig config, Dao dao) {
        Map<String, String> map = new HashMap<>();
        List<Sys_config> configList = dao.query(Sys_config.class, Cnd.orderBy().asc("location"));
        for (Sys_config sysConfig : configList) {
            map.put(sysConfig.getCname(), sysConfig.getCvalue());
        }
        Map<String, Object> dictMap = new HashMap<>();
        dictMap.put(Dict.DIVSION, dictService.list(Sqls.create("SELECT dkey,dval FROM sys_dict WHERE id LIKE '" + Dict.DIVSION + "_%'")));
        CacheUtils.put(Globals.SYS_CONFIG_KEY, map);
        CacheUtils.put(Globals.SYS_DICT_KEY, dictMap);
        Globals.APP_BASE_PATH = Strings.sNull(config.getAppRoot());//项目路径
        Globals.APP_BASE_NAME = Strings.sNull(config.getServletContext().getContextPath());//部署名
        Globals.APP_NAME = Strings.sNull(map.get("app_name"));//项目名称
    }

    /**
     * 初始化Velocity
     *
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
        p.setProperty("velocimacro.library", "/WEB-INF/template/common/globals.html");
        Velocity.init(p);
        log.info("Veloctiy Init End.");
    }

    public void destroy(NutConfig config) {
    }
}
