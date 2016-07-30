package cn.wizzer.common.services.redis;

import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;

import static cn.wizzer.common.services.redis.RedisInterceptor.jedis;

/**
 * Created by Wizzer on 2016/7/31.
 */
@IocBean
public class RedisService {
    @Aop("redis")
    public void set(String key, String val) {
        jedis().set(key, val);
    }

    @Aop("redis")
    public String get(String key) {
        return jedis().get(key);
    }
}
