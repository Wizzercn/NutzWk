package cn.wizzer.framework.cache.impl.redis;

import cn.wizzer.framework.cache.CacheSerializer;
import cn.wizzer.framework.cache.serializer.DefaultJdkSerializer;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

/**
 * 基于 hset的缓存实现
 * Created by wendal,wizzer on 2017/1/21.
 */
public class RedisCacheManager implements CacheManager {

    protected String mode;
    protected boolean debug;
    protected CacheSerializer serializer = new DefaultJdkSerializer();

    public <K, V> Cache<K, V> getCache(String name) {
        if (mode == null || mode.equals("hset"))
            return (Cache<K, V>) new RedisCache<K, V>().setName(name).setDebug(debug).setSerializer(serializer);
        return (Cache<K, V>) new RedisCache2<K, V>().setName(name).setDebug(debug).setSerializer(serializer);
    }


    public void init() {
    }

    public void depose() {
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setSerializer(CacheSerializer serializer) {
        this.serializer = serializer;
    }
}
