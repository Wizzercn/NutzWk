package cn.wizzer.common.services.redis;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Wizzer on 2016/7/31.
 */
@IocBean(name="redis")
public class RedisInterceptor implements MethodInterceptor {

    @Inject
    JedisPool jedisPool;

    static ThreadLocal<Jedis> TL = new ThreadLocal<Jedis>();

    public void filter(InterceptorChain chain) throws Throwable {
        try (Jedis jedis = jedisPool.getResource()) {
            TL.set(jedis);
            chain.doChain();
        } finally{
            TL.remove();
        }
    }

    public static Jedis jedis() {
        return TL.get();
    }
}