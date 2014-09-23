package cn.xuetang.core;

import cn.xuetang.common.config.Globals;
import cn.xuetang.common.task.LoadTask;
import cn.xuetang.common.util.DateUtil;
import cn.xuetang.modules.sys.bean.*;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.velocity.app.Velocity;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Daos;
import org.nutz.filepool.NutFilePool;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-11-26 下午2:11:13
 */

public class StartSetup implements Setup {
    private final Log log = Logs.get();

    @Override
    public void destroy(NutConfig config) {

    }

    @Override
    public void init(NutConfig config) {
        try {
            Dao dao = Mvcs.getIoc().get(Dao.class);
            initDB(dao);
            velocityInit(config.getAppRoot());
            Globals.APP_BASE_PATH = Strings.sNull(config.getAppRoot());//项目路径
            Globals.APP_BASE_NAME = Strings.sNull(config.getServletContext().getContextPath());//部署名
            Globals.InitSysConfig(dao);//初始化系统参数
            Globals.InitDataDict(dao);//初始化数据字典
            Globals.APP_NAME = Strings.sNull(Globals.SYS_CONFIG.get("app_name"));//项目名称
            Globals.FILE_POOL = new NutFilePool("~/tmp/myfiles", 10);//创建一个文件夹用于下载
            //初始化Quartz任务
            Globals.SCHEDULER = StdSchedulerFactory.getDefaultScheduler();
            new Thread(config.getIoc().get(LoadTask.class)).start();//定时任务
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    private void velocityInit(String appPath) throws IOException {
        log.info("Veloctiy引擎初始化...");
        Properties p = new Properties();
        p.setProperty("resource.loader", "file,classloader");
        p.setProperty("file.resource.loader.path", appPath);
        p.setProperty("file", "org.apache.velocity.tools.view.WebappResourceLoader");
        p.setProperty("classloader.resource.loader.class", "cn.xuetang.common.view.VelocityResourceLoader");
        p.setProperty("classloader.resource.loader.path", appPath);
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        p.setProperty("velocimacro.library.autoreload", "false");
        p.setProperty("classloader.resource.loader.root", appPath);
        p.setProperty("velocimarco.library.autoreload", "true");
        p.setProperty("runtime.log.error.stacktrace", "false");
        p.setProperty("runtime.log.warn.stacktrace", "false");
        p.setProperty("runtime.log.info.stacktrace", "false");
        p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        p.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");
        Velocity.init(p);
        log.info("Veloctiy引擎初始化完成。");
    }

    public void initDB(Dao dao) {
        dao.drop(Sys_user.class);
        if (!dao.exists(Sys_user.class)) {
            log.info("数据库始化...");
            Daos.createTablesInPackage(dao, "cn.xuetang.modules", true);
            //初始化配置表
            Sys_config config = new Sys_config();
            config.setId(1);
            config.setCname("app_name");
            config.setCvalue("NutzWk开源企业级开发框架");
            config.setNote("项目名称，用于管理后台显示");
            dao.insert(config);
            //初始化IP防火墙表
            Sys_safeconfig safeconfig = new Sys_safeconfig();
            safeconfig.setId(1);
            safeconfig.setType(1);
            safeconfig.setState(1);
            safeconfig.setNote("10.10.10.1");
            dao.insert(safeconfig);
            safeconfig.setId(2);
            safeconfig.setType(0);
            safeconfig.setState(0);
            safeconfig.setNote("10.10.10.2");
            dao.insert(safeconfig);
            //初始化单位
            Sys_unit unit = new Sys_unit();
            unit.setId("0001");
            unit.setName("系统管理");
            unit.setLocation(0);
            dao.insert(unit);
            //初始化用户
            Sys_user user = new Sys_user();
            user.setUid(1);
            user.setLoginname("superadmin");
            user.setRealname("超级管理员");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("11", salt, 1024).toBase64();
            user.setPassword(hashedPasswordBase64);
            user.setSalt(salt);
            user.setLogintime(DateUtil.getCurDateTime());
            user.setUnitid(unit.getId());
            dao.insert(user);
            //初始化角色表
            Sys_role role = new Sys_role();
            role.setId(1);
            role.setName("公共角色");
            dao.insert(role);
            role.setId(2);
            role.setName("系统管理");
            dao.insert(role);
            //初始化菜单
            Sys_resource resource = new Sys_resource();
            resource.setId("0001");
            resource.setName("系统管理");
            resource.setUrl("");
            resource.setLocation(0);
            dao.insert(resource);
            resource.setId("00010001");
            resource.setName("系统管理");
            resource.setUrl("");
            resource.setLocation(1);
            dao.insert(resource);
            resource.setId("000100010001");
            resource.setName("机构管理");
            resource.setUrl("/private/sys/unit");
            resource.setLocation(2);
            resource.setButton("增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;排序/BtnSort;");
            dao.insert(resource);
            resource.setId("000100010002");
            resource.setName("用户管理");
            resource.setUrl("/private/sys/user");
            resource.setLocation(3);
            resource.setButton("增加/BtnAdd;修改/BtnUpdate;删除/BtnDel;禁用/BtnLocked;启用/BtnUnlocked;");
            dao.insert(resource);
            resource.setId("000100010003");
            resource.setName("角色管理");
            resource.setUrl("/private/sys/role");
            resource.setLocation(4);
            resource.setButton("增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;添加用户到角色/BtnAddRole;从角色中删除用户/BtnDelRole;分配权限/BtnMenu;");
            dao.insert(resource);
            resource.setId("000100010004");
            resource.setName("资源管理");
            resource.setUrl("/private/sys/res");
            resource.setLocation(5);
            resource.setButton("增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;排序/BtnSort;");
            dao.insert(resource);
            resource.setId("000100010005");
            resource.setName("参数配置");
            resource.setUrl("/private/sys/config");
            resource.setLocation(6);
            resource.setButton("新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;");
            dao.insert(resource);
            resource.setId("000100010006");
            resource.setName("数字字典");
            resource.setUrl("/private/sys/dict");
            resource.setLocation(7);
            resource.setButton("增加/BtnAdd;修改/BtnUpdate;删除/BtnDel;排序/BtnPaixu;");
            dao.insert(resource);
            resource.setId("000100010007");
            resource.setName("定时任务");
            resource.setUrl("/private/sys/task");
            resource.setLocation(8);
            resource.setButton("新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;");
            dao.insert(resource);
            resource.setId("000100010008");
            resource.setName("访问控制");
            resource.setUrl("/private/sys/safe");
            resource.setLocation(9);
            resource.setButton("");
            dao.insert(resource);
            resource.setId("000100010009");
            resource.setName("登陆日志");
            resource.setUrl("/private/sys/user/log");
            resource.setLocation(9);
            resource.setButton("");
            dao.insert(resource);
            //初始化角色资源
            Sys_role_resource roleResource = new Sys_role_resource();
            roleResource.setRoleid(2);
            roleResource.setResourceid("0001");
            roleResource.setButton("");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("00010001");
            roleResource.setButton("");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010001");
            roleResource.setButton("BtnAdd,BtnDel,BtnUpdate,BtnSort,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010002");
            roleResource.setButton("BtnAdd,BtnUpdate,BtnDel,BtnLocked,BtnUnlocked,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010003");
            roleResource.setButton("BtnAdd,BtnDel,BtnUpdate,BtnAddRole,BtnDelRole,BtnMenu,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010004");
            roleResource.setButton("BtnAdd,BtnDel,BtnUpdate,BtnSort,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010005");
            roleResource.setButton("BtnAdd,BtnUpdate,BtnDel,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010006");
            roleResource.setButton("BtnAdd,BtnUpdate,BtnDel,BtnPaixu,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010007");
            roleResource.setButton("BtnAdd,BtnUpdate,BtnDel,");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010008");
            roleResource.setButton("");
            dao.insert(roleResource);
            roleResource.setRoleid(2);
            roleResource.setResourceid("000100010009");
            roleResource.setButton("");
            dao.insert(roleResource);
            //初始化角色用户
            Sys_user_role userRole = new Sys_user_role();
            userRole.setRoleid(2);
            userRole.setUserid(1);
            dao.insert(userRole);
            //Oracle创建序列
            if (dao.meta().isOracle()) {
                dao.execute(Sqls.create("CREATE SEQUENCE SYS_USER_S  INCREMENT BY 1 START WITH 10001 NOMAXVALUE NOCYCLE  CACHE 10;"));
                dao.execute(Sqls.create("CREATE SEQUENCE SYS_CONFIG_S  INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE  CACHE 10;"));
                dao.execute(Sqls.create("CREATE SEQUENCE SYS_ROLE_S  INCREMENT BY 1 START WITH 3 NOMAXVALUE NOCYCLE  CACHE 10;"));
                dao.execute(Sqls.create("CREATE SEQUENCE SYS_TASK_S  INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE  CACHE 10;"));
                dao.execute(Sqls.create("CREATE SEQUENCE SYS_USER_LOG_S  INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCYCLE  CACHE 10;"));
            }
            FileSqlManager fm = new FileSqlManager("init_sys_dict.sql");
            List<Sql> sqlList = fm.createCombo(fm.keys());
            dao.execute(sqlList.toArray(new Sql[sqlList.size()]));
            log.info("数据库初始化完成。");

        }
    }
}
