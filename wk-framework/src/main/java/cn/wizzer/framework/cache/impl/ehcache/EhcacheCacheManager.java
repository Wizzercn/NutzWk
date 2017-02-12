package cn.wizzer.framework.cache.impl.ehcache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

public class EhcacheCacheManager implements CacheManager {
    
    private byte[] lock = new byte[0];
    
    protected net.sf.ehcache.CacheManager cacheManager;

    public <K, V> Cache<K, V> getCache(String name) {
        return new EhcacheCache<K, V>(getCache(name, true));
    }

    public void init() {}

    public void depose() {}

    protected net.sf.ehcache.Cache getCache(String name, boolean create) {
        net.sf.ehcache.Cache cache = cacheManager.getCache(name);
        if (cache == null) {
            if (!create)
                return null;
            synchronized (lock) {
                cache = cacheManager.getCache(name);
                if (cache == null) {
                    cacheManager.addCache(name);
                    cache = cacheManager.getCache(name);
                }
            }
        }
        return cache;
    }
    
    public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
