package cn.wizzer.common.services.redis;

import static cn.wizzer.common.services.redis.RedisInterceptor.jedis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Client;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster.Reset;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.Slowlog;

/**
 * Created by Wizzer on 2016/7/31.
 */
@SuppressWarnings("deprecation")
@IocBean
public class RedisService extends Jedis {

    @Aop("redis")
    public int hashCode() {
        return jedis().hashCode();
    }

    @Aop("redis")
    public String set(String key, String value) {
        return jedis().set(key, value);
    }

    @Aop("redis")
    public String set(String key, String value, String nxxx, String expx, long time) {
        return jedis().set(key, value, nxxx, expx, time);
    }

    @Aop("redis")
    public String get(String key) {
        return jedis().get(key);
    }

    @Aop("redis")
    public Long exists(String... keys) {
        return jedis().exists(keys);
    }

    @Aop("redis")
    public String ping() {
        return jedis().ping();
    }

    @Aop("redis")
    public String set(byte[] key, byte[] value) {
        return jedis().set(key, value);
    }

    @Aop("redis")
    public boolean equals(Object obj) {
        return jedis().equals(obj);
    }

    @Aop("redis")
    public Boolean exists(String key) {
        return jedis().exists(key);
    }

    @Aop("redis")
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        return jedis().set(key, value, nxxx, expx, time);
    }

    @Aop("redis")
    public Long del(String... keys) {
        return jedis().del(keys);
    }

    @Aop("redis")
    public Long del(String key) {
        return jedis().del(key);
    }

    @Aop("redis")
    public byte[] get(byte[] key) {
        return jedis().get(key);
    }

    @Aop("redis")
    public String type(String key) {
        return jedis().type(key);
    }

    @Aop("redis")
    public String quit() {
        return jedis().quit();
    }

    @Aop("redis")
    public Long exists(byte[]... keys) {
        return jedis().exists(keys);
    }

    @Aop("redis")
    public Set<String> keys(String pattern) {
        return jedis().keys(pattern);
    }

    @Aop("redis")
    public Boolean exists(byte[] key) {
        return jedis().exists(key);
    }

    @Aop("redis")
    public Long del(byte[]... keys) {
        return jedis().del(keys);
    }

    @Aop("redis")
    public Long del(byte[] key) {
        return jedis().del(key);
    }

    @Aop("redis")
    public String type(byte[] key) {
        return jedis().type(key);
    }

    @Aop("redis")
    public String randomKey() {
        return jedis().randomKey();
    }

    @Aop("redis")
    public String rename(String oldkey, String newkey) {
        return jedis().rename(oldkey, newkey);
    }

    @Aop("redis")
    public String flushDB() {
        return jedis().flushDB();
    }

    @Aop("redis")
    public Long renamenx(String oldkey, String newkey) {
        return jedis().renamenx(oldkey, newkey);
    }

    @Aop("redis")
    public Set<byte[]> keys(byte[] pattern) {
        return jedis().keys(pattern);
    }

    @Aop("redis")
    public Long expire(String key, int seconds) {
        return jedis().expire(key, seconds);
    }

    @Aop("redis")
    public byte[] randomBinaryKey() {
        return jedis().randomBinaryKey();
    }

    @Aop("redis")
    public String toString() {
        return jedis().toString();
    }

    @Aop("redis")
    public String rename(byte[] oldkey, byte[] newkey) {
        return jedis().rename(oldkey, newkey);
    }

    @Aop("redis")
    public Long expireAt(String key, long unixTime) {
        return jedis().expireAt(key, unixTime);
    }

    @Aop("redis")
    public Long renamenx(byte[] oldkey, byte[] newkey) {
        return jedis().renamenx(oldkey, newkey);
    }

    @Aop("redis")
    public Long dbSize() {
        return jedis().dbSize();
    }

    @Aop("redis")
    public Long expire(byte[] key, int seconds) {
        return jedis().expire(key, seconds);
    }

    @Aop("redis")
    public Long ttl(String key) {
        return jedis().ttl(key);
    }

    @Aop("redis")
    public Long move(String key, int dbIndex) {
        return jedis().move(key, dbIndex);
    }

    @Aop("redis")
    public String getSet(String key, String value) {
        return jedis().getSet(key, value);
    }

    @Aop("redis")
    public List<String> mget(String... keys) {
        return jedis().mget(keys);
    }

    @Aop("redis")
    public Long setnx(String key, String value) {
        return jedis().setnx(key, value);
    }

    @Aop("redis")
    public Long expireAt(byte[] key, long unixTime) {
        return jedis().expireAt(key, unixTime);
    }

    @Aop("redis")
    public String setex(String key, int seconds, String value) {
        return jedis().setex(key, seconds, value);
    }

    @Aop("redis")
    public String mset(String... keysvalues) {
        return jedis().mset(keysvalues);
    }

    @Aop("redis")
    public Long ttl(byte[] key) {
        return jedis().ttl(key);
    }

    @Aop("redis")
    public Long msetnx(String... keysvalues) {
        return jedis().msetnx(keysvalues);
    }

    @Aop("redis")
    public String select(int index) {
        return jedis().select(index);
    }

    @Aop("redis")
    public Long move(byte[] key, int dbIndex) {
        return jedis().move(key, dbIndex);
    }

    @Aop("redis")
    public Long decrBy(String key, long integer) {
        return jedis().decrBy(key, integer);
    }

    @Aop("redis")
    public String flushAll() {
        return jedis().flushAll();
    }

    @Aop("redis")
    public byte[] getSet(byte[] key, byte[] value) {
        return jedis().getSet(key, value);
    }

    @Aop("redis")
    public Long decr(String key) {
        return jedis().decr(key);
    }

    @Aop("redis")
    public List<byte[]> mget(byte[]... keys) {
        return jedis().mget(keys);
    }

    @Aop("redis")
    public Long incrBy(String key, long integer) {
        return jedis().incrBy(key, integer);
    }

    @Aop("redis")
    public Long setnx(byte[] key, byte[] value) {
        return jedis().setnx(key, value);
    }

    @Aop("redis")
    public String setex(byte[] key, int seconds, byte[] value) {
        return jedis().setex(key, seconds, value);
    }

    @Aop("redis")
    public Double incrByFloat(String key, double value) {
        return jedis().incrByFloat(key, value);
    }

    @Aop("redis")
    public String mset(byte[]... keysvalues) {
        return jedis().mset(keysvalues);
    }

    @Aop("redis")
    public Long incr(String key) {
        return jedis().incr(key);
    }

    @Aop("redis")
    public Long msetnx(byte[]... keysvalues) {
        return jedis().msetnx(keysvalues);
    }

    @Aop("redis")
    public Long append(String key, String value) {
        return jedis().append(key, value);
    }

    @Aop("redis")
    public Long decrBy(byte[] key, long integer) {
        return jedis().decrBy(key, integer);
    }

    @Aop("redis")
    public String substr(String key, int start, int end) {
        return jedis().substr(key, start, end);
    }

    @Aop("redis")
    public Long decr(byte[] key) {
        return jedis().decr(key);
    }

    @Aop("redis")
    public Long hset(String key, String field, String value) {
        return jedis().hset(key, field, value);
    }

    @Aop("redis")
    public Long incrBy(byte[] key, long integer) {
        return jedis().incrBy(key, integer);
    }

    @Aop("redis")
    public String hget(String key, String field) {
        return jedis().hget(key, field);
    }

    @Aop("redis")
    public Long hsetnx(String key, String field, String value) {
        return jedis().hsetnx(key, field, value);
    }

    @Aop("redis")
    public Double incrByFloat(byte[] key, double integer) {
        return jedis().incrByFloat(key, integer);
    }

    @Aop("redis")
    public String hmset(String key, Map<String, String> hash) {
        return jedis().hmset(key, hash);
    }

    @Aop("redis")
    public List<String> hmget(String key, String... fields) {
        return jedis().hmget(key, fields);
    }

    @Aop("redis")
    public Long incr(byte[] key) {
        return jedis().incr(key);
    }

    @Aop("redis")
    public Long hincrBy(String key, String field, long value) {
        return jedis().hincrBy(key, field, value);
    }

    @Aop("redis")
    public Long append(byte[] key, byte[] value) {
        return jedis().append(key, value);
    }

    @Aop("redis")
    public Double hincrByFloat(String key, String field, double value) {
        return jedis().hincrByFloat(key, field, value);
    }

    @Aop("redis")
    public byte[] substr(byte[] key, int start, int end) {
        return jedis().substr(key, start, end);
    }

    @Aop("redis")
    public Boolean hexists(String key, String field) {
        return jedis().hexists(key, field);
    }

    @Aop("redis")
    public Long hdel(String key, String... fields) {
        return jedis().hdel(key, fields);
    }

    @Aop("redis")
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return jedis().hset(key, field, value);
    }

    @Aop("redis")
    public Long hlen(String key) {
        return jedis().hlen(key);
    }

    @Aop("redis")
    public byte[] hget(byte[] key, byte[] field) {
        return jedis().hget(key, field);
    }

    @Aop("redis")
    public Set<String> hkeys(String key) {
        return jedis().hkeys(key);
    }

    @Aop("redis")
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return jedis().hsetnx(key, field, value);
    }

    @Aop("redis")
    public List<String> hvals(String key) {
        return jedis().hvals(key);
    }

    @Aop("redis")
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return jedis().hmset(key, hash);
    }

    @Aop("redis")
    public Map<String, String> hgetAll(String key) {
        return jedis().hgetAll(key);
    }

    @Aop("redis")
    public Long rpush(String key, String... strings) {
        return jedis().rpush(key, strings);
    }

    @Aop("redis")
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return jedis().hmget(key, fields);
    }

    @Aop("redis")
    public Long lpush(String key, String... strings) {
        return jedis().lpush(key, strings);
    }

    @Aop("redis")
    public Long hincrBy(byte[] key, byte[] field, long value) {
        return jedis().hincrBy(key, field, value);
    }

    @Aop("redis")
    public Long llen(String key) {
        return jedis().llen(key);
    }

    @Aop("redis")
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return jedis().hincrByFloat(key, field, value);
    }

    @Aop("redis")
    public List<String> lrange(String key, long start, long end) {
        return jedis().lrange(key, start, end);
    }

    @Aop("redis")
    public Boolean hexists(byte[] key, byte[] field) {
        return jedis().hexists(key, field);
    }

    @Aop("redis")
    public Long hdel(byte[] key, byte[]... fields) {
        return jedis().hdel(key, fields);
    }

    @Aop("redis")
    public String ltrim(String key, long start, long end) {
        return jedis().ltrim(key, start, end);
    }

    @Aop("redis")
    public Long hlen(byte[] key) {
        return jedis().hlen(key);
    }

    @Aop("redis")
    public Set<byte[]> hkeys(byte[] key) {
        return jedis().hkeys(key);
    }

    @Aop("redis")
    public List<byte[]> hvals(byte[] key) {
        return jedis().hvals(key);
    }

    @Aop("redis")
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return jedis().hgetAll(key);
    }

    @Aop("redis")
    public String lindex(String key, long index) {
        return jedis().lindex(key, index);
    }

    @Aop("redis")
    public Long rpush(byte[] key, byte[]... strings) {
        return jedis().rpush(key, strings);
    }

    @Aop("redis")
    public Long lpush(byte[] key, byte[]... strings) {
        return jedis().lpush(key, strings);
    }

    @Aop("redis")
    public String lset(String key, long index, String value) {
        return jedis().lset(key, index, value);
    }

    @Aop("redis")
    public Long llen(byte[] key) {
        return jedis().llen(key);
    }

    @Aop("redis")
    public Long lrem(String key, long count, String value) {
        return jedis().lrem(key, count, value);
    }

    @Aop("redis")
    public List<byte[]> lrange(byte[] key, long start, long end) {
        return jedis().lrange(key, start, end);
    }

    @Aop("redis")
    public String lpop(String key) {
        return jedis().lpop(key);
    }

    @Aop("redis")
    public String rpop(String key) {
        return jedis().rpop(key);
    }

    @Aop("redis")
    public String ltrim(byte[] key, long start, long end) {
        return jedis().ltrim(key, start, end);
    }

    @Aop("redis")
    public String rpoplpush(String srckey, String dstkey) {
        return jedis().rpoplpush(srckey, dstkey);
    }

    @Aop("redis")
    public Long sadd(String key, String... members) {
        return jedis().sadd(key, members);
    }

    @Aop("redis")
    public byte[] lindex(byte[] key, long index) {
        return jedis().lindex(key, index);
    }

    @Aop("redis")
    public Set<String> smembers(String key) {
        return jedis().smembers(key);
    }

    @Aop("redis")
    public Long srem(String key, String... members) {
        return jedis().srem(key, members);
    }

    @Aop("redis")
    public String lset(byte[] key, long index, byte[] value) {
        return jedis().lset(key, index, value);
    }

    @Aop("redis")
    public String spop(String key) {
        return jedis().spop(key);
    }

    @Aop("redis")
    public Set<String> spop(String key, long count) {
        return jedis().spop(key, count);
    }

    @Aop("redis")
    public Long lrem(byte[] key, long count, byte[] value) {
        return jedis().lrem(key, count, value);
    }

    @Aop("redis")
    public Long smove(String srckey, String dstkey, String member) {
        return jedis().smove(srckey, dstkey, member);
    }

    @Aop("redis")
    public byte[] lpop(byte[] key) {
        return jedis().lpop(key);
    }

    @Aop("redis")
    public Long scard(String key) {
        return jedis().scard(key);
    }

    @Aop("redis")
    public byte[] rpop(byte[] key) {
        return jedis().rpop(key);
    }

    @Aop("redis")
    public Boolean sismember(String key, String member) {
        return jedis().sismember(key, member);
    }

    @Aop("redis")
    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        return jedis().rpoplpush(srckey, dstkey);
    }

    @Aop("redis")
    public Set<String> sinter(String... keys) {
        return jedis().sinter(keys);
    }

    @Aop("redis")
    public Long sadd(byte[] key, byte[]... members) {
        return jedis().sadd(key, members);
    }

    @Aop("redis")
    public Long sinterstore(String dstkey, String... keys) {
        return jedis().sinterstore(dstkey, keys);
    }

    @Aop("redis")
    public Set<byte[]> smembers(byte[] key) {
        return jedis().smembers(key);
    }

    @Aop("redis")
    public Set<String> sunion(String... keys) {
        return jedis().sunion(keys);
    }

    @Aop("redis")
    public Long srem(byte[] key, byte[]... member) {
        return jedis().srem(key, member);
    }

    @Aop("redis")
    public byte[] spop(byte[] key) {
        return jedis().spop(key);
    }

    @Aop("redis")
    public Long sunionstore(String dstkey, String... keys) {
        return jedis().sunionstore(dstkey, keys);
    }

    @Aop("redis")
    public Set<byte[]> spop(byte[] key, long count) {
        return jedis().spop(key, count);
    }

    @Aop("redis")
    public Set<String> sdiff(String... keys) {
        return jedis().sdiff(keys);
    }

    @Aop("redis")
    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return jedis().smove(srckey, dstkey, member);
    }

    @Aop("redis")
    public Long sdiffstore(String dstkey, String... keys) {
        return jedis().sdiffstore(dstkey, keys);
    }

    @Aop("redis")
    public String srandmember(String key) {
        return jedis().srandmember(key);
    }

    @Aop("redis")
    public Long scard(byte[] key) {
        return jedis().scard(key);
    }

    @Aop("redis")
    public Boolean sismember(byte[] key, byte[] member) {
        return jedis().sismember(key, member);
    }

    @Aop("redis")
    public List<String> srandmember(String key, int count) {
        return jedis().srandmember(key, count);
    }

    @Aop("redis")
    public Long zadd(String key, double score, String member) {
        return jedis().zadd(key, score, member);
    }

    @Aop("redis")
    public Set<byte[]> sinter(byte[]... keys) {
        return jedis().sinter(keys);
    }

    @Aop("redis")
    public Long zadd(String key, double score, String member, ZAddParams params) {
        return jedis().zadd(key, score, member, params);
    }

    @Aop("redis")
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return jedis().zadd(key, scoreMembers);
    }

    @Aop("redis")
    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        return jedis().sinterstore(dstkey, keys);
    }

    @Aop("redis")
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return jedis().zadd(key, scoreMembers, params);
    }

    @Aop("redis")
    public Set<String> zrange(String key, long start, long end) {
        return jedis().zrange(key, start, end);
    }

    @Aop("redis")
    public Set<byte[]> sunion(byte[]... keys) {
        return jedis().sunion(keys);
    }

    @Aop("redis")
    public Long zrem(String key, String... members) {
        return jedis().zrem(key, members);
    }

    @Aop("redis")
    public Double zincrby(String key, double score, String member) {
        return jedis().zincrby(key, score, member);
    }

    @Aop("redis")
    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        return jedis().sunionstore(dstkey, keys);
    }

    @Aop("redis")
    public Set<byte[]> sdiff(byte[]... keys) {
        return jedis().sdiff(keys);
    }

    @Aop("redis")
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return jedis().zincrby(key, score, member, params);
    }

    @Aop("redis")
    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        return jedis().sdiffstore(dstkey, keys);
    }

    @Aop("redis")
    public Long zrank(String key, String member) {
        return jedis().zrank(key, member);
    }

    @Aop("redis")
    public byte[] srandmember(byte[] key) {
        return jedis().srandmember(key);
    }

    @Aop("redis")
    public List<byte[]> srandmember(byte[] key, int count) {
        return jedis().srandmember(key, count);
    }

    @Aop("redis")
    public Long zrevrank(String key, String member) {
        return jedis().zrevrank(key, member);
    }

    @Aop("redis")
    public Long zadd(byte[] key, double score, byte[] member) {
        return jedis().zadd(key, score, member);
    }

    @Aop("redis")
    public Set<String> zrevrange(String key, long start, long end) {
        return jedis().zrevrange(key, start, end);
    }

    @Aop("redis")
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return jedis().zrangeWithScores(key, start, end);
    }

    @Aop("redis")
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return jedis().zadd(key, score, member, params);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return jedis().zrevrangeWithScores(key, start, end);
    }

    @Aop("redis")
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return jedis().zadd(key, scoreMembers);
    }

    @Aop("redis")
    public Long zcard(String key) {
        return jedis().zcard(key);
    }

    @Aop("redis")
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return jedis().zadd(key, scoreMembers, params);
    }

    @Aop("redis")
    public Set<byte[]> zrange(byte[] key, long start, long end) {
        return jedis().zrange(key, start, end);
    }

    @Aop("redis")
    public Double zscore(String key, String member) {
        return jedis().zscore(key, member);
    }

    @Aop("redis")
    public Long zrem(byte[] key, byte[]... members) {
        return jedis().zrem(key, members);
    }

    @Aop("redis")
    public String watch(String... keys) {
        return jedis().watch(keys);
    }

    @Aop("redis")
    public List<String> sort(String key) {
        return jedis().sort(key);
    }

    @Aop("redis")
    public Double zincrby(byte[] key, double score, byte[] member) {
        return jedis().zincrby(key, score, member);
    }

    @Aop("redis")
    public List<String> sort(String key, SortingParams sortingParameters) {
        return jedis().sort(key, sortingParameters);
    }

    @Aop("redis")
    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return jedis().zincrby(key, score, member, params);
    }

    @Aop("redis")
    public Long zrank(byte[] key, byte[] member) {
        return jedis().zrank(key, member);
    }

    @Aop("redis")
    public List<String> blpop(int timeout, String... keys) {
        return jedis().blpop(timeout, keys);
    }

    @Aop("redis")
    public Long zrevrank(byte[] key, byte[] member) {
        return jedis().zrevrank(key, member);
    }

    @Aop("redis")
    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        return jedis().zrevrange(key, start, end);
    }

    @Aop("redis")
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return jedis().zrangeWithScores(key, start, end);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        return jedis().zrevrangeWithScores(key, start, end);
    }

    @Aop("redis")
    public Long zcard(byte[] key) {
        return jedis().zcard(key);
    }

    @Aop("redis")
    public Double zscore(byte[] key, byte[] member) {
        return jedis().zscore(key, member);
    }

    @Aop("redis")
    public Transaction multi() {
        return jedis().multi();
    }

    @Aop("redis")
    public List<Object> multi(TransactionBlock jedisTransaction) {
        return jedis().multi(jedisTransaction);
    }

    @Aop("redis")
    public void connect() {
        jedis().connect();
    }

    @Aop("redis")
    public void disconnect() {
        jedis().disconnect();
    }

    @Aop("redis")
    public void resetState() {
        jedis().resetState();
    }

    @Aop("redis")
    public List<String> blpop(String... args) {
        return jedis().blpop(args);
    }

    @Aop("redis")
    public List<String> brpop(String... args) {
        return jedis().brpop(args);
    }

    @Aop("redis")
    public String watch(byte[]... keys) {
        return jedis().watch(keys);
    }

    @Aop("redis")
    public String unwatch() {
        return jedis().unwatch();
    }

    @Aop("redis")
    public List<String> blpop(String arg) {
        return jedis().blpop(arg);
    }

    @Aop("redis")
    public List<byte[]> sort(byte[] key) {
        return jedis().sort(key);
    }

    @Aop("redis")
    public List<String> brpop(String arg) {
        return jedis().brpop(arg);
    }

    @Aop("redis")
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return jedis().sort(key, sortingParameters, dstkey);
    }

    @Aop("redis")
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return jedis().sort(key, sortingParameters);
    }

    @Aop("redis")
    public Long sort(String key, String dstkey) {
        return jedis().sort(key, dstkey);
    }

    @Aop("redis")
    public List<String> brpop(int timeout, String... keys) {
        return jedis().brpop(timeout, keys);
    }

    @Aop("redis")
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return jedis().blpop(timeout, keys);
    }

    @Aop("redis")
    public Long zcount(String key, double min, double max) {
        return jedis().zcount(key, min, max);
    }

    @Aop("redis")
    public Long zcount(String key, String min, String max) {
        return jedis().zcount(key, min, max);
    }

    @Aop("redis")
    public Set<String> zrangeByScore(String key, double min, double max) {
        return jedis().zrangeByScore(key, min, max);
    }

    @Aop("redis")
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return jedis().sort(key, sortingParameters, dstkey);
    }

    @Aop("redis")
    public Long sort(byte[] key, byte[] dstkey) {
        return jedis().sort(key, dstkey);
    }

    @Aop("redis")
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return jedis().brpop(timeout, keys);
    }

    @Aop("redis")
    public Set<String> zrangeByScore(String key, String min, String max) {
        return jedis().zrangeByScore(key, min, max);
    }

    @Aop("redis")
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }

    @Aop("redis")
    public List<byte[]> blpop(byte[] arg) {
        return jedis().blpop(arg);
    }

    @Aop("redis")
    public List<byte[]> brpop(byte[] arg) {
        return jedis().brpop(arg);
    }

    @Aop("redis")
    public List<byte[]> blpop(byte[]... args) {
        return jedis().blpop(args);
    }

    @Aop("redis")
    public List<byte[]> brpop(byte[]... args) {
        return jedis().brpop(args);
    }

    @Aop("redis")
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }

    @Aop("redis")
    public String auth(String password) {
        return jedis().auth(password);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }

    @Aop("redis")
    public List<Object> pipelined(PipelineBlock jedisPipeline) {
        return jedis().pipelined(jedisPipeline);
    }

    @Aop("redis")
    public Pipeline pipelined() {
        return jedis().pipelined();
    }

    @Aop("redis")
    public Long zcount(byte[] key, double min, double max) {
        return jedis().zcount(key, min, max);
    }

    @Aop("redis")
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return jedis().zcount(key, min, max);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return jedis().zrangeByScore(key, min, max);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByScore(key, min, max);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(String key,
                                              String min,
                                              String max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Aop("redis")
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return jedis().zrevrangeByScore(key, max, min);
    }

    @Aop("redis")
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return jedis().zrevrangeByScore(key, max, min);
    }

    @Aop("redis")
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedis().zrangeByScore(key, min, max, offset, count);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(String key,
                                                 String max,
                                                 String min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }

    @Aop("redis")
    public Long zremrangeByRank(String key, long start, long end) {
        return jedis().zremrangeByRank(key, start, end);
    }

    @Aop("redis")
    public Long zremrangeByScore(String key, double start, double end) {
        return jedis().zremrangeByScore(key, start, end);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByScoreWithScores(key, min, max);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              double min,
                                              double max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Aop("redis")
    public Long zremrangeByScore(String key, String start, String end) {
        return jedis().zremrangeByScore(key, start, end);
    }

    @Aop("redis")
    public Long zunionstore(String dstkey, String... sets) {
        return jedis().zunionstore(dstkey, sets);
    }

    @Aop("redis")
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return jedis().zunionstore(dstkey, params, sets);
    }

    @Aop("redis")
    public Set<Tuple> zrangeByScoreWithScores(byte[] key,
                                              byte[] min,
                                              byte[] max,
                                              int offset,
                                              int count) {
        return jedis().zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return jedis().zrevrangeByScore(key, max, min);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByScore(key, max, min);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedis().zrevrangeByScore(key, max, min, offset, count);
    }

    @Aop("redis")
    public Long zinterstore(String dstkey, String... sets) {
        return jedis().zinterstore(dstkey, sets);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 double max,
                                                 double min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByScoreWithScores(key, max, min);
    }

    @Aop("redis")
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key,
                                                 byte[] max,
                                                 byte[] min,
                                                 int offset,
                                                 int count) {
        return jedis().zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Aop("redis")
    public Long zremrangeByRank(byte[] key, long start, long end) {
        return jedis().zremrangeByRank(key, start, end);
    }

    @Aop("redis")
    public Long zremrangeByScore(byte[] key, double start, double end) {
        return jedis().zremrangeByScore(key, start, end);
    }

    @Aop("redis")
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return jedis().zinterstore(dstkey, params, sets);
    }

    @Aop("redis")
    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return jedis().zremrangeByScore(key, start, end);
    }

    @Aop("redis")
    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        return jedis().zunionstore(dstkey, sets);
    }

    @Aop("redis")
    public Long zlexcount(String key, String min, String max) {
        return jedis().zlexcount(key, min, max);
    }

    @Aop("redis")
    public Set<String> zrangeByLex(String key, String min, String max) {
        return jedis().zrangeByLex(key, min, max);
    }

    @Aop("redis")
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return jedis().zrangeByLex(key, min, max, offset, count);
    }

    @Aop("redis")
    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedis().zunionstore(dstkey, params, sets);
    }

    @Aop("redis")
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return jedis().zrevrangeByLex(key, max, min);
    }

    @Aop("redis")
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return jedis().zrevrangeByLex(key, max, min, offset, count);
    }

    @Aop("redis")
    public Long zremrangeByLex(String key, String min, String max) {
        return jedis().zremrangeByLex(key, min, max);
    }

    @Aop("redis")
    public Long strlen(String key) {
        return jedis().strlen(key);
    }

    @Aop("redis")
    public Long lpushx(String key, String... string) {
        return jedis().lpushx(key, string);
    }

    @Aop("redis")
    public Long persist(String key) {
        return jedis().persist(key);
    }

    @Aop("redis")
    public Long rpushx(String key, String... string) {
        return jedis().rpushx(key, string);
    }

    @Aop("redis")
    public String echo(String string) {
        return jedis().echo(string);
    }

    @Aop("redis")
    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        return jedis().zinterstore(dstkey, sets);
    }

    @Aop("redis")
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        return jedis().linsert(key, where, pivot, value);
    }

    @Aop("redis")
    public String brpoplpush(String source, String destination, int timeout) {
        return jedis().brpoplpush(source, destination, timeout);
    }

    @Aop("redis")
    public Boolean setbit(String key, long offset, boolean value) {
        return jedis().setbit(key, offset, value);
    }

    @Aop("redis")
    public Boolean setbit(String key, long offset, String value) {
        return jedis().setbit(key, offset, value);
    }

    @Aop("redis")
    public Boolean getbit(String key, long offset) {
        return jedis().getbit(key, offset);
    }

    @Aop("redis")
    public Long setrange(String key, long offset, String value) {
        return jedis().setrange(key, offset, value);
    }

    @Aop("redis")
    public String getrange(String key, long startOffset, long endOffset) {
        return jedis().getrange(key, startOffset, endOffset);
    }

    @Aop("redis")
    public Long bitpos(String key, boolean value) {
        return jedis().bitpos(key, value);
    }

    @Aop("redis")
    public Long bitpos(String key, boolean value, BitPosParams params) {
        return jedis().bitpos(key, value, params);
    }

    @Aop("redis")
    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return jedis().zinterstore(dstkey, params, sets);
    }

    @Aop("redis")
    public List<String> configGet(String pattern) {
        return jedis().configGet(pattern);
    }

    @Aop("redis")
    public String configSet(String parameter, String value) {
        return jedis().configSet(parameter, value);
    }

    @Aop("redis")
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return jedis().zlexcount(key, min, max);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedis().zrangeByLex(key, min, max);
    }

    @Aop("redis")
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return jedis().zrangeByLex(key, min, max, offset, count);
    }

    @Aop("redis")
    public Object eval(String script, int keyCount, String... params) {
        return jedis().eval(script, keyCount, params);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return jedis().zrevrangeByLex(key, max, min);
    }

    @Aop("redis")
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        jedis().subscribe(jedisPubSub, channels);
    }

    @Aop("redis")
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return jedis().zrevrangeByLex(key, max, min, offset, count);
    }

    @Aop("redis")
    public Long publish(String channel, String message) {
        return jedis().publish(channel, message);
    }

    @Aop("redis")
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return jedis().zremrangeByLex(key, min, max);
    }

    @Aop("redis")
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        jedis().psubscribe(jedisPubSub, patterns);
    }

    @Aop("redis")
    public String save() {
        return jedis().save();
    }

    @Aop("redis")
    public Object eval(String script, List<String> keys, List<String> args) {
        return jedis().eval(script, keys, args);
    }

    @Aop("redis")
    public Object eval(String script) {
        return jedis().eval(script);
    }

    @Aop("redis")
    public String bgsave() {
        return jedis().bgsave();
    }

    @Aop("redis")
    public Object evalsha(String script) {
        return jedis().evalsha(script);
    }

    @Aop("redis")
    public String bgrewriteaof() {
        return jedis().bgrewriteaof();
    }

    @Aop("redis")
    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return jedis().evalsha(sha1, keys, args);
    }

    @Aop("redis")
    public Object evalsha(String sha1, int keyCount, String... params) {
        return jedis().evalsha(sha1, keyCount, params);
    }

    @Aop("redis")
    public Boolean scriptExists(String sha1) {
        return jedis().scriptExists(sha1);
    }

    @Aop("redis")
    public List<Boolean> scriptExists(String... sha1) {
        return jedis().scriptExists(sha1);
    }

    @Aop("redis")
    public String scriptLoad(String script) {
        return jedis().scriptLoad(script);
    }

    @Aop("redis")
    public Long lastsave() {
        return jedis().lastsave();
    }

    @Aop("redis")
    public List<Slowlog> slowlogGet() {
        return jedis().slowlogGet();
    }

    @Aop("redis")
    public List<Slowlog> slowlogGet(long entries) {
        return jedis().slowlogGet(entries);
    }

    @Aop("redis")
    public Long objectRefcount(String string) {
        return jedis().objectRefcount(string);
    }

    @Aop("redis")
    public String objectEncoding(String string) {
        return jedis().objectEncoding(string);
    }

    @Aop("redis")
    public String shutdown() {
        return jedis().shutdown();
    }

    @Aop("redis")
    public Long objectIdletime(String string) {
        return jedis().objectIdletime(string);
    }

    @Aop("redis")
    public Long bitcount(String key) {
        return jedis().bitcount(key);
    }

    @Aop("redis")
    public Long bitcount(String key, long start, long end) {
        return jedis().bitcount(key, start, end);
    }

    @Aop("redis")
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return jedis().bitop(op, destKey, srcKeys);
    }

    @Aop("redis")
    public List<Map<String, String>> sentinelMasters() {
        return jedis().sentinelMasters();
    }

    @Aop("redis")
    public String info() {
        return jedis().info();
    }

    @Aop("redis")
    public List<String> sentinelGetMasterAddrByName(String masterName) {
        return jedis().sentinelGetMasterAddrByName(masterName);
    }

    @Aop("redis")
    public String info(String section) {
        return jedis().info(section);
    }

    @Aop("redis")
    public Long sentinelReset(String pattern) {
        return jedis().sentinelReset(pattern);
    }

    @Aop("redis")
    public void monitor(JedisMonitor jedisMonitor) {
        jedis().monitor(jedisMonitor);
    }

    @Aop("redis")
    public List<Map<String, String>> sentinelSlaves(String masterName) {
        return jedis().sentinelSlaves(masterName);
    }

    @Aop("redis")
    public String slaveof(String host, int port) {
        return jedis().slaveof(host, port);
    }

    @Aop("redis")
    public String sentinelFailover(String masterName) {
        return jedis().sentinelFailover(masterName);
    }

    @Aop("redis")
    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        return jedis().sentinelMonitor(masterName, ip, port, quorum);
    }

    @Aop("redis")
    public String slaveofNoOne() {
        return jedis().slaveofNoOne();
    }

    @Aop("redis")
    public List<byte[]> configGet(byte[] pattern) {
        return jedis().configGet(pattern);
    }

    @Aop("redis")
    public String sentinelRemove(String masterName) {
        return jedis().sentinelRemove(masterName);
    }

    @Aop("redis")
    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        return jedis().sentinelSet(masterName, parameterMap);
    }

    @Aop("redis")
    public byte[] dump(String key) {
        return jedis().dump(key);
    }

    @Aop("redis")
    public String restore(String key, int ttl, byte[] serializedValue) {
        return jedis().restore(key, ttl, serializedValue);
    }

    @Aop("redis")
    public String configResetStat() {
        return jedis().configResetStat();
    }

    @Aop("redis")
    public Long pexpire(String key, int milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }

    @Aop("redis")
    public byte[] configSet(byte[] parameter, byte[] value) {
        return jedis().configSet(parameter, value);
    }

    @Aop("redis")
    public Long pexpire(String key, long milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }

    @Aop("redis")
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return jedis().pexpireAt(key, millisecondsTimestamp);
    }

    @Aop("redis")
    public Long pttl(String key) {
        return jedis().pttl(key);
    }

    @Aop("redis")
    public String psetex(String key, int milliseconds, String value) {
        return jedis().psetex(key, milliseconds, value);
    }

    @Aop("redis")
    public String psetex(String key, long milliseconds, String value) {
        return jedis().psetex(key, milliseconds, value);
    }

    @Aop("redis")
    public String set(String key, String value, String nxxx) {
        return jedis().set(key, value, nxxx);
    }

    @Aop("redis")
    public String set(String key, String value, String nxxx, String expx, int time) {
        return jedis().set(key, value, nxxx, expx, time);
    }

    @Aop("redis")
    public boolean isConnected() {
        return jedis().isConnected();
    }

    @Aop("redis")
    public Long strlen(byte[] key) {
        return jedis().strlen(key);
    }

    @Aop("redis")
    public String clientKill(String client) {
        return jedis().clientKill(client);
    }

    @Aop("redis")
    public void sync() {
        jedis().sync();
    }

    @Aop("redis")
    public Long lpushx(byte[] key, byte[]... string) {
        return jedis().lpushx(key, string);
    }

    @Aop("redis")
    public String clientSetname(String name) {
        return jedis().clientSetname(name);
    }

    @Aop("redis")
    public Long persist(byte[] key) {
        return jedis().persist(key);
    }

    @Aop("redis")
    public String migrate(String host, int port, String key, int destinationDb, int timeout) {
        return jedis().migrate(host, port, key, destinationDb, timeout);
    }

    @Aop("redis")
    public ScanResult<String> scan(int cursor) {
        return jedis().scan(cursor);
    }

    @Aop("redis")
    public Long rpushx(byte[] key, byte[]... string) {
        return jedis().rpushx(key, string);
    }

    @Aop("redis")
    public byte[] echo(byte[] string) {
        return jedis().echo(string);
    }

    @Aop("redis")
    public ScanResult<String> scan(int cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }

    @Aop("redis")
    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        return jedis().linsert(key, where, pivot, value);
    }

    @Aop("redis")
    public String debug(DebugParams params) {
        return jedis().debug(params);
    }

    @Aop("redis")
    public Client getClient() {
        return jedis().getClient();
    }

    @Aop("redis")
    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        return jedis().brpoplpush(source, destination, timeout);
    }

    @Aop("redis")
    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        return jedis().hscan(key, cursor);
    }

    @Aop("redis")
    public Boolean setbit(byte[] key, long offset, boolean value) {
        return jedis().setbit(key, offset, value);
    }

    @Aop("redis")
    public ScanResult<Entry<String, String>> hscan(String key, int cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }

    @Aop("redis")
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return jedis().setbit(key, offset, value);
    }

    @Aop("redis")
    public Boolean getbit(byte[] key, long offset) {
        return jedis().getbit(key, offset);
    }

    @Aop("redis")
    public Long bitpos(byte[] key, boolean value) {
        return jedis().bitpos(key, value);
    }

    @Aop("redis")
    public Long bitpos(byte[] key, boolean value, BitPosParams params) {
        return jedis().bitpos(key, value, params);
    }

    @Aop("redis")
    public Long setrange(byte[] key, long offset, byte[] value) {
        return jedis().setrange(key, offset, value);
    }

    @Aop("redis")
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return jedis().getrange(key, startOffset, endOffset);
    }

    @Aop("redis")
    public ScanResult<String> sscan(String key, int cursor) {
        return jedis().sscan(key, cursor);
    }

    @Aop("redis")
    public Long publish(byte[] channel, byte[] message) {
        return jedis().publish(channel, message);
    }

    @Aop("redis")
    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        jedis().subscribe(jedisPubSub, channels);
    }

    @Aop("redis")
    public ScanResult<String> sscan(String key, int cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }

    @Aop("redis")
    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        jedis().psubscribe(jedisPubSub, patterns);
    }

    @Aop("redis")
    public Long getDB() {
        return jedis().getDB();
    }

    @Aop("redis")
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return jedis().eval(script, keys, args);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return jedis().zscan(key, cursor);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }

    @Aop("redis")
    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        return jedis().eval(script, keyCount, params);
    }

    @Aop("redis")
    public Object eval(byte[] script, int keyCount, byte[]... params) {
        return jedis().eval(script, keyCount, params);
    }

    @Aop("redis")
    public Object eval(byte[] script) {
        return jedis().eval(script);
    }

    @Aop("redis")
    public Object evalsha(byte[] sha1) {
        return jedis().evalsha(sha1);
    }

    @Aop("redis")
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return jedis().evalsha(sha1, keys, args);
    }

    @Aop("redis")
    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        return jedis().evalsha(sha1, keyCount, params);
    }

    @Aop("redis")
    public ScanResult<String> scan(String cursor) {
        return jedis().scan(cursor);
    }

    @Aop("redis")
    public String scriptFlush() {
        return jedis().scriptFlush();
    }

    @Aop("redis")
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }

    @Aop("redis")
    public Long scriptExists(byte[] sha1) {
        return jedis().scriptExists(sha1);
    }

    @Aop("redis")
    public List<Long> scriptExists(byte[]... sha1) {
        return jedis().scriptExists(sha1);
    }

    @Aop("redis")
    public byte[] scriptLoad(byte[] script) {
        return jedis().scriptLoad(script);
    }

    @Aop("redis")
    public String scriptKill() {
        return jedis().scriptKill();
    }

    @Aop("redis")
    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        return jedis().hscan(key, cursor);
    }

    @Aop("redis")
    public String slowlogReset() {
        return jedis().slowlogReset();
    }

    @Aop("redis")
    public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }

    @Aop("redis")
    public Long slowlogLen() {
        return jedis().slowlogLen();
    }

    @Aop("redis")
    public List<byte[]> slowlogGetBinary() {
        return jedis().slowlogGetBinary();
    }

    @Aop("redis")
    public List<byte[]> slowlogGetBinary(long entries) {
        return jedis().slowlogGetBinary(entries);
    }

    @Aop("redis")
    public Long objectRefcount(byte[] key) {
        return jedis().objectRefcount(key);
    }

    @Aop("redis")
    public byte[] objectEncoding(byte[] key) {
        return jedis().objectEncoding(key);
    }

    @Aop("redis")
    public Long objectIdletime(byte[] key) {
        return jedis().objectIdletime(key);
    }

    @Aop("redis")
    public Long bitcount(byte[] key) {
        return jedis().bitcount(key);
    }

    @Aop("redis")
    public ScanResult<String> sscan(String key, String cursor) {
        return jedis().sscan(key, cursor);
    }

    @Aop("redis")
    public Long bitcount(byte[] key, long start, long end) {
        return jedis().bitcount(key, start, end);
    }

    @Aop("redis")
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }

    @Aop("redis")
    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return jedis().bitop(op, destKey, srcKeys);
    }

    @Aop("redis")
    public byte[] dump(byte[] key) {
        return jedis().dump(key);
    }

    @Aop("redis")
    public String restore(byte[] key, int ttl, byte[] serializedValue) {
        return jedis().restore(key, ttl, serializedValue);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return jedis().zscan(key, cursor);
    }

    @Aop("redis")
    public Long pexpire(byte[] key, int milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }

    @Aop("redis")
    public Long pexpire(byte[] key, long milliseconds) {
        return jedis().pexpire(key, milliseconds);
    }

    @Aop("redis")
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return jedis().pexpireAt(key, millisecondsTimestamp);
    }

    @Aop("redis")
    public Long pttl(byte[] key) {
        return jedis().pttl(key);
    }

    @Aop("redis")
    public String psetex(byte[] key, int milliseconds, byte[] value) {
        return jedis().psetex(key, milliseconds, value);
    }

    @Aop("redis")
    public String clusterNodes() {
        return jedis().clusterNodes();
    }

    @Aop("redis")
    public String psetex(byte[] key, long milliseconds, byte[] value) {
        return jedis().psetex(key, milliseconds, value);
    }

    @Aop("redis")
    public String readonly() {
        return jedis().readonly();
    }

    @Aop("redis")
    public String clusterMeet(String ip, int port) {
        return jedis().clusterMeet(ip, port);
    }

    @Aop("redis")
    public String clusterReset(Reset resetType) {
        return jedis().clusterReset(resetType);
    }

    @Aop("redis")
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        return jedis().set(key, value, nxxx);
    }

    @Aop("redis")
    public String clusterAddSlots(int... slots) {
        return jedis().clusterAddSlots(slots);
    }

    @Aop("redis")
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        return jedis().set(key, value, nxxx, expx, time);
    }

    @Aop("redis")
    public String clusterDelSlots(int... slots) {
        return jedis().clusterDelSlots(slots);
    }

    @Aop("redis")
    public String clusterInfo() {
        return jedis().clusterInfo();
    }

    @Aop("redis")
    public String clientKill(byte[] client) {
        return jedis().clientKill(client);
    }

    @Aop("redis")
    public List<String> clusterGetKeysInSlot(int slot, int count) {
        return jedis().clusterGetKeysInSlot(slot, count);
    }

    @Aop("redis")
    public String clientGetname() {
        return jedis().clientGetname();
    }

    @Aop("redis")
    public String clientList() {
        return jedis().clientList();
    }

    @Aop("redis")
    public String clusterSetSlotNode(int slot, String nodeId) {
        return jedis().clusterSetSlotNode(slot, nodeId);
    }

    @Aop("redis")
    public String clientSetname(byte[] name) {
        return jedis().clientSetname(name);
    }

    @Aop("redis")
    public String clusterSetSlotMigrating(int slot, String nodeId) {
        return jedis().clusterSetSlotMigrating(slot, nodeId);
    }

    @Aop("redis")
    public List<String> time() {
        return jedis().time();
    }

    @Aop("redis")
    public String clusterSetSlotImporting(int slot, String nodeId) {
        return jedis().clusterSetSlotImporting(slot, nodeId);
    }

    @Aop("redis")
    public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        return jedis().migrate(host, port, key, destinationDb, timeout);
    }

    @Aop("redis")
    public String clusterSetSlotStable(int slot) {
        return jedis().clusterSetSlotStable(slot);
    }

    @Aop("redis")
    public Long waitReplicas(int replicas, long timeout) {
        return jedis().waitReplicas(replicas, timeout);
    }

    @Aop("redis")
    public String clusterForget(String nodeId) {
        return jedis().clusterForget(nodeId);
    }

    @Aop("redis")
    public String clusterFlushSlots() {
        return jedis().clusterFlushSlots();
    }

    @Aop("redis")
    public Long pfadd(byte[] key, byte[]... elements) {
        return jedis().pfadd(key, elements);
    }

    @Aop("redis")
    public Long clusterKeySlot(String key) {
        return jedis().clusterKeySlot(key);
    }

    @Aop("redis")
    public Long clusterCountKeysInSlot(int slot) {
        return jedis().clusterCountKeysInSlot(slot);
    }

    @Aop("redis")
    public long pfcount(byte[] key) {
        return jedis().pfcount(key);
    }

    @Aop("redis")
    public String clusterSaveConfig() {
        return jedis().clusterSaveConfig();
    }

    @Aop("redis")
    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return jedis().pfmerge(destkey, sourcekeys);
    }

    @Aop("redis")
    public String clusterReplicate(String nodeId) {
        return jedis().clusterReplicate(nodeId);
    }

    @Aop("redis")
    public Long pfcount(byte[]... keys) {
        return jedis().pfcount(keys);
    }

    @Aop("redis")
    public List<String> clusterSlaves(String nodeId) {
        return jedis().clusterSlaves(nodeId);
    }

    @Aop("redis")
    public ScanResult<byte[]> scan(byte[] cursor) {
        return jedis().scan(cursor);
    }

    @Aop("redis")
    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        return jedis().scan(cursor, params);
    }

    @Aop("redis")
    public String clusterFailover() {
        return jedis().clusterFailover();
    }

    @Aop("redis")
    public List<Object> clusterSlots() {
        return jedis().clusterSlots();
    }

    @Aop("redis")
    public String asking() {
        return jedis().asking();
    }

    @Aop("redis")
    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return jedis().hscan(key, cursor);
    }

    @Aop("redis")
    public List<String> pubsubChannels(String pattern) {
        return jedis().pubsubChannels(pattern);
    }

    @Aop("redis")
    public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().hscan(key, cursor, params);
    }

    @Aop("redis")
    public Long pubsubNumPat() {
        return jedis().pubsubNumPat();
    }

    @Aop("redis")
    public Map<String, String> pubsubNumSub(String... channels) {
        return jedis().pubsubNumSub(channels);
    }

    @Aop("redis")
    public void close() {
        jedis().close();
    }

    @Aop("redis")
    public void setDataSource(Pool<Jedis> jedisPool) {
        jedis().setDataSource(jedisPool);
    }

    @Aop("redis")
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return jedis().sscan(key, cursor);
    }

    @Aop("redis")
    public Long pfadd(String key, String... elements) {
        return jedis().pfadd(key, elements);
    }

    @Aop("redis")
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().sscan(key, cursor, params);
    }

    @Aop("redis")
    public long pfcount(String key) {
        return jedis().pfcount(key);
    }

    @Aop("redis")
    public long pfcount(String... keys) {
        return jedis().pfcount(keys);
    }

    @Aop("redis")
    public String pfmerge(String destkey, String... sourcekeys) {
        return jedis().pfmerge(destkey, sourcekeys);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return jedis().zscan(key, cursor);
    }

    @Aop("redis")
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return jedis().zscan(key, cursor, params);
    }

    @Aop("redis")
    public List<String> blpop(int timeout, String key) {
        return jedis().blpop(timeout, key);
    }

    @Aop("redis")
    public List<String> brpop(int timeout, String key) {
        return jedis().brpop(timeout, key);
    }

    @Aop("redis")
    public Long geoadd(String key, double longitude, double latitude, String member) {
        return jedis().geoadd(key, longitude, latitude, member);
    }

    @Aop("redis")
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return jedis().geoadd(key, memberCoordinateMap);
    }

    @Aop("redis")
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return jedis().geoadd(key, longitude, latitude, member);
    }

    @Aop("redis")
    public Double geodist(String key, String member1, String member2) {
        return jedis().geodist(key, member1, member2);
    }

    @Aop("redis")
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return jedis().geoadd(key, memberCoordinateMap);
    }

    @Aop("redis")
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return jedis().geodist(key, member1, member2, unit);
    }

    @Aop("redis")
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return jedis().geodist(key, member1, member2);
    }

    @Aop("redis")
    public List<String> geohash(String key, String... members) {
        return jedis().geohash(key, members);
    }

    @Aop("redis")
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return jedis().geodist(key, member1, member2, unit);
    }

    @Aop("redis")
    public List<GeoCoordinate> geopos(String key, String... members) {
        return jedis().geopos(key, members);
    }

    @Aop("redis")
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return jedis().geohash(key, members);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedis().georadius(key, longitude, latitude, radius, unit);
    }

    @Aop("redis")
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return jedis().geopos(key, members);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadius(String key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedis().georadius(key, longitude, latitude, radius, unit, param);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit) {
        return jedis().georadius(key, longitude, latitude, radius, unit);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedis().georadiusByMember(key, member, radius, unit);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadius(byte[] key,
                                             double longitude,
                                             double latitude,
                                             double radius,
                                             GeoUnit unit,
                                             GeoRadiusParam param) {
        return jedis().georadius(key, longitude, latitude, radius, unit, param);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadiusByMember(String key,
                                                     String member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedis().georadiusByMember(key, member, radius, unit, param);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit) {
        return jedis().georadiusByMember(key, member, radius, unit);
    }

    @Aop("redis")
    public List<GeoRadiusResponse> georadiusByMember(byte[] key,
                                                     byte[] member,
                                                     double radius,
                                                     GeoUnit unit,
                                                     GeoRadiusParam param) {
        return jedis().georadiusByMember(key, member, radius, unit, param);
    }
}
