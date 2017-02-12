package cn.wizzer.framework.cache.impl.lcache;

import org.apache.shiro.cache.Cache;
import org.nutz.lang.Streams;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import redis.clients.jedis.Jedis;

import java.util.*;

public class LCache<K, V> implements Cache<K, V> {
    
    private static final Log log = Logs.get();

    protected List<Cache<K, V>> list = new ArrayList<Cache<K, V>>();

    protected String name;

    public LCache(String name) {
        this.name = name;
    }

    public void add(Cache<K, V> cache) {
        list.add(cache);
    }

    public V get(K key) {
        for (Cache<K, V> cache : list) {
            V v = cache.get(key);
            if (v != null)
                return v;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (Cache<K, V> cache : list)
            cache.put(key, value);
        fire(genKey(key));
        return null;
    }

    public V remove(K key) {
        for (Cache<K, V> cache : list)
            cache.remove(key);
        fire(genKey(key));
        return null;
    }

    @Override
    public void clear() {
        for (Cache<K, V> cache : list) {
            cache.clear();
        }
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        Set<K> keys = new HashSet<K>();
        for (Cache<K, V> cache : list) {
            keys.addAll(cache.keys());
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        Set<V> values = new HashSet<V>();
        for (Cache<K, V> cache : list) {
            values.addAll(cache.values());
        }
        return values;
    }

    public String genKey(K k) {
        return k.toString();
    }

    public void fire(String key) {
        Jedis jedis = null;
        try {
            jedis = LCacheManager.me.jedis();
            String channel = (LCacheManager.PREFIX + name);
            String msg = LCacheManager.me().id + ":" + key;
            log.debugf("fire channel=%s msg=%s", channel, msg);
            jedis.publish(channel, msg);
        } finally {
            Streams.safeClose(jedis);
        }
    }
}
