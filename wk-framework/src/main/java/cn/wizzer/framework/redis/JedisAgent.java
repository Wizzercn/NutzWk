package cn.wizzer.framework.redis;

import org.nutz.ioc.loader.annotation.IocBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.Pool;

/**
 * 封装JedisPool和JedisCluster,抹平两者的差异. 通过redis.mode配置, 当等于cluster时,使用JedisCluster, 否则是使用JedisPool
 * <p>
 * Created by wendal,wizzer on 2017/1/21.
 */
@IocBean
public class JedisAgent {
    public String mode = "";
    // 存储JedisPool,即单机版/主从/Sentinel模式
    protected Pool<Jedis> jedisPool;
    // 将JedisCluster封装为Jedis,就可以实现自动切换了
    protected JedisClusterWrapper jedisClusterWrapper;

    public JedisAgent() {

    }

    public JedisAgent(String mode) {
        this.mode = mode;
    }

    public JedisAgent(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisAgent(JedisCluster jedisCluster) {
        super();
        this.jedisClusterWrapper = new JedisClusterWrapper(jedisCluster);
    }


    /**
     * 若redis.mode=cluster,则返回JedisClusterWrapper对象,否则返回JedisPool(或Pool<Jedis>)的Jedis实例
     *
     * @return
     */
    public Jedis jedis() {
        if (!"cluster".equals(mode))
            return getJedisPool().getResource();
        return getJedisClusterWrapper();
    }

    public Jedis getResource() {
        return jedis();
    }

    @SuppressWarnings("unchecked")
    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public JedisClusterWrapper getJedisClusterWrapper() {
        return jedisClusterWrapper;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setJedisClusterWrapper(JedisClusterWrapper jedisClusterWrapper) {
        this.jedisClusterWrapper = jedisClusterWrapper;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
