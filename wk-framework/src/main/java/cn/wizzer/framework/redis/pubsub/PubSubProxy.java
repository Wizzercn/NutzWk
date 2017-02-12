package cn.wizzer.framework.redis.pubsub;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.JedisPubSub;

public class PubSubProxy extends JedisPubSub {

    private static final Log log = Logs.get();
    protected String pattern;
    protected PubSub pb;

    public PubSubProxy(String pattern, PubSub pb) {
        this.pattern = pattern;
        this.pb = pb;
    }
    
    public void onPMessage(String pattern, String channel, String message) {
        if (log.isDebugEnabled())
            log.debugf("channel=%s, message=%s", channel, message);
        pb.onMessage(channel, message);
    }
}
