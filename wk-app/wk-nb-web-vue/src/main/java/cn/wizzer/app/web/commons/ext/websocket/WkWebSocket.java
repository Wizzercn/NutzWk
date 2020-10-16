package cn.wizzer.app.web.commons.ext.websocket;

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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

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

    public WsHandler createHandler(Session session, EndpointConfig config) {
        return ioc.get(WkWsHandler.class);
    }

    public void init() {
        roomPrefix = "wsroom:";
        roomProvider = new WkJedisRoomProvider(jedisAgent);
        try (Jedis jedis = jedisAgent.getResource()) {
            ScanParams match = new ScanParams().match(roomPrefix + "*");
            ScanResult<String> scan = null;
            do {
                scan = jedis.scan(scan == null ? ScanParams.SCAN_POINTER_START : scan.getStringCursor(), match);
                for (String key : scan.getResult()) {
                    switch (jedis.type(key)) {
                        case "none":
                        case "set":
                            break;
                        default:
                            jedis.del(key);
                    }
                }
            } while (!scan.isCompleteIteration());
        }
        pubSubService.reg(roomPrefix + "*", this);
    }


    public void onMessage(String channel, String message) {
        if (log.isDebugEnabled())
            log.debugf("GET PubSub channel=%s msg=%s", channel, message);
        each(channel, (index, session, length) -> session.getAsyncRemote().sendText(message));
    }
}
