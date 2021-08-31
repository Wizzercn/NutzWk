package com.budwk.app.web.commons.ext.pubsub;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.sys.services.SysConfigService;
import com.budwk.app.sys.services.SysRouteService;
import org.nutz.integration.jedis.pubsub.PubSub;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 订阅发布用于更新所有实例的 Globals变量
 * Created by wizzer on 2018/3/18.
 */
@IocBean(create = "init")
public class WebPubSub implements PubSub {
    private static final Log log = Logs.get();
    @Inject
    protected PubSubService pubSubService;
    @Inject
    protected SysConfigService sysConfigService;
    @Inject
    protected SysRouteService sysRouteService;

    @Inject("refer:$ioc")
    protected Ioc ioc;

    @Inject
    protected PropertiesProxy conf;

    public void init() {
        pubSubService.reg(RedisConstant.PLATFORM_REDIS_PREFIX+"web:platform", this);
    }

    @Override
    public void onMessage(String channel, String message) {
        log.debug("WebPubSub onMessage::" + message);
        switch (message) {
            case "sys_config":
                Globals.initSysConfig(sysConfigService);
                break;
            case "sys_route":
                Globals.initRoute(sysRouteService);
                break;
            case "sys_wx":
                Globals.initWx();
                break;
        }
    }
}
