package cn.wizzer.framework.redis;

import org.nutz.lang.Lang;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.JedisCluster.Reset;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

import java.util.*;
import java.util.Map.Entry;

/**
 * 将JedisCluster对象封装为Jedis对象. 请务必留意哪些方法是集群环境下不可使用的.
 * Created by wendal,wizzer on 2017/1/21.
 *
 */
@SuppressWarnings("deprecation")
public class JedisClusterWrapper extends Jedis {

    protected JedisCluster jedisCluster;
    
    public JedisClusterWrapper() {}
    
    public JedisClusterWrapper(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
    
    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
    
    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    @Deprecated @Override
    public Set<String> keys(String pattern) {
        HashSet<String> keys = new HashSet<String>();
        for (JedisPool pool : jedisCluster.getClusterNodes().values()) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                keys.addAll(jedis.keys(pattern));
            } finally {
                if (jedis != null)
                    jedis.close();
            }
        }
        return keys;
    }

    @Deprecated @Override
    public Set<byte[]> keys(byte[] pattern) {
        HashSet<byte[]> keys = new HashSet<byte[]>();
        for (JedisPool pool : jedisCluster.getClusterNodes().values()) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                keys.addAll(jedis.keys(pattern));
            } finally {
                if (jedis != null)
                    jedis.close();
            }
        }
        return keys;
    }
    
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    public String set(String key, String value, String nxxx, String expx, long time) {
        return jedisCluster.set(key, value, nxxx, expx, time);
    }

    
    public String get(String key) {
        return jedisCluster.get(key);
    }

    
    public Long exists(String... keys) {
        return jedisCluster.exists(keys);
    }
    
    public String set(byte[] key, byte[] value) {
        return jedisCluster.set(key, value);
    }

    
    public boolean equals(Object obj) {
        return jedisCluster.equals(obj);
    }

    
    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        return jedisCluster.set(key, value, nxxx, expx, time);
    }

    
    public Long del(String... keys) {
        return jedisCluster.del(keys);
    }

    
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    
    public byte[] get(byte[] key) {
        return jedisCluster.get(key);
    }

    
    public String type(String key) {
        return jedisCluster.type(key);
    }

    
    public String quit() {
        return jedisCluster.quit();
    }

    
    public Long exists(byte[]... keys) {
        return jedisCluster.exists(keys);
    }


    
    public Boolean exists(byte[] key) {
        return jedisCluster.exists(key);
    }

    
    public Long del(byte[]... keys) {
        return jedisCluster.del(keys);
    }

    
    public Long del(byte[] key) {
        return jedisCluster.del(key);
    }

    
    public String type(byte[] key) {
        return jedisCluster.type(key);
    }
    
    public String rename(String oldkey, String newkey) {
        return jedisCluster.rename(oldkey, newkey);
    }

    public String flushDB() {
        return jedisCluster.flushDB();
    }
    
    public Long renamenx(String oldkey, String newkey) {
        return jedisCluster.renamenx(oldkey, newkey);
    }
    
    public Long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }


    
    public String rename(byte[] oldkey, byte[] newkey) {
        return jedisCluster.rename(oldkey, newkey);
    }

    
    public Long expireAt(String key, long unixTime) {
        return jedisCluster.expireAt(key, unixTime);
    }

    
    public Long renamenx(byte[] oldkey, byte[] newkey) {
        return jedisCluster.renamenx(oldkey, newkey);
    }

    
    public Long dbSize() {
        return jedisCluster.dbSize();
    }

    
    public Long expire(byte[] key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    
    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    
    public Long move(String key, int dbIndex) {
        return jedisCluster.move(key, dbIndex);
    }

    
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    
    public List<String> mget(String... keys) {
        return jedisCluster.mget(keys);
    }

    
    public Long setnx(String key, String value) {
        return jedisCluster.setnx(key, value);
    }

    
    public Long expireAt(byte[] key, long unixTime) {
        return jedisCluster.expireAt(key, unixTime);
    }

    
    public String setex(String key, int seconds, String value) {
        return jedisCluster.setex(key, seconds, value);
    }

    
    public String mset(String... keysvalues) {
        return jedisCluster.mset(keysvalues);
    }

    
    public Long ttl(byte[] key) {
        return jedisCluster.ttl(key);
    }

    
    public Long msetnx(String... keysvalues) {
        return jedisCluster.msetnx(keysvalues);
    }

    
    public Long decrBy(String key, long integer) {
        return jedisCluster.decrBy(key, integer);
    }

    
    public String flushAll() {
        return jedisCluster.flushAll();
    }

    
    public byte[] getSet(byte[] key, byte[] value) {
        return jedisCluster.getSet(key, value);
    }

    
    public Long decr(String key) {
        return jedisCluster.decr(key);
    }

    
    public List<byte[]> mget(byte[]... keys) {
        return jedisCluster.mget(keys);
    }

    
    public Long incrBy(String key, long integer) {
        return jedisCluster.incrBy(key, integer);
    }

    
    public Long setnx(byte[] key, byte[] value) {
        return jedisCluster.setnx(key, value);
    }

    
    public String setex(byte[] key, int seconds, byte[] value) {
        return jedisCluster.setex(key, seconds, value);
    }

    
    public Double incrByFloat(String key, double value) {
        return jedisCluster.incrByFloat(key, value);
    }

    
    public String mset(byte[]... keysvalues) {
        return jedisCluster.mset(keysvalues);
    }

    
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    
    public Long msetnx(byte[]... keysvalues) {
        return jedisCluster.msetnx(keysvalues);
    }

    
    public Long append(String key, String value) {
        return jedisCluster.append(key, value);
    }

    
    public Long decrBy(byte[] key, long integer) {
        return jedisCluster.decrBy(key, integer);
    }

    
    public String substr(String key, int start, int end) {
        return jedisCluster.substr(key, start, end);
    }

    
    public Long decr(byte[] key) {
        return jedisCluster.decr(key);
    }

    
    public Long hset(String key, String field, String value) {
        return jedisCluster.hset(key, field, value);
    }

    
    public Long incrBy(byte[] key, long integer) {
        return jedisCluster.incrBy(key, integer);
    }

    
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    
    public Long hsetnx(String key, String field, String value) {
        return jedisCluster.hsetnx(key, field, value);
    }

    
    public Double incrByFloat(byte[] key, double integer) {
        return jedisCluster.incrByFloat(key, integer);
    }

    
    public String hmset(String key, Map<String, String> hash) {
        return jedisCluster.hmset(key, hash);
    }

    
    public List<String> hmget(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    
    public Long incr(byte[] key) {
        return jedisCluster.incr(key);
    }

    
    public Long hincrBy(String key, String field, long value) {
        return jedisCluster.hincrBy(key, field, value);
    }

    
    public Long append(byte[] key, byte[] value) {
        return jedisCluster.append(key, value);
    }

    
    public Double hincrByFloat(String key, String field, double value) {
        return jedisCluster.hincrByFloat(key, field, value);
    }

    
    public byte[] substr(byte[] key, int start, int end) {
        return jedisCluster.substr(key, start, end);
    }

    
    public Boolean hexists(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    
    public Long hdel(String key, String... fields) {
        return jedisCluster.hdel(key, fields);
    }

    
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hset(key, field, value);
    }

    
    public Long hlen(String key) {
        return jedisCluster.hlen(key);
    }

    
    public byte[] hget(byte[] key, byte[] field) {
        return jedisCluster.hget(key, field);
    }

    
    public Set<String> hkeys(String key) {
        return jedisCluster.hkeys(key);
    }

    
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return jedisCluster.hsetnx(key, field, value);
    }

    
    public List<String> hvals(String key) {
        return jedisCluster.hvals(key);
    }

    
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return jedisCluster.hmset(key, hash);
    }

    
    public Map<String, String> hgetAll(String key) {
        return jedisCluster.hgetAll(key);
    }

    
    public Long rpush(String key, String... strings) {
        return jedisCluster.rpush(key, strings);
    }

    
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return jedisCluster.hmget(key, fields);
    }

    
    public Long lpush(String key, String... strings) {
        return jedisCluster.lpush(key, strings);
    }

    
    public Long hincrBy(byte[] key, byte[] field, long value) {
        return jedisCluster.hincrBy(key, field, value);
    }

    
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return jedisCluster.hincrByFloat(key, field, value);
    }

    
    public List<String> lrange(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    
    public Boolean hexists(byte[] key, byte[] field) {
        return jedisCluster.hexists(key, field);
    }

    
    public Long hdel(byte[] key, byte[]... fields) {
        return jedisCluster.hdel(key, fields);
    }

    
    public String ltrim(String key, long start, long end) {
        return jedisCluster.ltrim(key, start, end);
    }

    
    public Long hlen(byte[] key) {
        return jedisCluster.hlen(key);
    }

    
    public Set<byte[]> hkeys(byte[] key) {
        return jedisCluster.hkeys(key);
    }

    
    public List<byte[]> hvals(byte[] key) {
        return new ArrayList<byte[]>(jedisCluster.hvals(key));
    }

    
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return jedisCluster.hgetAll(key);
    }

    
    public String lindex(String key, long index) {
        return jedisCluster.lindex(key, index);
    }

    
    public Long rpush(byte[] key, byte[]... strings) {
        return jedisCluster.rpush(key, strings);
    }

    
    public Long lpush(byte[] key, byte[]... strings) {
        return jedisCluster.lpush(key, strings);
    }

    
    public String lset(String key, long index, String value) {
        return jedisCluster.lset(key, index, value);
    }

    
    public Long llen(byte[] key) {
        return jedisCluster.llen(key);
    }

    
    public Long lrem(String key, long count, String value) {
        return jedisCluster.lrem(key, count, value);
    }

    
    public List<byte[]> lrange(byte[] key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    
    public String lpop(String key) {
        return jedisCluster.lpop(key);
    }

    
    public String rpop(String key) {
        return jedisCluster.rpop(key);
    }

    
    public String ltrim(byte[] key, long start, long end) {
        return jedisCluster.ltrim(key, start, end);
    }

    
    public String rpoplpush(String srckey, String dstkey) {
        return jedisCluster.rpoplpush(srckey, dstkey);
    }

    
    public Long sadd(String key, String... members) {
        return jedisCluster.sadd(key, members);
    }

    
    public byte[] lindex(byte[] key, long index) {
        return jedisCluster.lindex(key, index);
    }

    
    public Set<String> smembers(String key) {
        return jedisCluster.smembers(key);
    }

    
    public Long srem(String key, String... members) {
        return jedisCluster.srem(key, members);
    }

    
    public String lset(byte[] key, long index, byte[] value) {
        return jedisCluster.lset(key, index, value);
    }

    
    public String spop(String key) {
        return jedisCluster.spop(key);
    }

    
    public Set<String> spop(String key, long count) {
        return jedisCluster.spop(key, count);
    }

    
    public Long lrem(byte[] key, long count, byte[] value) {
        return jedisCluster.lrem(key, count, value);
    }

    
    public Long smove(String srckey, String dstkey, String member) {
        return jedisCluster.smove(srckey, dstkey, member);
    }

    
    public byte[] lpop(byte[] key) {
        return jedisCluster.lpop(key);
    }

    
    public Long scard(String key) {
        return jedisCluster.scard(key);
    }

    
    public byte[] rpop(byte[] key) {
        return jedisCluster.rpop(key);
    }

    
    public Boolean sismember(String key, String member) {
        return jedisCluster.sismember(key, member);
    }

    
    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        return jedisCluster.rpoplpush(srckey, dstkey);
    }

    
    public Set<String> sinter(String... keys) {
        return jedisCluster.sinter(keys);
    }

    
    public Long sadd(byte[] key, byte[]... members) {
        return jedisCluster.sadd(key, members);
    }

    
    public Long sinterstore(String dstkey, String... keys) {
        return jedisCluster.sinterstore(dstkey, keys);
    }

    
    public Set<byte[]> smembers(byte[] key) {
        return jedisCluster.smembers(key);
    }

    
    public Set<String> sunion(String... keys) {
        return jedisCluster.sunion(keys);
    }

    
    public Long srem(byte[] key, byte[]... member) {
        return jedisCluster.srem(key, member);
    }

    
    public byte[] spop(byte[] key) {
        return jedisCluster.spop(key);
    }

    
    public Long sunionstore(String dstkey, String... keys) {
        return jedisCluster.sunionstore(dstkey, keys);
    }

    
    public Set<byte[]> spop(byte[] key, long count) {
        return jedisCluster.spop(key, count);
    }

    
    public Set<String> sdiff(String... keys) {
        return jedisCluster.sdiff(keys);
    }

    
    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return jedisCluster.smove(srckey, dstkey, member);
    }

    
    public Long sdiffstore(String dstkey, String... keys) {
        return jedisCluster.sdiffstore(dstkey, keys);
    }

    
    public String srandmember(String key) {
        return jedisCluster.srandmember(key);
    }

    
    public Long scard(byte[] key) {
        return jedisCluster.scard(key);
    }

    
    public Boolean sismember(byte[] key, byte[] member) {
        return jedisCluster.sismember(key, member);
    }

    
    public List<String> srandmember(String key, int count) {
        return jedisCluster.srandmember(key, count);
    }

    
    public Long zadd(String key, double score, String member) {
        return jedisCluster.zadd(key, score, member);
    }

    
    public Set<byte[]> sinter(byte[]... keys) {
        return jedisCluster.sinter(keys);
    }

    
    public Long zadd(String key, double score, String member, ZAddParams params) {
        return jedisCluster.zadd(key, score, member, params);
    }

    
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return jedisCluster.zadd(key, scoreMembers);
    }

    
    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        return jedisCluster.sinterstore(dstkey, keys);
    }

    
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return jedisCluster.zadd(key, scoreMembers, params);
    }

    
    public Set<String> zrange(String key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    
    public Set<byte[]> sunion(byte[]... keys) {
        return jedisCluster.sunion(keys);
    }

    
    public Long zrem(String key, String... members) {
        return jedisCluster.zrem(key, members);
    }

    
    public Double zincrby(String key, double score, String member) {
        return jedisCluster.zincrby(key, score, member);
    }

    
    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        return jedisCluster.sunionstore(dstkey, keys);
    }

    
    public Set<byte[]> sdiff(byte[]... keys) {
        return jedisCluster.sdiff(keys);
    }

    
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return jedisCluster.zincrby(key, score, member, params);
    }

    
    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        return jedisCluster.sdiffstore(dstkey, keys);
    }

    
    public Long zrank(String key, String member) {
        return jedisCluster.zrank(key, member);
    }

    
    public byte[] srandmember(byte[] key) {
        return jedisCluster.srandmember(key);
    }

    
    public List<byte[]> srandmember(byte[] key, int count) {
        return jedisCluster.srandmember(key, count);
    }

    
    public Long zrevrank(String key, String member) {
        return jedisCluster.zrevrank(key, member);
    }

    
    public Long zadd(byte[] key, double score, byte[] member) {
        return jedisCluster.zadd(key, score, member);
    }

    
    public Set<String> zrevrange(String key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return jedisCluster.zrangeWithScores(key, start, end);
    }

    
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return jedisCluster.zadd(key, score, member, params);
    }

    
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return jedisCluster.zrevrangeWithScores(key, start, end);
    }

    
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return jedisCluster.zadd(key, scoreMembers);
    }

    
    public Long zcard(String key) {
        return jedisCluster.zcard(key);
    }

    
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return jedisCluster.zadd(key, scoreMembers, params);
    }

    
    public Set<byte[]> zrange(byte[] key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    
    public Double zscore(String key, String member) {
        return jedisCluster.zscore(key, member);
    }

    
    public Long zrem(byte[] key, byte[]... members) {
        return jedisCluster.zrem(key, members);
    }

    
    public List<String> sort(String key) {
        return jedisCluster.sort(key);
    }

    
    public Double zincrby(byte[] key, double score, byte[] member) {
        return jedisCluster.zincrby(key, score, member);
    }

    
    public List<String> sort(String key, SortingParams sortingParameters) {
        return jedisCluster.sort(key, sortingParameters);
    }

    
    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return jedisCluster.zincrby(key, score, member, params);
    }

    
    public Long zrank(byte[] key, byte[] member) {
        return jedisCluster.zrank(key, member);
    }

    
    public List<String> blpop(int timeout, String... keys) {
        return jedisCluster.blpop(timeout, keys);
    }

    
    public Long zrevrank(byte[] key, byte[] member) {
        return jedisCluster.zrevrank(key, member);
    }

    
    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return jedisCluster.zrangeWithScores(key, start, end);
    }

    
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        return jedisCluster.zrevrangeWithScores(key, start, end);
    }

    
    public Long zcard(byte[] key) {
        return jedisCluster.zcard(key);
    }

    
    public Double zscore(byte[] key, byte[] member) {
        return jedisCluster.zscore(key, member);
    }


    
    public List<byte[]> sort(byte[] key) {
        return jedisCluster.sort(key);
    }


    
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return jedisCluster.sort(key, sortingParameters, dstkey);
    }

    
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return jedisCluster.sort(key, sortingParameters);
    }

    
    public Long sort(String key, String dstkey) {
        return jedisCluster.sort(key, dstkey);
    }

    
    public List<String> brpop(int timeout, String... keys) {
        return jedisCluster.brpop(timeout, keys);
    }

    
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return jedisCluster.blpop(timeout, keys);
    }

    
    public Long zcount(String key, double min, double max) {
        return jedisCluster.zcount(key, min, max);
    }

    
    public Long zcount(String key, String min, String max) {
        return jedisCluster.zcount(key, min, max);
    }

    
    public Set<String> zrangeByScore(String key, double min, double max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return jedisCluster.sort(key, sortingParameters, dstkey);
    }

    
    public Long sort(byte[] key, byte[] dstkey) {
        return jedisCluster.sort(key, dstkey);
    }

    
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return jedisCluster.brpop(timeout, keys);
    }

    
    public Set<String> zrangeByScore(String key, String min, String max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }


    
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    
    public String auth(String password) {
        return jedisCluster.auth(password);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }
    
    public Long zcount(byte[] key, double min, double max) {
        return jedisCluster.zcount(key, min, max);
    }

    
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zcount(key, min, max);
    }

    
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              String min,
                                              String max,
                                              int offset,
                                              int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 String max,
                                                 String min,
                                                 int offset,
                                                 int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
    }

    
    public Long zremrangeByRank(String key, long start, long end) {
        return jedisCluster.zremrangeByRank(key, start, end);
    }

    
    public Long zremrangeByScore(String key, double start, double end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    
    public Long zremrangeByScore(String key, String start, String end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    
    public Long zunionstore(String dstkey, String... sets) {
        return jedisCluster.zunionstore(dstkey, sets);
    }

    
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return jedisCluster.zunionstore(dstkey, params, sets);
    }

    
    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              byte[] min,
                                              byte[] max,
                                              int offset,
                                              int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    
    public Long zinterstore(String dstkey, String... sets) {
        return jedisCluster.zinterstore(dstkey, sets);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
    }

    
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 byte[] max,
                                                 byte[] min,
                                                 int offset,
                                                 int count) {
        return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    
    public Long zremrangeByRank(byte[] key, long start, long end) {
        return jedisCluster.zremrangeByRank(key, start, end);
    }

    
    public Long zremrangeByScore(byte[] key, double start, double end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return jedisCluster.zinterstore(dstkey, params, sets);
    }

    
    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return jedisCluster.zremrangeByScore(key, start, end);
    }

    
    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        return jedisCluster.zunionstore(dstkey, sets);
    }

    
    public Long zlexcount(String key, String min, String max) {
        return jedisCluster.zlexcount(key, min, max);
    }

    
    public Set<String> zrangeByLex(String key, String min, String max) {
        return jedisCluster.zrangeByLex(key, min, max);
    }

    
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return jedisCluster.zrangeByLex(key, min, max, offset, count);
    }

    
    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedisCluster.zunionstore(dstkey, params, sets);
    }

    
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return jedisCluster.zrevrangeByLex(key, max, min);
    }

    
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
    }

    
    public Long zremrangeByLex(String key, String min, String max) {
        return jedisCluster.zremrangeByLex(key, min, max);
    }

    
    public Long strlen(String key) {
        return jedisCluster.strlen(key);
    }

    
    public Long lpushx(String key, String... string) {
        return jedisCluster.lpushx(key, string);
    }

    
    public Long persist(String key) {
        return jedisCluster.persist(key);
    }

    
    public Long rpushx(String key, String... string) {
        return jedisCluster.rpushx(key, string);
    }

    
    public String echo(String string) {
        return jedisCluster.echo(string);
    }

    
    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        return jedisCluster.zinterstore(dstkey, sets);
    }

    
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        return jedisCluster.linsert(key, where, pivot, value);
    }

    
    public String brpoplpush(String source, String destination, int timeout) {
        return jedisCluster.brpoplpush(source, destination, timeout);
    }

    
    public Boolean setbit(String key, long offset, boolean value) {
        return jedisCluster.setbit(key, offset, value);
    }

    
    public Boolean setbit(String key, long offset, String value) {
        return jedisCluster.setbit(key, offset, value);
    }

    
    public Boolean getbit(String key, long offset) {
        return jedisCluster.getbit(key, offset);
    }

    
    public Long setrange(String key, long offset, String value) {
        return jedisCluster.setrange(key, offset, value);
    }

    
    public String getrange(String key, long startOffset, long endOffset) {
        return jedisCluster.getrange(key, startOffset, endOffset);
    }

    
    public Long bitpos(String key, boolean value) {
        return jedisCluster.bitpos(key, value);
    }

    
    public Long bitpos(String key, boolean value, BitPosParams params) {
        return jedisCluster.bitpos(key, value, params);
    }

    
    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedisCluster.zinterstore(dstkey, params, sets);
    }


    
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zlexcount(key, min, max);
    }

    
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zrangeByLex(key, min, max);
    }

    
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedisCluster.zrangeByLex(key, min, max, offset, count);
    }

    
    public Object eval(String script, int keyCount, String... params) {
        return jedisCluster.eval(script, keyCount, params);
    }

    
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return jedisCluster.zrevrangeByLex(key, max, min);
    }

    
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        jedisCluster.subscribe(jedisPubSub, channels);
    }

    
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
    }

    
    public Long publish(String channel, String message) {
        return jedisCluster.publish(channel, message);
    }

    
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedisCluster.zremrangeByLex(key, min, max);
    }

    
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        jedisCluster.psubscribe(jedisPubSub, patterns);
    }


    
    public Object eval(String script, List<String> keys, List<String> args) {
        return jedisCluster.eval(script, keys, args);
    }


    
    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return jedisCluster.evalsha(sha1, keys, args);
    }

    
    public Object evalsha(String sha1, int keyCount, String... params) {
        return jedisCluster.evalsha(sha1, keyCount, params);
    }


    
    public Long bitcount(String key) {
        return jedisCluster.bitcount(key);
    }

    
    public Long bitcount(String key, long start, long end) {
        return jedisCluster.bitcount(key, start, end);
    }

    
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return jedisCluster.bitop(op, destKey, srcKeys);
    }

    

    
    public Long pexpire(String key, int milliseconds) {
        return jedisCluster.pexpire(key, milliseconds);
    }

    
    public Long pexpire(String key, long milliseconds) {
        return jedisCluster.pexpire(key, milliseconds);
    }

    
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return jedisCluster.pexpireAt(key, millisecondsTimestamp);
    }

    
    public Long pttl(String key) {
        return jedisCluster.pttl(key);
    }

    
    public String psetex(String key, int milliseconds, String value) {
        return jedisCluster.psetex(key, milliseconds, value);
    }

    
    public String psetex(String key, long milliseconds, String value) {
        return jedisCluster.psetex(key, milliseconds, value);
    }

    
    public String set(String key, String value, String nxxx) {
        return jedisCluster.set(key, value, nxxx);
    }

    
    public String set(String key, String value, String nxxx, String expx, int time) {
        return jedisCluster.set(key, value, nxxx, expx, time);
    }


    
    public Long strlen(byte[] key) {
        return jedisCluster.strlen(key);
    }


    
    public Long lpushx(byte[] key, byte[]... string) {
        return jedisCluster.lpushx(key, string);
    }


    
    public Long persist(byte[] key) {
        return jedisCluster.persist(key);
    }


    
    public Long rpushx(byte[] key, byte[]... string) {
        return jedisCluster.rpushx(key, string);
    }

    
    public byte[] echo(byte[] string) {
        return jedisCluster.echo(string);
    }
    
    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        return jedisCluster.linsert(key, where, pivot, value);
    }


    
    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        return jedisCluster.brpoplpush(source, destination, timeout);
    }

    
    public Boolean setbit(byte[] key, long offset, boolean value) {
        return jedisCluster.setbit(key, offset, value);
    }


    
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return jedisCluster.setbit(key, offset, value);
    }

    
    public Boolean getbit(byte[] key, long offset) {
        return jedisCluster.getbit(key, offset);
    }


    
    public Long setrange(byte[] key, long offset, byte[] value) {
        return jedisCluster.setrange(key, offset, value);
    }

    
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return jedisCluster.getrange(key, startOffset, endOffset);
    }

    
    public ScanResult<String> sscan(String key, int cursor) {
        return jedisCluster.sscan(key, cursor);
    }

    
    public Long publish(byte[] channel, byte[] message) {
        return jedisCluster.publish(channel, message);
    }

    
    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        jedisCluster.subscribe(jedisPubSub, channels);
    }

    @Deprecated @Override
    public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
        return jedisCluster.sscan(key, ""+cursor, params);
    }

    
    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        jedisCluster.psubscribe(jedisPubSub, patterns);
    }

    
    public Long getDB() {
        return jedisCluster.getDB();
    }
    
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return jedisCluster.eval(script, keys, args);
    }

    
    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        return jedisCluster.eval(script, keyCount, params);
    }
    
    public Object eval(byte[] script, int keyCount, byte[]... params) {
        return jedisCluster.eval(script, keyCount, params);
    }
    
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return jedisCluster.evalsha(sha1, keys, args);
    }
    
    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        return jedisCluster.evalsha(sha1, keyCount, params);
    }
    
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return jedisCluster.scan(cursor, params);
    }

    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        return jedisCluster.hscan(key, cursor);
    }
    
    public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return jedisCluster.hscan(key, cursor, params);
    }
    
    public Long bitcount(byte[] key) {
        return jedisCluster.bitcount(key);
    }
    
    public ScanResult<String> sscan(String key, String cursor) {
        return jedisCluster.sscan(key, cursor);
    }
    
    public Long bitcount(byte[] key, long start, long end) {
        return jedisCluster.bitcount(key, start, end);
    }
    
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return jedisCluster.sscan(key, cursor, params);
    }
    
    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return jedisCluster.bitop(op, destKey, srcKeys);
    }

    
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return jedisCluster.zscan(key, cursor);
    }

    public Long pexpire(byte[] key, int milliseconds) {
        return jedisCluster.pexpire(key, milliseconds);
    }
    
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return jedisCluster.zscan(key, cursor, params);
    }
    
    public Long pexpire(byte[] key, long milliseconds) {
        return jedisCluster.pexpire(key, milliseconds);
    }
    
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return jedisCluster.pexpireAt(key, millisecondsTimestamp);
    }

    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        return jedisCluster.set(key, value, nxxx, expx, time);
    }

    public Long pfadd(byte[] key, byte[]... elements) {
        return jedisCluster.pfadd(key, elements);
    }

    public long pfcount(byte[] key) {
        return jedisCluster.pfcount(key);
    }

    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return jedisCluster.pfmerge(destkey, sourcekeys);
    }

    
    public Long pfcount(byte[]... keys) {
        return jedisCluster.pfcount(keys);
    }

    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        return jedisCluster.scan(cursor, params);
    }


    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return jedisCluster.hscan(key, cursor);
    }

    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedisCluster.hscan(key, cursor, params);
    }

    public void close() {
        //jedisCluster.close();
    }

    
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return jedisCluster.sscan(key, cursor);
    }

    
    public Long pfadd(String key, String... elements) {
        return jedisCluster.pfadd(key, elements);
    }

    
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedisCluster.sscan(key, cursor, params);
    }

    
    public long pfcount(String key) {
        return jedisCluster.pfcount(key);
    }

    
    public long pfcount(String... keys) {
        return jedisCluster.pfcount(keys);
    }

    
    public String pfmerge(String destkey, String... sourcekeys) {
        return jedisCluster.pfmerge(destkey, sourcekeys);
    }

    
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return jedisCluster.zscan(key, cursor);
    }

    
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedisCluster.zscan(key, cursor, params);
    }

    
    public List<String> blpop(int timeout, String key) {
        return jedisCluster.blpop(timeout, key);
    }

    
    public List<String> brpop(int timeout, String key) {
        return jedisCluster.brpop(timeout, key);
    }

    
    public Long geoadd(String key, double longitude, double latitude, String member) {
        return jedisCluster.geoadd(key, longitude, latitude, member);
    }

    
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return jedisCluster.geoadd(key, memberCoordinateMap);
    }

    
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return jedisCluster.geoadd(key, longitude, latitude, member);
    }

    
    public Double geodist(String key, String member1, String member2) {
        return jedisCluster.geodist(key, member1, member2);
    }

    
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return jedisCluster.geoadd(key, memberCoordinateMap);
    }

    
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return jedisCluster.geodist(key, member1, member2, unit);
    }

    
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return jedisCluster.geodist(key, member1, member2);
    }

    
    public List<String> geohash(String key, String... members) {
        return jedisCluster.geohash(key, members);
    }

    
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return jedisCluster.geodist(key, member1, member2, unit);
    }

    
    public List<GeoCoordinate> geopos(String key, String... members) {
        return jedisCluster.geopos(key, members);
    }

    
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return jedisCluster.geohash(key, members);
    }

    
    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit);
    }

    
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return jedisCluster.geopos(key, members);
    }

    
    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit, param);
    }

    
    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit);
    }

    
    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedisCluster.georadiusByMember(key, member, radius, unit);
    }

    
    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedisCluster.georadius(key, longitude, latitude, radius, unit, param);
    }

    
    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedisCluster.georadiusByMember(key, member, radius, unit, param);
    }

    
    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedisCluster.georadiusByMember(key, member, radius, unit);
    }

    
    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedisCluster.georadiusByMember(key, member, radius, unit, param);
    }
    

    //----------------------------------------------------------------------------------
    // 需要留意的方法, 部分是调用无意义
    //----------------------------------------------------------------------------------

    public String ping() {
        return jedisCluster.ping();
    }
    @Deprecated @Override
    public String bgsave() {
        return jedisCluster.bgsave();
    }
    @Deprecated @Override
    public String bgrewriteaof() {
        return jedisCluster.bgrewriteaof();
    }
    @Deprecated @Override
    public String slaveofNoOne() {
        return jedisCluster.slaveofNoOne();
    }
    @Deprecated @Override
    public Long waitReplicas(int replicas, long timeout) {
        return jedisCluster.waitReplicas(replicas, timeout);
    }

@Deprecated @Override
    
    public String select(int index) {
        return jedisCluster.select(index);
    }


    @Deprecated @Override
    public List<String> blpop(String arg) {
        return jedisCluster.blpop(arg);
    }
    @Deprecated @Override
    public List<String> brpop(String arg) {
        return jedisCluster.brpop(arg);
    }
    
    public String save() {
        return jedisCluster.save();
    }
    public String info() {
        return jedisCluster.info();
    }

    
    public String info(String section) {
        return jedisCluster.info(section);
    }


    
    public String slaveof(String host, int port) {
        return jedisCluster.slaveof(host, port);
    }

    @Deprecated @Override
    public String configResetStat() {
        return jedisCluster.configResetStat();
    }
    
    @Deprecated @Override
    public String debug(DebugParams params) {
        return jedisCluster.debug(params);
    }

    @Deprecated @Override
    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        return jedisCluster.hscan(key, cursor);
    }

    @Deprecated @Override
    public ScanResult<Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
        return jedisCluster.hscan(key, ""+cursor, params);
    }
    
    @Deprecated @Override
    public Long bitpos(byte[] key, boolean value) {
        return jedisCluster.bitpos(new String(key), value);
    }

    @Deprecated @Override
    public Long bitpos(byte[] key, boolean value, BitPosParams params) {
        return jedisCluster.bitpos(new String(key), value, params);
    }
    
    @Deprecated @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return jedisCluster.zscan(key, cursor);
    }
    //----------------------------------------------------------------------------------
    // 明确不支持的方法列表
    //----------------------------------------------------------------------------------

    @Deprecated @Override
    public String randomKey() {
        //return jedisCluster.randomKey();
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public byte[] randomBinaryKey() {
        //return jedisCluster.randomBinaryKey();
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public Long move(byte[] key, int dbIndex) {
        //return jedisCluster.move(key, dbIndex);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Transaction multi() {
        //return jedisCluster.multi();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Object> multi(TransactionBlock jedisTransaction) {
        //return jedisCluster.multi(jedisTransaction);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public void connect() {
        //jedisCluster.connect();
    }

    @Deprecated @Override
    public void disconnect() {
        //jedisCluster.disconnect();
    }

    @Deprecated @Override
    public void resetState() {
        //jedisCluster.resetState();
    }

    @Deprecated @Override
    public List<String> blpop(String... args) {
        //return jedisCluster.blpop(args);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<String> brpop(String... args) {
        //return jedisCluster.brpop(args);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String watch(byte[]... keys) {
        //return jedisCluster.watch(keys);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String unwatch() {
        //return jedisCluster.unwatch();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> blpop(byte[] arg) {
        //return jedisCluster.blpop(arg);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> brpop(byte[] arg) {
        //return jedisCluster.brpop(arg);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> blpop(byte[]... args) {
        //return jedisCluster.blpop(args);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> brpop(byte[]... args) {
        //return jedisCluster.brpop(args);
        throw Lang.noImplement();
    }
    
    @Deprecated @Override
    public List<Object> pipelined(PipelineBlock jedisPipeline) {
        //return jedisCluster.pipelined(jedisPipeline);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Pipeline pipelined() {
        //return jedisCluster.pipelined();
        throw Lang.noImplement();
    }
    
    @Deprecated @Override
    public List<String> configGet(String pattern) {
        //return jedisCluster.configGet(pattern);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String configSet(String parameter, String value) {
        //return jedisCluster.configSet(parameter, value);
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public Object eval(String script) {
        //return jedisCluster.eval(script);
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public Object evalsha(String script) {
        //return jedisCluster.evalsha(script);
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public Boolean scriptExists(String sha1) {
        //return jedisCluster.scriptExists(sha1);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Boolean> scriptExists(String... sha1) {
        //return jedisCluster.scriptExists(sha1);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String scriptLoad(String script) {
        //return jedisCluster.scriptLoad(script);
        throw Lang.noImplement();
    }

    
    @Deprecated @Override
    public List<Slowlog> slowlogGet() {
        //return jedisCluster.slowlogGet();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Slowlog> slowlogGet(long entries) {
        //return jedisCluster.slowlogGet(entries);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Long objectRefcount(String string) {
        //return jedisCluster.objectRefcount(string);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String objectEncoding(String string) {
        //return jedisCluster.objectEncoding(string);
        throw Lang.noImplement();
    }
    
    

    @Deprecated @Override
    public Long lastsave() {
        //return jedisCluster.lastsave();
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public String shutdown() {
        //return jedisCluster.shutdown();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Long objectIdletime(String string) {
        //return jedisCluster.objectIdletime(string);
        throw Lang.noImplement();
    }
    
    

    @Deprecated @Override
    public List<Map<String, String>> sentinelMasters() {
        //return jedisCluster.sentinelMasters();
        throw Lang.noImplement();
    }
    
    

    @Deprecated @Override
    public List<String> sentinelGetMasterAddrByName(String masterName) {
        //return jedisCluster.sentinelGetMasterAddrByName(masterName);
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public Long sentinelReset(String pattern) {
        //return jedisCluster.sentinelReset(pattern);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public void monitor(JedisMonitor jedisMonitor) {
        //jedisCluster.monitor(jedisMonitor);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Map<String, String>> sentinelSlaves(String masterName) {
        //return jedisCluster.sentinelSlaves(masterName);
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public String sentinelFailover(String masterName) {
        //return jedisCluster.sentinelFailover(masterName);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        //return jedisCluster.sentinelMonitor(masterName, ip, port, quorum);
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public List<byte[]> configGet(byte[] pattern) {
        //return jedisCluster.configGet(pattern);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String sentinelRemove(String masterName) {
        //return jedisCluster.sentinelRemove(masterName);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        //return jedisCluster.sentinelSet(masterName, parameterMap);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public byte[] dump(String key) {
        //return jedisCluster.dump(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String restore(String key, int ttl, byte[] serializedValue) {
        //return jedisCluster.restore(key, ttl, serializedValue);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public byte[] configSet(byte[] parameter, byte[] value) {
        //return jedisCluster.configSet(parameter, value);
        throw Lang.noImplement();
    }
    
    @Deprecated @Override
    public boolean isConnected() {
        //return jedisCluster.isConnected();
        throw Lang.noImplement();
    }
    
    

    @Deprecated @Override
    public String clientKill(String client) {
        //return jedisCluster.clientKill(client);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public void sync() {
        //jedisCluster.sync();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clientSetname(String name) {
        //return jedisCluster.clientSetname(name);
        throw Lang.noImplement();
    }   
    

    @Deprecated @Override
    public String migrate(String host, int port, String key, int destinationDb, int timeout) {
        //return jedisCluster.migrate(host, port, key, destinationDb, timeout);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public ScanResult<String> scan(int cursor) {
        //return jedisCluster.scan(cursor);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public ScanResult<String> scan(int cursor, ScanParams params) {
        //return jedisCluster.scan(cursor, params);
        throw Lang.noImplement();
    }
    
    @Deprecated @Override
    public Client getClient() {
        //return jedisCluster.getClient();
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
        //return jedisCluster.zscan(key, cursor, params);
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public Object eval(byte[] script) {
        //return jedisCluster.eval(script);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Object evalsha(byte[] sha1) {
        //return jedisCluster.evalsha(sha1);
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public ScanResult<String> scan(String cursor) {
        //return jedisCluster.scan(cursor);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String scriptFlush() {
        //return jedisCluster.scriptFlush();
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public Long scriptExists(byte[] sha1) {
        //return jedisCluster.scriptExists(sha1);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Long> scriptExists(byte[]... sha1) {
        //return jedisCluster.scriptExists(sha1);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public byte[] scriptLoad(byte[] script) {
        //return jedisCluster.scriptLoad(script);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String scriptKill() {
        //return jedisCluster.scriptKill();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String slowlogReset() {
        //return jedisCluster.slowlogReset();
        throw Lang.noImplement();
    }
    @Deprecated @Override
    public Long slowlogLen() {
        //return jedisCluster.slowlogLen();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> slowlogGetBinary() {
        //return jedisCluster.slowlogGetBinary();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<byte[]> slowlogGetBinary(long entries) {
        //return jedisCluster.slowlogGetBinary(entries);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Long objectRefcount(byte[] key) {
        //return jedisCluster.objectRefcount(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public byte[] objectEncoding(byte[] key) {
        //return jedisCluster.objectEncoding(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Long objectIdletime(byte[] key) {
        //return jedisCluster.objectIdletime(key);
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public byte[] dump(byte[] key) {
        //return jedisCluster.dump(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String restore(byte[] key, int ttl, byte[] serializedValue) {
        //return jedisCluster.restore(key, ttl, serializedValue);
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public Long pttl(byte[] key) {
        //return jedisCluster.pttl(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String psetex(byte[] key, int milliseconds, byte[] value) {
        //return jedisCluster.psetex(key, milliseconds, value);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterNodes() {
        //return jedisCluster.clusterNodes();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String psetex(byte[] key, long milliseconds, byte[] value) {
        //return jedisCluster.psetex(key, milliseconds, value);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String readonly() {
        //return jedisCluster.readonly();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterMeet(String ip, int port) {
        //return jedisCluster.clusterMeet(ip, port);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterReset(Reset resetType) {
        //return jedisCluster.clusterReset(resetType);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        //return jedisCluster.set(key, value, nxxx);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterAddSlots(int... slots) {
        //return jedisCluster.clusterAddSlots(slots);
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public String clusterDelSlots(int... slots) {
        //return jedisCluster.clusterDelSlots(slots);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterInfo() {
        //return jedisCluster.clusterInfo();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clientKill(byte[] client) {
        //return jedisCluster.clientKill(client);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<String> clusterGetKeysInSlot(int slot, int count) {
        //return jedisCluster.clusterGetKeysInSlot(slot, count);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clientGetname() {
        //return jedisCluster.clientGetname();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clientList() {
        //return jedisCluster.clientList();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterSetSlotNode(int slot, String nodeId) {
        //return jedisCluster.clusterSetSlotNode(slot, nodeId);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clientSetname(byte[] name) {
        //return jedisCluster.clientSetname(name);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterSetSlotMigrating(int slot, String nodeId) {
        //return jedisCluster.clusterSetSlotMigrating(slot, nodeId);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<String> time() {
        //return jedisCluster.time();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterSetSlotImporting(int slot, String nodeId) {
        //return jedisCluster.clusterSetSlotImporting(slot, nodeId);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        //return jedisCluster.migrate(host, port, key, destinationDb, timeout);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterSetSlotStable(int slot) {
        //return jedisCluster.clusterSetSlotStable(slot);
        throw Lang.noImplement();
    }


    @Deprecated @Override
    public String clusterForget(String nodeId) {
        //return jedisCluster.clusterForget(nodeId);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterFlushSlots() {
        //return jedisCluster.clusterFlushSlots();
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public Long clusterKeySlot(String key) {
        //return jedisCluster.clusterKeySlot(key);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Long clusterCountKeysInSlot(int slot) {
        //return jedisCluster.clusterCountKeysInSlot(slot);
        throw Lang.noImplement();
    }
    @Deprecated @Override
    public String clusterSaveConfig() {
        //return jedisCluster.clusterSaveConfig();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String clusterReplicate(String nodeId) {
        //return jedisCluster.clusterReplicate(nodeId);
        throw Lang.noImplement();
    }
    
    
    @Deprecated @Override
    public List<String> clusterSlaves(String nodeId) {
        //return jedisCluster.clusterSlaves(nodeId);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public ScanResult<byte[]> scan(byte[] cursor) {
        //return jedisCluster.scan(cursor);
        throw Lang.noImplement();
    }

    
    @Deprecated @Override
    public String clusterFailover() {
        //return jedisCluster.clusterFailover();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public List<Object> clusterSlots() {
        //return jedisCluster.clusterSlots();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String asking() {
        //return jedisCluster.asking();
        throw Lang.noImplement();
    }
    

    @Deprecated @Override
    public List<String> pubsubChannels(String pattern) {
        //return jedisCluster.pubsubChannels(pattern);
        throw Lang.noImplement();
    }
    @Deprecated @Override
    public Long pubsubNumPat() {
        //return jedisCluster.pubsubNumPat();
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public Map<String, String> pubsubNumSub(String... channels) {
        //return jedisCluster.pubsubNumSub(channels);
        throw Lang.noImplement();
    }
    @Deprecated @Override
    public void setDataSource(Pool<Jedis> jedisPool) {
        //jedisCluster.setDataSource(jedisPool);
        throw Lang.noImplement();
    }

    @Deprecated @Override
    public String watch(String... keys) {
        //return jedisCluster.watch(keys);
        throw Lang.noImplement();
    }
}
