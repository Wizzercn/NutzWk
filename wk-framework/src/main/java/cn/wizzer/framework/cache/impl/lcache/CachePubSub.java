package cn.wizzer.framework.cache.impl.lcache;

import org.apache.shiro.cache.Cache;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.JedisPubSub;

public class CachePubSub extends JedisPubSub {

    private static final Log log = Logs.get();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void onPMessage(String pattern, String channel, String message) {
        log.debugf("channel=%s, msg=%s", channel, message);
        if (message.startsWith(LCacheManager.me.id))
            return;
        String cacheName = channel.substring(LCacheManager.PREFIX.length());
        LCache cache = LCacheManager.me.caches.get(cacheName);
        if (cache != null && cache.list.size() > 1)
            ((Cache) cache.list.get(0)).remove(message.substring(LCacheManager.me.id.length() + 1));
    }
}
