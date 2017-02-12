package cn.wizzer.framework.redis;

import cn.wizzer.framework.util.StringUtil;
import org.nutz.aop.InterceptorChain;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wendal,wizzer on 2017/1/11.
 */
public class RedisManager {

    private JedisPool jedisPool;

    static ThreadLocal<Jedis> TL = new ThreadLocal<Jedis>();

    public void filter(InterceptorChain chain) throws Throwable {
        try (Jedis jedis = jedisPool.getResource()) {
            TL.set(jedis);
            chain.doChain();
        } finally {
            TL.remove();
        }
    }

    public static Jedis jedis() {
        return TL.get();
    }

    public void publish(String key, String val) {
        jedis().publish(key, val);
    }


    public String set(Object key, Object val, int dbIndex) throws IOException {
        jedis().select(dbIndex);
        return jedis().set(StringUtil.getBytesFromObject(key), StringUtil.getBytesFromObject(val));

    }

    public Object get(Object key, int dbIndex) throws IOException, ClassNotFoundException {
        jedis().select(dbIndex);
        byte[] bytes = jedis().get(StringUtil.getBytesFromObject(key));
        return StringUtil.getObjectFromByteArray(bytes);

    }

    public Long del(Object key, int dbIndex) throws IOException {
        jedis().select(dbIndex);
        return jedis().del(StringUtil.getBytesFromObject(key));

    }

    public Set<byte[]> getFuzzyKeys(Object key, int dbIndex) throws IOException {
        jedis().select(dbIndex);
        return jedis().keys(StringUtil.getBytesFromObject(key));

    }

    public void flushDb(int dbIndex) {
        jedis().select(dbIndex);
        jedis().flushDB();
    }

    public Long size(int dbIndex) {
        jedis().select(dbIndex);
        return jedis().dbSize();
    }

    public void flushAll() {
        jedis().flushAll();
    }

    public List<Object> mget(int dbIndex, byte[]... keys) throws IOException, ClassNotFoundException {
        List<Object> list = new ArrayList<>();
        jedis().select(dbIndex);
        List<byte[]> bytes = jedis().mget(keys);
        if (bytes == null) {
            return list;
        }
        for (byte[] b : bytes) {
            Object o = StringUtil.getObjectFromByteArray(b);
            list.add(o);
        }
        return list;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
