package cn.wizzer.framework.cache.impl.lcache;

import cn.wizzer.framework.redis.JedisAgent;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.random.R;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LCacheManager implements CacheManager, Runnable, Destroyable, Initializable {

    private static final Log log = Logs.get();

    public static String PREFIX = "LCache:";

    protected String id = R.UU32();

    protected CacheManager level1;
    protected CacheManager level2;

    protected JedisAgent jedisAgent;
    protected CachePubSub pubSub = new CachePubSub();
    protected Map<String, LCache> caches = new HashMap<String, LCache>();
    protected boolean running = true;
    protected Thread t;

    protected static LCacheManager me;

    public static LCacheManager me() {
        return me;
    }

    public LCacheManager() {
        me = this;
    }

    public Jedis jedis() {
        return jedisAgent.jedis();
    }

    public void setJedisAgent(JedisAgent jedisAgent) {
        this.jedisAgent = jedisAgent;
        t = new Thread(this, "lcache.pubsub");
        t.start();
    }

    public void run() {
        int count = 1;
        while (running) {
            try {
                log.debug("psubscribe " + PREFIX + "*");
                jedis().psubscribe(pubSub, PREFIX + "*");
            }
            catch (Exception e) {
                if (!running)
                    break;
                log.debug("psubscribe fail, retry after 3 seconds", e);
                Lang.quiteSleep(count * 1000);
            }
        }
    }

    public void destroy() throws Exception {
        running = false;
        if (pubSub != null)
            pubSub.unsubscribe();
        try {
            Client client = (Client) Mirror.me(pubSub).getValue(pubSub, "client");
            if (client != null)
                client.close();
        } catch (Throwable e) {
        }
        if (level2 != null && level2 instanceof Destroyable)
            ((Destroyable) level2).destroy();
        if (level1 != null && level1 instanceof Destroyable)
            ((Destroyable) level1).destroy();
    }

    public void init() {
        if (level2 != null && level2 instanceof Initializable)
            ((Initializable) level2).init();
        if (level1 != null && level1 instanceof Initializable)
            ((Initializable) level1).init();
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) {
        LCache<K, V> combo = caches.get(name);
        if (combo != null)
            return (Cache<K, V>) combo;
        combo = new LCache<K, V>(name);
        if (level1 != null)
            combo.add((Cache<K, V>) level1.getCache(name));
        if (level2 != null)
            combo.add((Cache<K, V>) level2.getCache(name));
        caches.put(name, combo);
        return combo;
    }

    public void setLevel1(CacheManager level1) {
        this.level1 = level1;
    }

    public void setLevel2(CacheManager level2) {
        this.level2 = level2;
    }

}
