package cn.wizzer.framework.cache.impl.ehcache;

import net.sf.ehcache.Element;
import org.apache.shiro.cache.Cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class EhcacheCache<K, V> implements Cache<K, V> {
    
    protected net.sf.ehcache.Cache cache;

    public EhcacheCache(net.sf.ehcache.Cache cache) {
        this.cache = cache;
    }

    @Override
    public V get(K key) {
        Element ele = cache.get(key);
        if (ele == null)
            return null;
        return (V) ele.getObjectValue();
    }

    @Override
    public V put(K key, V value) {
        cache.put(new Element(key, value));
        return null;
    }

    @Override
    public V remove(K key) {
        cache.remove(key);
        return null;
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    @Override
    public int size() {
        return cache.getSize();
    }

    @Override
    public Set<K> keys() {
        return new HashSet<K>(cache.getKeys());
    }

    @Override
    public Collection<V> values() {
        return (Collection<V>) cache.getAll(cache.getKeys()).values();
    }

}
