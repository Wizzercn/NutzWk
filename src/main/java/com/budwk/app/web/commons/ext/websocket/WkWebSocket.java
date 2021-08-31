package com.budwk.app.web.commons.ext.websocket;

import com.budwk.app.base.constant.RedisConstant;
import org.nutz.integration.jedis.JedisAgent;
import org.nutz.integration.jedis.pubsub.PubSub;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.plugins.mvc.websocket.AbstractWsEndpoint;
import org.nutz.plugins.mvc.websocket.NutWsConfigurator;
import org.nutz.plugins.mvc.websocket.WsHandler;
import redis.clients.jedis.*;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/websocket", configurator = NutWsConfigurator.class)
@IocBean(create = "init") // 使用NutWsConfigurator的必备条件
public class WkWebSocket extends AbstractWsEndpoint implements PubSub {
    protected static final Log log = Logs.get();
    @Inject
    protected PubSubService pubSubService;
    @Inject
    protected JedisAgent jedisAgent;
    @Inject("refer:$ioc")
    protected Ioc ioc;
    @Inject("java:$conf.getInt('security.timeout')")
    private int REDIS_KEY_SESSION_TTL;

    public WsHandler createHandler(Session session, EndpointConfig config) {
        return ioc.get(WkWsHandler.class);
    }

    public void init() {
        roomProvider = new WkJedisRoomProvider(jedisAgent, REDIS_KEY_SESSION_TTL);
        if (jedisAgent.isClusterMode()) {
            JedisCluster jedisCluster = jedisAgent.getJedisClusterWrapper().getJedisCluster();
            List<String> keys=new ArrayList<>();
            for (JedisPool pool : jedisCluster.getClusterNodes().values()) {
                try (Jedis jedis = pool.getResource()) {
                    ScanParams match = new ScanParams().match(RedisConstant.REDIS_KEY_WSROOM + "*");
                    ScanResult<String> scan = null;
                    do {
                        scan = jedis.scan(scan == null ? ScanParams.SCAN_POINTER_START : scan.getStringCursor(), match);
                        keys.addAll(scan.getResult());

                    } while (!scan.isCompleteIteration());
                }
            }
            try (Jedis jedis = jedisAgent.getResource()) {
                for (String key : keys) {
                    switch (jedis.type(key)) {
                        case "none":
                            break;
                        case "set":
                            break;
                        default:
                            jedis.del(key);
                    }
                }
            }
        } else {
            try (Jedis jedis = jedisAgent.getResource()) {
                ScanParams match = new ScanParams().match(RedisConstant.REDIS_KEY_WSROOM + "*");
                ScanResult<String> scan = null;
                do {
                    scan = jedis.scan(scan == null ? ScanParams.SCAN_POINTER_START : scan.getStringCursor(), match);
                    for (String key : scan.getResult()) {
                        switch (jedis.type(key)) {
                            case "none":
                                break;
                            case "set":
                                break;
                            default:
                                jedis.del(key);
                        }
                    }
                } while (!scan.isCompleteIteration());
            }
        }
        pubSubService.reg(RedisConstant.REDIS_KEY_WSROOM + "*", this);
    }


    public void onMessage(String channel, String message) {
        if (log.isDebugEnabled())
            log.debugf("GET PubSub channel=%s msg=%s", channel, message);
        each(channel, (index, session, length) -> session.getAsyncRemote().sendText(message));
    }
}
