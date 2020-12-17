package com.budwk.app.web.commons.ext.websocket;

import org.nutz.integration.jedis.JedisAgent;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.plugins.mvc.websocket.WsRoomProvider;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by wizzer on 2018/7/5.
 */
public class WkJedisRoomProvider implements WsRoomProvider {
    private static final Log log = Logs.get();
    protected JedisAgent jedisAgent;
    protected int RedisKeySessionTTL;

    public WkJedisRoomProvider(JedisAgent jedisAgent, int RedisKeySessionTTL) {
        this.jedisAgent = jedisAgent;
        this.RedisKeySessionTTL = RedisKeySessionTTL;
    }

    public Set<String> wsids(String room) {
        try (Jedis jedis = jedisAgent.getResource()) {
            return jedis.smembers(room);
        }
    }

    public void join(String room, String wsid) {
        try (Jedis jedis = jedisAgent.getResource()) {
            jedis.sadd(room, wsid);
            jedis.expire(room,RedisKeySessionTTL);//每次加入的时候时间有效期重置?
        }
    }

    public void left(String room, String wsid) {
        try (Jedis jedis = jedisAgent.getResource()) {
            jedis.srem(room, wsid);
        }
    }
}
