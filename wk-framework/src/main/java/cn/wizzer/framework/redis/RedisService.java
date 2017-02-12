package cn.wizzer.framework.redis;


import org.nutz.ioc.loader.annotation.IocBean;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.JedisCluster.Reset;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 代理jedis的全部方法
 *
 * Created by wendal,wizzer on 2017/1/21.
 */
@SuppressWarnings("deprecation")
@IocBean
public class RedisService extends Jedis {

    private JedisAgent jedisAgent;

    public void setJedisAgent(JedisAgent jedisAgent) {
        this.jedisAgent = jedisAgent;
    }

    public Jedis jedis() {
        return jedisAgent.jedis();
    }

    public String set(String key, String value) {
        return jedis().set(key, value);
    }


    public String set(String key, String value, String nxxx, String expx, long time) {
        return jedis().set(key, value, nxxx, expx, time);
    }


    public String get(String key) {
        return jedis().get(key);
    }


    public Long exists(String... keys) {
        return jedis().exists(keys);
    }


    public String ping() {
        return jedis().ping();
    }


    public String set(byte[] key, byte[] value) {
        return jedis().set(key, value);
    }


    public boolean equals(Object obj) {
        return jedis().equals(obj);
    }


    public Boolean exists(String key) {
        return jedis().exists(key);
    }


    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        return jedis().set(key, value, nxxx, expx, time);
    }


    public Long del(String... keys) {
        return jedis().del(keys);
    }


    public Long del(String key) {
        return jedis().del(key);
    }


    public byte[] get(byte[] key) {
        return jedis().get(key);
    }


    public String type(String key) {
        return jedis().type(key);
    }


    public String quit() {
        return jedis().quit();
    }


    public Long exists(byte[]... keys) {
        return jedis().exists(keys);
    }


    public Set<String> keys(String pattern) {
        return jedis().keys(pattern);
    }


    public Boolean exists(byte[] key) {
        return jedis().exists(key);
    }


    public Long del(byte[]... keys) {
        return jedis().del(keys);
    }


    public Long del(byte[] key) {
        return jedis().del(key);
    }


    public String type(byte[] key) {
        return jedis().type(key);
    }


    public String randomKey() {
        return jedis().randomKey();
    }


    public String rename(String oldkey, String newkey) {
        return jedis().rename(oldkey, newkey);
    }


    public String flushDB() {
        return jedis().flushDB();
    }


    public Long renamenx(String oldkey, String newkey) {
        return jedis().renamenx(oldkey, newkey);
    }


    public Set<byte[]> keys(byte[] pattern) {
        return jedis().keys(pattern);
    }


    public Long expire(String key, int seconds) {
        return jedis().expire(key, seconds);
    }


    public byte[] randomBinaryKey() {
        return jedis().randomBinaryKey();
    }


    public String rename(byte[] oldkey, byte[] newkey) {
        return jedis().rename(oldkey, newkey);
    }


    public Long expireAt(String key, long unixTime) {
        return jedis().expireAt(key, unixTime);
    }


    public Long renamenx(byte[] oldkey, byte[] newkey) {
        return jedis().renamenx(oldkey, newkey);
    }


    public Long dbSize() {
        return jedis().dbSize();
    }


    public Long expire(byte[] key, int seconds) {
        return jedis().expire(key, seconds);
    }


    public Long ttl(String key) {
        return jedis().ttl(key);
    }


    public Long move(String key, int dbIndex) {
        return jedis().move(key, dbIndex);
    }


    public String getSet(String key, String value) {
        return jedis().getSet(key, value);
    }


    public List<String> mget(String... keys) {
        return jedis().mget(keys);
    }


    public Long setnx(String key, String value) {
        return jedis().setnx(key, value);
    }


    public Long expireAt(byte[] key, long unixTime) {
        return jedis().expireAt(key, unixTime);
    }


    public String setex(String key, int seconds, String value) {
        return jedis().setex(key, seconds, value);
    }


    public String mset(String... keysvalues) {
        return jedis().mset(keysvalues);
    }


    public Long ttl(byte[] key) {
        return jedis().ttl(key);
    }


    public Long msetnx(String... keysvalues) {
        return jedis().msetnx(keysvalues);
    }


    public String select(int index) {
        return jedis().select(index);
    }


    public Long move(byte[] key, int dbIndex) {
        return jedis().move(key, dbIndex);
    }


    public Long decrBy(String key, long integer) {
        return jedis().decrBy(key, integer);
    }


    public String flushAll() {
        return jedis().flushAll();
    }


    public byte[] getSet(byte[] key, byte[] value) {
        return jedis().getSet(key, value);
    }


    public Long decr(String key) {
        return jedis().decr(key);
    }


    public List<byte[]> mget(byte[]... keys) {
        return jedis().mget(keys);
    }


    public Long incrBy(String key, long integer) {
        return jedis().incrBy(key, integer);
    }


    public Long setnx(byte[] key, byte[] value) {
        return jedis().setnx(key, value);
    }


    public String setex(byte[] key, int seconds, byte[] value) {
        return jedis().setex(key, seconds, value);
    }


    public Double incrByFloat(String key, double value) {
        return jedis().incrByFloat(key, value);
    }


    public String mset(byte[]... keysvalues) {
        return jedis().mset(keysvalues);
    }


    public Long incr(String key) {
        return jedis().incr(key);
    }


    public Long msetnx(byte[]... keysvalues) {
        return jedis().msetnx(keysvalues);
    }


    public Long append(String key, String value) {
        return jedis().append(key, value);
    }


    public Long decrBy(byte[] key, long integer) {
        return jedis().decrBy(key, integer);
    }


    public String substr(String key, int start, int end) {
        return jedis().substr(key, start, end);
    }


    public Long decr(byte[] key) {
        return jedis().decr(key);
    }


    public Long hset(String key, String field, String value) {
        return jedis().hset(key, field, value);
    }


    public Long incrBy(byte[] key, long integer) {
        return jedis().incrBy(key, integer);
    }


    public String hget(String key, String field) {
        return jedis().hget(key, field);
    }


    public Long hsetnx(String key, String field, String value) {
        return jedis().hsetnx(key, field, value);
    }


    public Double incrByFloat(byte[] key, double integer) {
        return jedis().incrByFloat(key, integer);
    }


    public String hmset(String key, Map<String, String> hash) {
        return jedis().hmset(key, hash);
    }


    public List<String> hmget(String key, String... fields) {
        return jedis().hmget(key, fields);
    }


    public Long incr(byte[] key) {
        return jedis().incr(key);
    }


    public Long hincrBy(String key, String field, long value) {
        return jedis().hincrBy(key, field, value);
    }


    public Long append(byte[] key, byte[] value) {
        return jedis().append(key, value);
    }


    public Double hincrByFloat(String key, String field, double value) {
        return jedis().hincrByFloat(key, field, value);
    }


    public byte[] substr(byte[] key, int start, int end) {
        return jedis().substr(key, start, end);
    }


    public Boolean hexists(String key, String field) {
        return jedis().hexists(key, field);
    }


    public Long hdel(String key, String... fields) {
        return jedis().hdel(key, fields);
    }


    public Long hset(byte[] key, byte[] field, byte[] value) {
        return jedis().hset(key, field, value);
    }


    public Long hlen(String key) {
        return jedis().hlen(key);
    }


    public byte[] hget(byte[] key, byte[] field) {
        return jedis().hget(key, field);
    }


    public Set<String> hkeys(String key) {
        return jedis().hkeys(key);
    }


    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return jedis().hsetnx(key, field, value);
    }


    public List<String> hvals(String key) {
        return jedis().hvals(key);
    }


    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return jedis().hmset(key, hash);
    }


    public Map<String, String> hgetAll(String key) {
        return jedis().hgetAll(key);
    }


    public Long rpush(String key, String... strings) {
        return jedis().rpush(key, strings);
    }


    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return jedis().hmget(key, fields);
    }


    public Long lpush(String key, String... strings) {
        return jedis().lpush(key, strings);
    }


    public Long hincrBy(byte[] key, byte[] field, long value) {
        return jedis().hincrBy(key, field, value);
    }


    public Long llen(String key) {
        return jedis().llen(key);
    }


    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return jedis().hincrByFloat(key, field, value);
    }


    public List<String> lrange(String key, long start, long end) {
        return jedis().lrange(key, start, end);
    }


    public Boolean hexists(byte[] key, byte[] field) {
        return jedis().hexists(key, field);
    }


    public Long hdel(byte[] key, byte[]... fields) {
        return jedis().hdel(key, fields);
    }


    public String ltrim(String key, long start, long end) {
        return jedis().ltrim(key, start, end);
    }


    public Long hlen(byte[] key) {
        return jedis().hlen(key);
    }


    public Set<byte[]> hkeys(byte[] key) {
        return jedis().hkeys(key);
    }


    public List<byte[]> hvals(byte[] key) {
        return jedis().hvals(key);
    }


    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return jedis().hgetAll(key);
    }


    public String lindex(String key, long index) {
        return jedis().lindex(key, index);
    }


    public Long rpush(byte[] key, byte[]... strings) {
        return jedis().rpush(key, strings);
    }


    public Long lpush(byte[] key, byte[]... strings) {
        return jedis().lpush(key, strings);
    }


    public String lset(String key, long index, String value) {
        return jedis().lset(key, index, value);
    }


    public Long llen(byte[] key) {
        return jedis().llen(key);
    }


    public Long lrem(String key, long count, String value) {
        return jedis().lrem(key, count, value);
    }


    public List<byte[]> lrange(byte[] key, long start, long end) {
        return jedis().lrange(key, start, end);
    }


    public String lpop(String key) {
        return jedis().lpop(key);
    }


    public String rpop(String key) {
        return jedis().rpop(key);
    }


    public String ltrim(byte[] key, long start, long end) {
        return jedis().ltrim(key, start, end);
    }


    public String rpoplpush(String srckey, String dstkey) {
        return jedis().rpoplpush(srckey, dstkey);
    }


    public Long sadd(String key, String... members) {
        return jedis().sadd(key, members);
    }


    public byte[] lindex(byte[] key, long index) {
        return jedis().lindex(key, index);
    }


    public Set<String> smembers(String key) {
        return jedis().smembers(key);
    }


    public Long srem(String key, String... members) {
        return jedis().srem(key, members);
    }


    public String lset(byte[] key, long index, byte[] value) {
        return jedis().lset(key, index, value);
    }


    public String spop(String key) {
        return jedis().spop(key);
    }


    public Set<String> spop(String key, long count) {
        return jedis().spop(key, count);
    }


    public Long lrem(byte[] key, long count, byte[] value) {
        return jedis().lrem(key, count, value);
    }


    public Long smove(String srckey, String dstkey, String member) {
        return jedis().smove(srckey, dstkey, member);
    }


    public byte[] lpop(byte[] key) {
        return jedis().lpop(key);
    }


    public Long scard(String key) {
        return jedis().scard(key);
    }


    public byte[] rpop(byte[] key) {
        return jedis().rpop(key);
    }


    public Boolean sismember(String key, String member) {
        return jedis().sismember(key, member);
    }


    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        return jedis().rpoplpush(srckey, dstkey);
    }


    public Set<String> sinter(String... keys) {
        return jedis().sinter(keys);
    }


    public Long sadd(byte[] key, byte[]... members) {
        return jedis().sadd(key, members);
    }


    public Long sinterstore(String dstkey, String... keys) {
        return jedis().sinterstore(dstkey, keys);
    }


    public Set<byte[]> smembers(byte[] key) {
        return jedis().smembers(key);
    }


    public Set<String> sunion(String... keys) {
        return jedis().sunion(keys);
    }


    public Long srem(byte[] key, byte[]... member) {
        return jedis().srem(key, member);
    }


    public byte[] spop(byte[] key) {
        return jedis().spop(key);
    }


    public Long sunionstore(String dstkey, String... keys) {
        return jedis().sunionstore(dstkey, keys);
    }


    public Set<byte[]> spop(byte[] key, long count) {
        return jedis().spop(key, count);
    }


    public Set<String> sdiff(String... keys) {
        return jedis().sdiff(keys);
    }


    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return jedis().smove(srckey, dstkey, member);
    }


    public Long sdiffstore(String dstkey, String... keys) {
        return jedis().sdiffstore(dstkey, keys);
    }


    public String srandmember(String key) {
        return jedis().srandmember(key);
    }


    public Long scard(byte[] key) {
        return jedis().scard(key);
    }


    public Boolean sismember(byte[] key, byte[] member) {
        return jedis().sismember(key, member);
    }


    public List<String> srandmember(String key, int count) {
        return jedis().srandmember(key, count);
    }


    public Long zadd(String key, double score, String member) {
        return jedis().zadd(key, score, member);
    }


    public Set<byte[]> sinter(byte[]... keys) {
        return jedis().sinter(keys);
    }


    public Long zadd(String key, double score, String member, ZAddParams params) {
        return jedis().zadd(key, score, member, params);
    }


    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return jedis().zadd(key, scoreMembers);
    }


    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        return jedis().sinterstore(dstkey, keys);
    }


    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return jedis().zadd(key, scoreMembers, params);
    }


    public Set<String> zrange(String key, long start, long end) {
        return jedis().zrange(key, start, end);
    }


    public Set<byte[]> sunion(byte[]... keys) {
        return jedis().sunion(keys);
    }


    public Long zrem(String key, String... members) {
        return jedis().zrem(key, members);
    }


    public Double zincrby(String key, double score, String member) {
        return jedis().zincrby(key, score, member);
    }


    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        return jedis().sunionstore(dstkey, keys);
    }


    public Set<byte[]> sdiff(byte[]... keys) {
        return jedis().sdiff(keys);
    }


    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return jedis().zincrby(key, score, member, params);
    }


    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        return jedis().sdiffstore(dstkey, keys);
    }


    public Long zrank(String key, String member) {
        return jedis().zrank(key, member);
    }


    public byte[] srandmember(byte[] key) {
        return jedis().srandmember(key);
    }


    public List<byte[]> srandmember(byte[] key, int count) {
        return jedis().srandmember(key, count);
    }


    public Long zrevrank(String key, String member) {
        return jedis().zrevrank(key, member);
    }


    public Long zadd(byte[] key, double score, byte[] member) {
        return jedis().zadd(key, score, member);
    }


    public Set<String> zrevrange(String key, long start, long end) {
        return jedis().zrevrange(key, start, end);
    }


    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return jedis().zrangeWithScores(key, start, end);
    }


    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return jedis().zadd(key, score, member, params);
    }


    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return jedis().zrevrangeWithScores(key, start, end);
    }


    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return jedis().zadd(key, scoreMembers);
    }


    public Long zcard(String key) {
        return jedis().zcard(key);
    }


    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return jedis().zadd(key, scoreMembers, params);
    }


    public Set<byte[]> zrange(byte[] key, long start, long end) {
        return jedis().zrange(key, start, end);
    }


    public Double zscore(String key, String member) {
        return jedis().zscore(key, member);
    }


    public Long zrem(byte[] key, byte[]... members) {
        return jedis().zrem(key, members);
    }


    public String watch(String... keys) {
        return jedis().watch(keys);
    }


    public List<String> sort(String key) {
        return jedis().sort(key);
    }


    public Double zincrby(byte[] key, double score, byte[] member) {
        return jedis().zincrby(key, score, member);
    }


    public List<String> sort(String key, SortingParams sortingParameters) {
        return jedis().sort(key, sortingParameters);
    }


    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return jedis().zincrby(key, score, member, params);
    }


    public Long zrank(byte[] key, byte[] member) {
        return jedis().zrank(key, member);
    }


    public List<String> blpop(int timeout, String... keys) {
        return jedis().blpop(timeout, keys);
    }


    public Long zrevrank(byte[] key, byte[] member) {
        return jedis().zrevrank(key, member);
    }


    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        return jedis().zrevrange(key, start, end);
    }


    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return jedis().zrangeWithScores(key, start, end);
    }


    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        return jedis().zrevrangeWithScores(key, start, end);
    }


    public Long zcard(byte[] key) {
        return jedis().zcard(key);
    }


    public Double zscore(byte[] key, byte[] member) {
        return jedis().zscore(key, member);
    }


    public Transaction multi() {
        return jedis().multi();
    }


    public List<Object> multi(TransactionBlock jedisTransaction) {
        return jedis().multi(jedisTransaction);
    }


    public void connect() {
        jedis().connect();
    }


    public void disconnect() {
        jedis().disconnect();
    }


    public void resetState() {
        jedis().resetState();
    }


    public List<String> blpop(String... args) {
        return jedis().blpop(args);
    }


    public List<String> brpop(String... args) {
        return jedis().brpop(args);
    }


    public String watch(byte[]... keys) {
        return jedis().watch(keys);
    }


    public String unwatch() {
        return jedis().unwatch();
    }


    public List<String> blpop(String arg) {
        return jedis().blpop(arg);
    }


    public List<byte[]> sort(byte[] key) {
        return jedis().sort(key);
    }


    public List<String> brpop(String arg) {
        return jedis().brpop(arg);
    }


    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return jedis().sort(key, sortingParameters, dstkey);
    }


    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return jedis().sort(key, sortingParameters);
    }


    public Long sort(String key, String dstkey) {
        return jedis().sort(key, dstkey);
    }


    public List<String> brpop(int timeout, String... keys) {
        return jedis().brpop(timeout, keys);
    }


    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return jedis().blpop(timeout, keys);
    }


    public Long zcount(String key, double min, double max) {
        return jedis().zcount(key, min, max);
    }


    public Long zcount(String key, String min, String max) {
        return jedis().zcount(key, min, max);
    }


    public Set<String> zrangeByScore(String key, double min, double max) {
        return jedis().zrangeByScore(key, min, max);
    }


    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return jedis().sort(key, sortingParameters, dstkey);
    }


    public Long sort(byte[] key, byte[] dstkey) {
        return jedis().sort(key, dstkey);
    }


    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return jedis().brpop(timeout, keys);
    }


    public Set<String> zrangeByScore(String key, String min, String max) {
        return jedis().zrangeByScore(key, min, max);
    }


    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }


    public List<byte[]> blpop(byte[] arg) {
        return jedis().blpop(arg);
    }


    public List<byte[]> brpop(byte[] arg) {
        return jedis().brpop(arg);
    }


    public List<byte[]> blpop(byte[]... args) {
        return jedis().blpop(args);
    }


    public List<byte[]> brpop(byte[]... args) {
        return jedis().brpop(args);
    }


    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }


    public String auth(String password) {
        return jedis().auth(password);
    }


    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }


    public List<Object> pipelined(PipelineBlock jedisPipeline) {
        return jedis().pipelined(jedisPipeline);
    }


    public Pipeline pipelined() {
        return jedis().pipelined();
    }


    public Long zcount(byte[] key, double min, double max) {
        return jedis().zcount(key, min, max);
    }


    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return jedis().zcount(key, min, max);
    }


    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return jedis().zrangeByScore(key, min, max);
    }


    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }


    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }


    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByScore(key, min, max);
    }


    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }


    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              String min,
                                              String max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }


    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return jedis().zrevrangeByScore(key, max, min);
    }


    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return jedis().zrevrangeByScore(key, max, min);
    }


    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }


    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }


    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 String max,
                                                 String min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }


    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }


    public Long zremrangeByRank(String key, long start, long end) {
        return jedis().zremrangeByRank(key, start, end);
    }


    public Long zremrangeByScore(String key, double start, double end) {
        return jedis().zremrangeByScore(key, start, end);
    }


    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }


    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }


    public Long zremrangeByScore(String key, String start, String end) {
        return jedis().zremrangeByScore(key, start, end);
    }


    public Long zunionstore(String dstkey, String... sets) {
        return jedis().zunionstore(dstkey, sets);
    }


    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return jedis().zunionstore(dstkey, params, sets);
    }


    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              byte[] min,
                                              byte[] max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }


    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return jedis().zrevrangeByScore(key, max, min);
    }


    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByScore(key, max, min);
    }


    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }


    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }


    public Long zinterstore(String dstkey, String... sets) {
        return jedis().zinterstore(dstkey, sets);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }


    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 byte[] max,
                                                 byte[] min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }


    public Long zremrangeByRank(byte[] key, long start, long end) {
        return jedis().zremrangeByRank(key, start, end);
    }


    public Long zremrangeByScore(byte[] key, double start, double end) {
        return jedis().zremrangeByScore(key, start, end);
    }


    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return jedis().zinterstore(dstkey, params, sets);
    }


    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return jedis().zremrangeByScore(key, start, end);
    }


    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        return jedis().zunionstore(dstkey, sets);
    }


    public Long zlexcount(String key, String min, String max) {
        return jedis().zlexcount(key, min, max);
    }


    public Set<String> zrangeByLex(String key, String min, String max) {
        return jedis().zrangeByLex(key, min, max);
    }


    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return jedis().zrangeByLex(key, min, max, offset, count);
    }


    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedis().zunionstore(dstkey, params, sets);
    }


    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return jedis().zrevrangeByLex(key, max, min);
    }


    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return jedis().zrevrangeByLex(key, max, min, offset, count);
    }


    public Long zremrangeByLex(String key, String min, String max) {
        return jedis().zremrangeByLex(key, min, max);
    }


    public Long strlen(String key) {
        return jedis().strlen(key);
    }


    public Long lpushx(String key, String... string) {
        return jedis().lpushx(key, string);
    }


    public Long persist(String key) {
        return jedis().persist(key);
    }


    public Long rpushx(String key, String... string) {
        return jedis().rpushx(key, string);
    }


    public String echo(String string) {
        return jedis().echo(string);
    }


    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        return jedis().zinterstore(dstkey, sets);
    }


    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        return jedis().linsert(key, where, pivot, value);
    }


    public String brpoplpush(String source, String destination, int timeout) {
        return jedis().brpoplpush(source, destination, timeout);
    }


    public Boolean setbit(String key, long offset, boolean value) {
        return jedis().setbit(key, offset, value);
    }


    public Boolean setbit(String key, long offset, String value) {
        return jedis().setbit(key, offset, value);
    }


    public Boolean getbit(String key, long offset) {
        return jedis().getbit(key, offset);
    }


    public Long setrange(String key, long offset, String value) {
        return jedis().setrange(key, offset, value);
    }


    public String getrange(String key, long startOffset, long endOffset) {
        return jedis().getrange(key, startOffset, endOffset);
    }


    public Long bitpos(String key, boolean value) {
        return jedis().bitpos(key, value);
    }


    public Long bitpos(String key, boolean value, BitPosParams params) {
        return jedis().bitpos(key, value, params);
    }


    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedis().zinterstore(dstkey, params, sets);
    }


    public List<String> configGet(String pattern) {
        return jedis().configGet(pattern);
    }


    public String configSet(String parameter, String value) {
        return jedis().configSet(parameter, value);
    }


    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return jedis().zlexcount(key, min, max);
    }


    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByLex(key, min, max);
    }


    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedis().zrangeByLex(key, min, max, offset, count);
    }


    public Object eval(String script, int keyCount, String... params) {
        return jedis().eval(script, keyCount, params);
    }


    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByLex(key, max, min);
    }


    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        jedis().subscribe(jedisPubSub, channels);
    }


    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedis().zrevrangeByLex(key, max, min, offset, count);
    }


    public Long publish(String channel, String message) {
        return jedis().publish(channel, message);
    }


    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedis().zremrangeByLex(key, min, max);
    }


    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        jedis().psubscribe(jedisPubSub, patterns);
    }


    public String save() {
        return jedis().save();
    }


    public Object eval(String script, List<String> keys, List<String> args) {
        return jedis().eval(script, keys, args);
    }


    public Object eval(String script) {
        return jedis().eval(script);
    }


    public String bgsave() {
        return jedis().bgsave();
    }


    public Object evalsha(String script) {
        return jedis().evalsha(script);
    }


    public String bgrewriteaof() {
        return jedis().bgrewriteaof();
    }


    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return jedis().evalsha(sha1, keys, args);
    }


    public Object evalsha(String sha1, int keyCount, String... params) {
        return jedis().evalsha(sha1, keyCount, params);
    }


    public Boolean scriptExists(String sha1) {
        return jedis().scriptExists(sha1);
    }


    public List<Boolean> scriptExists(String... sha1) {
        return jedis().scriptExists(sha1);
    }


    public String scriptLoad(String script) {
        return jedis().scriptLoad(script);
    }


    public Long lastsave() {
        return jedis().lastsave();
    }


    public List<Slowlog> slowlogGet() {
        return jedis().slowlogGet();
    }


    public List<Slowlog> slowlogGet(long entries) {
        return jedis().slowlogGet(entries);
    }


    public Long objectRefcount(String string) {
        return jedis().objectRefcount(string);
    }


    public String objectEncoding(String string) {
        return jedis().objectEncoding(string);
    }


    public String shutdown() {
        return jedis().shutdown();
    }


    public Long objectIdletime(String string) {
        return jedis().objectIdletime(string);
    }


    public Long bitcount(String key) {
        return jedis().bitcount(key);
    }


    public Long bitcount(String key, long start, long end) {
        return jedis().bitcount(key, start, end);
    }


    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return jedis().bitop(op, destKey, srcKeys);
    }


    public List<Map<String, String>> sentinelMasters() {
        return jedis().sentinelMasters();
    }


    public String info() {
        return jedis().info();
    }


    public List<String> sentinelGetMasterAddrByName(String masterName) {
        return jedis().sentinelGetMasterAddrByName(masterName);
    }


    public String info(String section) {
        return jedis().info(section);
    }


    public Long sentinelReset(String pattern) {
        return jedis().sentinelReset(pattern);
    }


    public void monitor(JedisMonitor jedisMonitor) {
        jedis().monitor(jedisMonitor);
    }


    public List<Map<String, String>> sentinelSlaves(String masterName) {
        return jedis().sentinelSlaves(masterName);
    }


    public String slaveof(String host, int port) {
        return jedis().slaveof(host, port);
    }


    public String sentinelFailover(String masterName) {
        return jedis().sentinelFailover(masterName);
    }


    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        return jedis().sentinelMonitor(masterName, ip, port, quorum);
    }


    public String slaveofNoOne() {
        return jedis().slaveofNoOne();
    }


    public List<byte[]> configGet(byte[] pattern) {
        return jedis().configGet(pattern);
    }


    public String sentinelRemove(String masterName) {
        return jedis().sentinelRemove(masterName);
    }


    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        return jedis().sentinelSet(masterName, parameterMap);
    }


    public byte[] dump(String key) {
        return jedis().dump(key);
    }


    public String restore(String key, int ttl, byte[] serializedValue) {
        return jedis().restore(key, ttl, serializedValue);
    }


    public String configResetStat() {
        return jedis().configResetStat();
    }


    public Long pexpire(String key, int milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }


    public byte[] configSet(byte[] parameter, byte[] value) {
        return jedis().configSet(parameter, value);
    }


    public Long pexpire(String key, long milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }


    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return jedis().pexpireAt(key, millisecondsTimestamp);
    }


    public Long pttl(String key) {
        return jedis().pttl(key);
    }


    public String psetex(String key, int milliseconds, String value) {
        return jedis().psetex(key, milliseconds, value);
    }


    public String psetex(String key, long milliseconds, String value) {
        return jedis().psetex(key, milliseconds, value);
    }


    public String set(String key, String value, String nxxx) {
        return jedis().set(key, value, nxxx);
    }


    public String set(String key, String value, String nxxx, String expx, int time) {
        return jedis().set(key, value, nxxx, expx, time);
    }


    public boolean isConnected() {
        return jedis().isConnected();
    }


    public Long strlen(byte[] key) {
        return jedis().strlen(key);
    }


    public String clientKill(String client) {
        return jedis().clientKill(client);
    }


    public void sync() {
        jedis().sync();
    }


    public Long lpushx(byte[] key, byte[]... string) {
        return jedis().lpushx(key, string);
    }


    public String clientSetname(String name) {
        return jedis().clientSetname(name);
    }


    public Long persist(byte[] key) {
        return jedis().persist(key);
    }


    public String migrate(String host, int port, String key, int destinationDb, int timeout) {
        return jedis().migrate(host, port, key, destinationDb, timeout);
    }


    public ScanResult<String> scan(int cursor) {
        return jedis().scan(cursor);
    }


    public Long rpushx(byte[] key, byte[]... string) {
        return jedis().rpushx(key, string);
    }


    public byte[] echo(byte[] string) {
        return jedis().echo(string);
    }


    public ScanResult<String> scan(int cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }


    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        return jedis().linsert(key, where, pivot, value);
    }


    public String debug(DebugParams params) {
        return jedis().debug(params);
    }


    public Client getClient() {
        return jedis().getClient();
    }


    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        return jedis().brpoplpush(source, destination, timeout);
    }


    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        return jedis().hscan(key, cursor);
    }


    public Boolean setbit(byte[] key, long offset, boolean value) {
        return jedis().setbit(key, offset, value);
    }


    public ScanResult<Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }


    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return jedis().setbit(key, offset, value);
    }


    public Boolean getbit(byte[] key, long offset) {
        return jedis().getbit(key, offset);
    }


    public Long bitpos(byte[] key, boolean value) {
        return jedis().bitpos(key, value);
    }


    public Long bitpos(byte[] key, boolean value, BitPosParams params) {
        return jedis().bitpos(key, value, params);
    }


    public Long setrange(byte[] key, long offset, byte[] value) {
        return jedis().setrange(key, offset, value);
    }


    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return jedis().getrange(key, startOffset, endOffset);
    }


    public ScanResult<String> sscan(String key, int cursor) {
        return jedis().sscan(key, cursor);
    }


    public Long publish(byte[] channel, byte[] message) {
        return jedis().publish(channel, message);
    }


    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        jedis().subscribe(jedisPubSub, channels);
    }


    public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }


    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        jedis().psubscribe(jedisPubSub, patterns);
    }


    public Long getDB() {
        return jedis().getDB();
    }


    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return jedis().eval(script, keys, args);
    }


    public ScanResult<Tuple> zscan(String key, int cursor) {
        return jedis().zscan(key, cursor);
    }


    public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }


    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        return jedis().eval(script, keyCount, params);
    }


    public Object eval(byte[] script, int keyCount, byte[]... params) {
        return jedis().eval(script, keyCount, params);
    }


    public Object eval(byte[] script) {
        return jedis().eval(script);
    }


    public Object evalsha(byte[] sha1) {
        return jedis().evalsha(sha1);
    }


    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return jedis().evalsha(sha1, keys, args);
    }


    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        return jedis().evalsha(sha1, keyCount, params);
    }


    public ScanResult<String> scan(String cursor) {
        return jedis().scan(cursor);
    }


    public String scriptFlush() {
        return jedis().scriptFlush();
    }


    public ScanResult<String> scan(String cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }


    public Long scriptExists(byte[] sha1) {
        return jedis().scriptExists(sha1);
    }


    public List<Long> scriptExists(byte[]... sha1) {
        return jedis().scriptExists(sha1);
    }


    public byte[] scriptLoad(byte[] script) {
        return jedis().scriptLoad(script);
    }


    public String scriptKill() {
        return jedis().scriptKill();
    }


    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        return jedis().hscan(key, cursor);
    }


    public String slowlogReset() {
        return jedis().slowlogReset();
    }


    public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }


    public Long slowlogLen() {
        return jedis().slowlogLen();
    }


    public List<byte[]> slowlogGetBinary() {
        return jedis().slowlogGetBinary();
    }


    public List<byte[]> slowlogGetBinary(long entries) {
        return jedis().slowlogGetBinary(entries);
    }


    public Long objectRefcount(byte[] key) {
        return jedis().objectRefcount(key);
    }


    public byte[] objectEncoding(byte[] key) {
        return jedis().objectEncoding(key);
    }


    public Long objectIdletime(byte[] key) {
        return jedis().objectIdletime(key);
    }


    public Long bitcount(byte[] key) {
        return jedis().bitcount(key);
    }


    public ScanResult<String> sscan(String key, String cursor) {
        return jedis().sscan(key, cursor);
    }


    public Long bitcount(byte[] key, long start, long end) {
        return jedis().bitcount(key, start, end);
    }


    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }


    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return jedis().bitop(op, destKey, srcKeys);
    }


    public byte[] dump(byte[] key) {
        return jedis().dump(key);
    }


    public String restore(byte[] key, int ttl, byte[] serializedValue) {
        return jedis().restore(key, ttl, serializedValue);
    }


    public ScanResult<Tuple> zscan(String key, String cursor) {
        return jedis().zscan(key, cursor);
    }


    public Long pexpire(byte[] key, int milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }


    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }


    public Long pexpire(byte[] key, long milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }


    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return jedis().pexpireAt(key, millisecondsTimestamp);
    }


    public Long pttl(byte[] key) {
        return jedis().pttl(key);
    }


    public String psetex(byte[] key, int milliseconds, byte[] value) {
        return jedis().psetex(key, milliseconds, value);
    }


    public String clusterNodes() {
        return jedis().clusterNodes();
    }


    public String psetex(byte[] key, long milliseconds, byte[] value) {
        return jedis().psetex(key, milliseconds, value);
    }


    public String readonly() {
        return jedis().readonly();
    }


    public String clusterMeet(String ip, int port) {
        return jedis().clusterMeet(ip, port);
    }


    public String clusterReset(Reset resetType) {
        return jedis().clusterReset(resetType);
    }


    public String set(byte[] key, byte[] value, byte[] nxxx) {
        return jedis().set(key, value, nxxx);
    }


    public String clusterAddSlots(int... slots) {
        return jedis().clusterAddSlots(slots);
    }


    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        return jedis().set(key, value, nxxx, expx, time);
    }


    public String clusterDelSlots(int... slots) {
        return jedis().clusterDelSlots(slots);
    }


    public String clusterInfo() {
        return jedis().clusterInfo();
    }


    public String clientKill(byte[] client) {
        return jedis().clientKill(client);
    }


    public List<String> clusterGetKeysInSlot(int slot, int count) {
        return jedis().clusterGetKeysInSlot(slot, count);
    }


    public String clientGetname() {
        return jedis().clientGetname();
    }


    public String clientList() {
        return jedis().clientList();
    }


    public String clusterSetSlotNode(int slot, String nodeId) {
        return jedis().clusterSetSlotNode(slot, nodeId);
    }


    public String clientSetname(byte[] name) {
        return jedis().clientSetname(name);
    }


    public String clusterSetSlotMigrating(int slot, String nodeId) {
        return jedis().clusterSetSlotMigrating(slot, nodeId);
    }


    public List<String> time() {
        return jedis().time();
    }


    public String clusterSetSlotImporting(int slot, String nodeId) {
        return jedis().clusterSetSlotImporting(slot, nodeId);
    }


    public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        return jedis().migrate(host, port, key, destinationDb, timeout);
    }


    public String clusterSetSlotStable(int slot) {
        return jedis().clusterSetSlotStable(slot);
    }


    public Long waitReplicas(int replicas, long timeout) {
        return jedis().waitReplicas(replicas, timeout);
    }


    public String clusterForget(String nodeId) {
        return jedis().clusterForget(nodeId);
    }


    public String clusterFlushSlots() {
        return jedis().clusterFlushSlots();
    }


    public Long pfadd(byte[] key, byte[]... elements) {
        return jedis().pfadd(key, elements);
    }


    public Long clusterKeySlot(String key) {
        return jedis().clusterKeySlot(key);
    }


    public Long clusterCountKeysInSlot(int slot) {
        return jedis().clusterCountKeysInSlot(slot);
    }


    public long pfcount(byte[] key) {
        return jedis().pfcount(key);
    }


    public String clusterSaveConfig() {
        return jedis().clusterSaveConfig();
    }


    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return jedis().pfmerge(destkey, sourcekeys);
    }


    public String clusterReplicate(String nodeId) {
        return jedis().clusterReplicate(nodeId);
    }


    public Long pfcount(byte[]... keys) {
        return jedis().pfcount(keys);
    }


    public List<String> clusterSlaves(String nodeId) {
        return jedis().clusterSlaves(nodeId);
    }


    public ScanResult<byte[]> scan(byte[] cursor) {
        return jedis().scan(cursor);
    }


    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }


    public String clusterFailover() {
        return jedis().clusterFailover();
    }


    public List<Object> clusterSlots() {
        return jedis().clusterSlots();
    }


    public String asking() {
        return jedis().asking();
    }


    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return jedis().hscan(key, cursor);
    }


    public List<String> pubsubChannels(String pattern) {
        return jedis().pubsubChannels(pattern);
    }


    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }


    public Long pubsubNumPat() {
        return jedis().pubsubNumPat();
    }


    public Map<String, String> pubsubNumSub(String... channels) {
        return jedis().pubsubNumSub(channels);
    }


    public void close() {
        jedis().close();
    }


    public void setDataSource(Pool<Jedis> jedisPool) {
        jedis().setDataSource(jedisPool);
    }


    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return jedis().sscan(key, cursor);
    }


    public Long pfadd(String key, String... elements) {
        return jedis().pfadd(key, elements);
    }


    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }


    public long pfcount(String key) {
        return jedis().pfcount(key);
    }


    public long pfcount(String... keys) {
        return jedis().pfcount(keys);
    }


    public String pfmerge(String destkey, String... sourcekeys) {
        return jedis().pfmerge(destkey, sourcekeys);
    }


    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return jedis().zscan(key, cursor);
    }


    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }


    public List<String> blpop(int timeout, String key) {
        return jedis().blpop(timeout, key);
    }


    public List<String> brpop(int timeout, String key) {
        return jedis().brpop(timeout, key);
    }


    public Long geoadd(String key, double longitude, double latitude, String member) {
        return jedis().geoadd(key, longitude, latitude, member);
    }


    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return jedis().geoadd(key, memberCoordinateMap);
    }


    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return jedis().geoadd(key, longitude, latitude, member);
    }


    public Double geodist(String key, String member1, String member2) {
        return jedis().geodist(key, member1, member2);
    }


    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return jedis().geoadd(key, memberCoordinateMap);
    }


    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return jedis().geodist(key, member1, member2, unit);
    }


    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return jedis().geodist(key, member1, member2);
    }


    public List<String> geohash(String key, String... members) {
        return jedis().geohash(key, members);
    }


    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return jedis().geodist(key, member1, member2, unit);
    }


    public List<GeoCoordinate> geopos(String key, String... members) {
        return jedis().geopos(key, members);
    }


    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return jedis().geohash(key, members);
    }


    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedis().georadius(key, longitude, latitude, radius, unit);
    }


    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return jedis().geopos(key, members);
    }


    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedis().georadius(key, longitude, latitude, radius, unit, param);
    }


    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedis().georadius(key, longitude, latitude, radius, unit);
    }


    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedis().georadiusByMember(key, member, radius, unit);
    }


    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedis().georadius(key, longitude, latitude, radius, unit, param);
    }


    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedis().georadiusByMember(key, member, radius, unit, param);
    }


    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedis().georadiusByMember(key, member, radius, unit);
    }


    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedis().georadiusByMember(key, member, radius, unit, param);
    }

}
