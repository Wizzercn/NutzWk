package cn.wizzer.common.core;

import cn.wizzer.common.base.Globals;
import cn.wizzer.modules.back.sys.models.*;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Encoding;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.quartz.Scheduler;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by wizzer on 2016/6/21.
 */
public class Setup implements org.nutz.mvc.Setup {
    private static final Log log = Logs.get();

    public void init(NutConfig config) {
        try {
            // 环境检查
            if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
                log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
            }
            Ioc ioc = config.getIoc();
            Dao dao = ioc.get(Dao.class);
            // 初始化数据表
            initSysData(config, dao);
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
            // 初始化定时任务
            initSysTask(config, dao);
            // 初始化自定义路由
            initSysRoute(config, dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化自定义路由
     * @param config
     * @param dao
     */
    private void initSysRoute(NutConfig config, Dao dao) {
        if (0 == dao.count(Sys_route.class)) {
            //路由示例
            Sys_route route = new Sys_route();
            route.setDisabled(false);
            route.setUrl("/sysadmin");
            route.setToUrl("/private/login");
            route.setType("hide");
            dao.insert(route);
        }
        Globals.initRoute(dao);
    }
    /**
     * 初始化定时任务
     *
     * @param config
     * @param dao
     */
    private void initSysTask(NutConfig config, Dao dao) {
        QuartzManager quartzManager = config.getIoc().get(QuartzManager.class);
        quartzManager.clear();
        if (0 == dao.count(Sys_task.class)) {
            //定时任务示例
            Sys_task task = new Sys_task();
            task.setDisabled(false);
            task.setName("测试任务");
            task.setJobClass("cn.wizzer.common.quartz.job.TestJob");
            task.setCron("*/5 * * * * ?");
            task.setData("{\"hi\":\"Wechat:wizzer | send red packets of support,thank u\"}");
            task.setNote("微信号：wizzer | 欢迎发送红包以示支持，多谢。。");
            dao.insert(task);
        }
        List<Sys_task> taskList = dao.query(Sys_task.class, Cnd.where("disabled", "=", 0));
        for (Sys_task sysTask : taskList) {
            try {
                QuartzJob qj = new QuartzJob();
                qj.setJobName(sysTask.getId());
                qj.setJobGroup(sysTask.getId());
                qj.setClassName(sysTask.getJobClass());
                qj.setCron(sysTask.getCron());
                qj.setComment(sysTask.getNote());
                qj.setDataMap(sysTask.getData());
                quartzManager.add(qj);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 初始化数据库
     *
     * @param config
     * @param dao
     */
    private void initSysData(NutConfig config, Dao dao) {
        Daos.createTablesInPackage(dao, "cn.wizzer.modules", false);
        // 若必要的数据表不存在，则初始化数据库
        if (0 == dao.count(Sys_user.class)) {
            //初始化配置表
            Sys_config conf = new Sys_config();
            conf.setConfigKey("AppName");
            conf.setConfigValue("NutzWk 开发框架");
            conf.setNote("系统名称");
            dao.insert(conf);
            conf = new Sys_config();
            conf.setConfigKey("AppShrotName");
            conf.setConfigValue("NutzWk架");
            conf.setNote("系统短名称");
            dao.insert(conf);
            conf = new Sys_config();
            conf.setConfigKey("AppDomain");
            conf.setConfigValue("127.0.0.1");
            conf.setNote("系统域名");
            dao.insert(conf);
            conf = new Sys_config();
            conf.setConfigKey("AppUploadPath");
            conf.setConfigValue("/upload");
            conf.setNote("文件上传文件夹");
            dao.insert(conf);
            //初始化单位
            Sys_unit unit = new Sys_unit();
            unit.setPath("0001");
            unit.setName("系统管理");
            unit.setAliasName("System");
            unit.setLocation(0);
            unit.setAddress("银河-太阳系-地球");
            unit.setEmail("wizzer@qq.com");
            unit.setTelephone("");
            unit.setHasChildren(false);
            unit.setParentId("");
            unit.setWebsite("http://www.wizzer.cn");
            Sys_unit dbunit = dao.insert(unit);
            //初始化菜单
            List<Sys_menu> menuList = new ArrayList<Sys_menu>();
            Sys_menu menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001");
            menu.setName("系统");
            menu.setNote("系统");
            menu.setAliasName("System");
            menu.setIcon("");
            menu.setLocation(0);
            menu.setHref("");
            menu.setTarget("");
            menu.setIsShow(true);
            menu.setHasChildren(true);
            menu.setParentId("");
            menu.setType("menu");
            menu.setPermission("sys");
            Sys_menu m0 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("00010001");
            menu.setName("系统管理");
            menu.setNote("系统管理");
            menu.setAliasName("Manager");
            menu.setIcon("ti-settings");
            menu.setLocation(0);
            menu.setHref("");
            menu.setTarget("");
            menu.setIsShow(true);
            menu.setHasChildren(true);
            menu.setParentId(m0.getId());
            menu.setType("menu");
            menu.setPermission("sys.manager");
            Sys_menu m1 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010001");
            menu.setName("单位管理");
            menu.setAliasName("Unit");
            menu.setLocation(0);
            menu.setHref("/private/sys/unit");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.unit");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m2 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100010001");
            menu.setName("添加单位");
            menu.setAliasName("Add");
            menu.setLocation(0);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.unit.add");
            menu.setParentId(m2.getId());
            menu.setType("data");
            Sys_menu m21 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100010002");
            menu.setName("修改单位");
            menu.setAliasName("Edit");
            menu.setLocation(0);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.unit.edit");
            menu.setParentId(m2.getId());
            menu.setType("data");
            Sys_menu m22 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100010003");
            menu.setName("删除单位");
            menu.setAliasName("Delete");
            menu.setLocation(0);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.unit.delete");
            menu.setParentId(m2.getId());
            menu.setType("data");
            Sys_menu m23 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010002");
            menu.setName("用户管理");
            menu.setAliasName("User");
            menu.setLocation(0);
            menu.setHref("/private/sys/user");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.user");
            menu.setHasChildren(false);
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m3 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100020001");
            menu.setName("添加用户");
            menu.setAliasName("Add");
            menu.setLocation(0);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.user.add");
            menu.setParentId(m3.getId());
            menu.setType("data");
            Sys_menu m31 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100020002");
            menu.setName("修改用户");
            menu.setAliasName("Edit");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.user.edit");
            menu.setParentId(m3.getId());
            menu.setType("data");
            Sys_menu m32 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100020003");
            menu.setName("删除用户");
            menu.setAliasName("Delete");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.user.delete");
            menu.setParentId(m3.getId());
            menu.setType("data");
            Sys_menu m33 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010003");
            menu.setName("角色管理");
            menu.setAliasName("Role");
            menu.setLocation(0);
            menu.setHref("/private/sys/role");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.role");
            menu.setTarget("data-pjax");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m4 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100030001");
            menu.setName("添加角色");
            menu.setAliasName("Add");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.role.add");
            menu.setParentId(m4.getId());
            menu.setType("data");
            Sys_menu m41 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100030002");
            menu.setName("修改角色");
            menu.setAliasName("Edit");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.role.edit");
            menu.setParentId(m4.getId());
            menu.setType("data");
            Sys_menu m42 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100030003");
            menu.setName("删除角色");
            menu.setAliasName("Delete");
            menu.setLocation(3);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.role.delete");
            menu.setParentId(m4.getId());
            menu.setType("data");
            Sys_menu m43 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100030004");
            menu.setName("分配菜单");
            menu.setAliasName("SetMenu");
            menu.setLocation(4);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.role.menu");
            menu.setParentId(m4.getId());
            menu.setType("data");
            Sys_menu m44 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100030005");
            menu.setName("分配用户");
            menu.setAliasName("SetUser");
            menu.setLocation(5);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.role.user");
            menu.setParentId(m4.getId());
            menu.setType("data");
            Sys_menu m45 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010004");
            menu.setName("菜单管理");
            menu.setAliasName("Menu");
            menu.setLocation(0);
            menu.setHref("/private/sys/menu");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.menu");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m5 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100040001");
            menu.setName("添加菜单");
            menu.setAliasName("Add");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.menu.add");
            menu.setParentId(m5.getId());
            menu.setType("data");
            Sys_menu m51 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100040002");
            menu.setName("修改菜单");
            menu.setAliasName("Edit");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.menu.edit");
            menu.setParentId(m5.getId());
            menu.setType("data");
            Sys_menu m52 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100040003");
            menu.setName("删除菜单");
            menu.setAliasName("Delete");
            menu.setLocation(3);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.menu.delete");
            menu.setParentId(m5.getId());
            menu.setType("data");
            Sys_menu m53 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010005");
            menu.setName("系统参数");
            menu.setAliasName("Param");
            menu.setLocation(0);
            menu.setHref("/private/sys/conf");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.conf");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m6 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100050001");
            menu.setName("添加参数");
            menu.setAliasName("Add");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.conf.add");
            menu.setParentId(m6.getId());
            menu.setType("data");
            Sys_menu m61 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100050002");
            menu.setName("修改参数");
            menu.setAliasName("Edit");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.conf.edit");
            menu.setParentId(m6.getId());
            menu.setType("data");
            Sys_menu m62 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100050003");
            menu.setName("删除参数");
            menu.setAliasName("Delete");
            menu.setLocation(3);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.conf.delete");
            menu.setParentId(m6.getId());
            menu.setType("data");
            Sys_menu m63 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010006");
            menu.setName("日志管理");
            menu.setAliasName("Log");
            menu.setLocation(0);
            menu.setHref("/private/sys/log");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.log");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m7 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100060001");
            menu.setName("清除日志");
            menu.setAliasName("Delete");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.log.delete");
            menu.setParentId(m7.getId());
            menu.setType("data");
            Sys_menu m71 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010007");
            menu.setName("定时任务");
            menu.setAliasName("Task");
            menu.setLocation(0);
            menu.setHref("/private/sys/task");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.task");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m8 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100070001");
            menu.setName("添加任务");
            menu.setAliasName("Add");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.task.add");
            menu.setParentId(m8.getId());
            menu.setType("data");
            Sys_menu m81 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100070002");
            menu.setName("修改任务");
            menu.setAliasName("Edit");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.task.edit");
            menu.setParentId(m8.getId());
            menu.setType("data");
            Sys_menu m82 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100070003");
            menu.setName("删除任务");
            menu.setAliasName("Delete");
            menu.setLocation(3);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.task.delete");
            menu.setParentId(m8.getId());
            menu.setType("data");
            Sys_menu m83 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("000100010008");
            menu.setName("自定义路由");
            menu.setAliasName("Route");
            menu.setLocation(0);
            menu.setHref("/private/sys/route");
            menu.setTarget("data-pjax");
            menu.setIsShow(true);
            menu.setPermission("sys.manager.route");
            menu.setParentId(m1.getId());
            menu.setType("menu");
            Sys_menu m9 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100080001");
            menu.setName("添加路由");
            menu.setAliasName("Add");
            menu.setLocation(1);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.route.add");
            menu.setParentId(m9.getId());
            menu.setType("data");
            Sys_menu m91 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100080002");
            menu.setName("修改路由");
            menu.setAliasName("Edit");
            menu.setLocation(2);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.route.edit");
            menu.setParentId(m9.getId());
            menu.setType("data");
            Sys_menu m92 = dao.insert(menu);
            menu = new Sys_menu();
            menu.setDisabled(false);
            menu.setPath("0001000100080003");
            menu.setName("删除路由");
            menu.setAliasName("Delete");
            menu.setLocation(3);
            menu.setIsShow(false);
            menu.setPermission("sys.manager.route.delete");
            menu.setParentId(m9.getId());
            menu.setType("data");
            Sys_menu m93 = dao.insert(menu);
            //初始化角色
            Sys_role role = new Sys_role();
            role.setName("公共角色");
            role.setCode("public");
            role.setAliasName("Public");
            role.setNote("All user has role");
            role.setUnitid("");
            role.setDisabled(false);
            dao.insert(role);
            role = new Sys_role();
            role.setName("系统管理员");
            role.setCode("sysadmin");
            role.setAliasName("Sysadmin");
            role.setNote("System Admin");
            role.setUnitid("");
            role.setMenus(menuList);
            role.setDisabled(false);
            Sys_role dbrole = dao.insert(role);
            //初始化用户
            Sys_user user = new Sys_user();
            user.setLoginname("superadmin");
            user.setNickname("超级管理员");
            user.setOpAt(1466571305);
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("1", salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            user.setLoginIp("127.0.0.1");
            user.setLoginAt(0);
            user.setLoginCount(0);
            user.setEmail("wizzer@qq.com");
            user.setLoginTheme("palette.css");
            user.setLoginBoxed(false);
            user.setLoginScroll(false);
            user.setLoginSidebar(false);
            user.setUnitid(dbunit.getId());
            Sys_user dbuser = dao.insert(user);
            //不同的插入数据方式(安全)
            dao.insert("sys_user_unit", Chain.make("userId", dbuser.getId()).add("unitId", dbunit.getId()));
            dao.insert("sys_user_role", Chain.make("userId", dbuser.getId()).add("roleId", dbrole.getId()));
            //执行自定义SQL,系统模块菜单关联到角色
            dao.execute(Sqls.create("INSERT INTO sys_role_menu(roleId,menuId) SELECT @roleId,id FROM sys_menu WHERE path LIKE '0001%'").setParam("roleId", dbrole.getId()));
            //执行微信菜单SQL脚本
            FileSqlManager fm = new FileSqlManager("db/init_menu_weixin.sql");
            List<Sql> sqlList = fm.createCombo(fm.keys());
            Sql[] sqls = sqlList.toArray(new Sql[sqlList.size()]);
            for (Sql sql : sqls) {
                dao.execute(sql);
            }
            //执行CMS菜单SQL脚本
            FileSqlManager fm_cms = new FileSqlManager("db/init_menu_cms.sql");
            List<Sql> sqlList_cms = fm_cms.createCombo(fm_cms.keys());
            Sql[] sqls_cms = sqlList_cms.toArray(new Sql[sqlList_cms.size()]);
            for (Sql sql : sqls_cms) {
                dao.execute(sql);
            }
            //微信模块菜单关联到角色
            dao.execute(Sqls.create("INSERT INTO sys_role_menu(roleId,menuId) SELECT @roleId,id FROM sys_menu WHERE path LIKE '0002%'").setParam("roleId", dbrole.getId()));
            //CMS模块菜单关联到角色
            dao.execute(Sqls.create("INSERT INTO sys_role_menu(roleId,menuId) SELECT @roleId,id FROM sys_menu WHERE path LIKE '0003%'").setParam("roleId", dbrole.getId()));
        }
    }

    /**
     * 初始化系统变量
     *
     * @param config
     * @param dao
     */
    private void initSysSetting(NutConfig config, Dao dao) {
        Globals.AppRoot = Strings.sNull(config.getAppRoot());//项目路径
        Globals.AppBase = Strings.sNull(config.getServletContext().getContextPath());//部署名
        Globals.init(dao);
    }

    public void destroy(NutConfig config) {
        // 解决quartz有时候无法停止的问题
        try {
            config.getIoc().get(Scheduler.class).shutdown(true);
        } catch (Exception e) {
        }
    }
}
