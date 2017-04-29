package cn.wizzer.framework.ig;

import org.nutz.integration.jedis.JedisAgent;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import redis.clients.jedis.Jedis;

import java.util.List;

@IocBean
public class RedisIdGenerator implements IdGenerator {

    @Inject
    protected JedisAgent jedisAgent;

    public RedisIdGenerator() {
    }

    public RedisIdGenerator(JedisAgent jedisAgent) {
        this.jedisAgent = jedisAgent;
    }

    public String next(String tableName) {
        String key = tableName.replaceAll("_", "").toUpperCase();
        if (key.length() > 22) {
            key = key.substring(22);
        } else if (key.length() < 22) {
            key = Strings.alignLeft(key, 22, 'A');
        }
        try (Jedis jedis = jedisAgent.getResource()) {
            String id = String.valueOf(jedis.incr("ig:" + key));
            return key + Strings.alignRight(id, 10, '0');
        }
    }

    public Object run(List<Object> fetchParam) {
        return next((String) fetchParam.get(0));
    }

    public String fetchSelf() {
        return "ig";
    }

}
