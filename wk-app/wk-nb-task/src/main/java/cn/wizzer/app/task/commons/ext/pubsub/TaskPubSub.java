package cn.wizzer.app.task.commons.ext.pubsub;

import cn.wizzer.app.sys.modules.services.SysTaskService;
import cn.wizzer.app.task.commons.base.Globals;
import com.alibaba.dubbo.config.annotation.Reference;
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
public class TaskPubSub implements PubSub {
    private static final Log log = Logs.get();
    @Inject
    protected PubSubService pubSubService;
    @Inject
    @Reference
    protected SysTaskService sysTaskService;

    @Inject("refer:$ioc")
    protected Ioc ioc;

    @Inject
    protected PropertiesProxy conf;

    public void init() {
        pubSubService.reg("nutzwk:task:platform", this);
    }

    @Override
    public void onMessage(String channel, String message) {
        log.debug("WebPubSub onMessage::" + message);
        switch (message) {
            case "sys_task":
                ioc.get(Globals.class).init(sysTaskService);
                break;
        }
    }
}
