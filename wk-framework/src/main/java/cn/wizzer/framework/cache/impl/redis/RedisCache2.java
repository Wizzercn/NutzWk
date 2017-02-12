package cn.wizzer.framework.cache.impl.redis;

import cn.wizzer.framework.cache.impl.lcache.LCacheManager;
import org.nutz.lang.Streams;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@SuppressWarnings("unchecked")
public class RedisCache2<K, V> extends RedisCache<K, V> {

    private static final Log log = Logs.get();

    private String name;

    public RedisCache2<K, V> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public V get(K key) {
        if (debug)
            log.debugf("GET key=%s:%s", name, key);
        Jedis jedis = null;
        byte[] buf = null;
        try {
            jedis = LCacheManager.me().jedis();
            buf = jedis.get(genKey(key));
            if (buf == null)
                return null;
            return (V) serializer.toObject(buf);
        } finally {
            Streams.safeClose(jedis);
        }
    }

    @Override
    public V put(K key, V value) {
        if (debug)
            log.debugf("SET key=%s:%s", name, key);
        Jedis jedis = null;
        try {
            jedis = LCacheManager.me().jedis();
            jedis.set(genKey(key), (byte[])serializer.fromObject(value));
            return null;
        } finally {
            Streams.safeClose(jedis);
        }
    }

    @Override
    public V remove(K key) {
        if (debug)
            log.debugf("DEL key=%s:%s", name, key);
        Jedis jedis = null;
        try {
            jedis = LCacheManager.me().jedis();
            jedis.del(genKey(key));
            return null;
        } finally {
            Streams.safeClose(jedis);
        }
    }

    public void clear() {
        if (debug)
            log.debugf("CLR name=%s", name);
        for (K key : keys()) {
            remove(key);
        }
    }

    public int size() {
        if (debug)
            log.debugf("SIZ name=%s", name);
        return keys().size();
    }

    public Set<K> keys() {
        if (debug)
            log.debugf("KEYS name=%s", name);
        Jedis jedis = null;
        try {
            jedis = LCacheManager.me().jedis();
            return (Set<K>) jedis.keys(name + ":*");
        } finally {
            Streams.safeClose(jedis);
        }
    }

    public Collection<V> values() {
        if (debug)
            log.debugf("VLES name=%s", name);
        return Collections.EMPTY_LIST;
    }
    
    protected byte[] genKey(Object key) {
        return (name + ":" + key).getBytes();
    }

}
