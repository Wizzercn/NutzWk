package cn.wizzer.app.web.commons.core;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.ext.pubsub.WebPubSub;
import cn.wizzer.app.web.commons.ig.RedisIdGenerator;
import org.apache.commons.lang3.math.NumberUtils;
import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.el.opt.custom.CustomMake;
import org.nutz.integration.jedis.JedisAgent;
import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wizzer on 2018/3/16.
 */
@IocBean(create = "init", depose = "depose")
@Modules(packages = "cn.wizzer")
@Localization(value = "locales/", defaultLocalizationKey = "zh_CN")
@Encoding(input = "UTF-8", output = "UTF-8")
@ChainBy(args = "chain/nutzwk-mvc-chain.json")
@SessionBy(ShiroSessionProvider.class)
public class WebPlatformMainLauncher {
    private static final Log log = Logs.get();
    @Inject("refer:$ioc")
    private Ioc ioc;
    @Inject
    private PropertiesProxy conf;
    @Inject
    private JedisAgent jedisAgent;
    @Inject
    private Globals globals;//注入一下为了初始化
    @Inject
    private WebPubSub webPubSub;//注入一下为了初始化

    public static void main(String[] args) throws Exception {
        NbApp nb = new NbApp().setArgs(args).setPrintProcDoc(true);
        nb.getAppContext().setMainPackage("cn.wizzer");
        nb.run();
    }

    public void init() {
        //注册主键生成器
        CustomMake.me().register("ig", ioc.get(RedisIdGenerator.class));
    }

    /**
     * 当项目启动的时候把表主键加载到redis缓存中
     */
    private void initRedisIg(JedisAgent jedisAgent, PropertiesProxy conf, Dao dao) {
        long a = System.currentTimeMillis();
        try (Jedis jedis = jedisAgent.getResource()) {
            Sql sql;
            if ("mysql".equalsIgnoreCase(dao.getJdbcExpert().getDatabaseType())) {
                sql = Sqls.create("SELECT table_name FROM information_schema.columns WHERE table_schema='" + conf.get("db.name", "") + "' AND column_name='id'");
            } else {
                //oracle mssql该怎么写呢,等你来添加...
                log.info("wait for you ...");
                return;
            }
            sql.setCallback(Sqls.callback.strs());
            dao.execute(sql);
            List<String> tableNameList = sql.getList(String.class);
            for (String tableName : tableNameList) {
                List<Record> list = dao.query(tableName, Cnd.NEW().desc("id"), new Pager().setPageSize(1).setPageNumber(1));
                if (list.size() > 0) {
                    String id = list.get(0).getString("id");
                    if (Strings.isMatch(Pattern.compile("^.*[\\d]{16}$"), id)) {
                        String ym = id.substring(id.length() - 16, id.length() - 10);
                        if (Strings.isBlank(jedis.get("ig:" + tableName.toUpperCase() + ym))) {
                            jedis.set("ig:" + tableName.toUpperCase() + ym, String.valueOf(NumberUtils.toLong(id.substring(id.length() - 10, id.length()), 1)));
                        }
                    }
                }
            }
        }
        long b = System.currentTimeMillis();
        log.info("init redis ig time::" + (b - a) + "ms");
    }

    public void depose() {

    }
}
