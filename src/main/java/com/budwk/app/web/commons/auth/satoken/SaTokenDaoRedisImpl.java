package com.budwk.app.web.commons.auth.satoken;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.budwk.app.base.constant.RedisConstant;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wizzer@qq.com
 */
@IocBean
public class SaTokenDaoRedisImpl implements SaTokenDao {

    @Inject
    private RedisService redisService;

    @Override
    public String get(String key) {
        return redisService.get(RedisConstant.TOKEN + key);
    }

    @Override
    public void set(String key, String value, long timeout) {
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            redisService.set(RedisConstant.TOKEN + key, value);
        } else {
            redisService.setex(RedisConstant.TOKEN + key, (int) timeout, value);
        }
    }

    @Override
    public void update(String key, String value) {
        long expire = this.getTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.set(key, value, expire);
    }

    @Override
    public void delete(String key) {
        redisService.del(RedisConstant.TOKEN + key);
    }

    @Override
    public long getTimeout(String key) {
        return redisService.ttl(RedisConstant.TOKEN + key);
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = this.getTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                this.set(key, this.get(key), timeout);
            }
            return;
        }
        redisService.expire(RedisConstant.TOKEN + key, (int) timeout);
    }

    @Override
    public Object getObject(String key) {
        byte[] bytes = redisService.get((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8));
        if (bytes != null) {
            return Lang.fromBytes(bytes, Object.class);
        }
        return null;
    }

    @Override
    public void setObject(String key, Object value, long timeout) {
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            redisService.set((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8), Lang.toBytes(value));
        } else {
            redisService.setex((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8), (int) timeout, Lang.toBytes(value));
        }
    }

    @Override
    public void updateObject(String key, Object object) {
        long expire = this.getObjectTimeout(key);
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.setObject(key, object, expire);
    }

    @Override
    public void deleteObject(String key) {
        redisService.del((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public long getObjectTimeout(String key) {
        return redisService.ttl((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void updateObjectTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getObjectTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                this.setObject(key, this.getObject(key), timeout);
            }
            return;
        }
        redisService.expire((RedisConstant.TOKEN + key).getBytes(StandardCharsets.UTF_8), (int) timeout);
    }

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size) {
        Set<String> keys = redisService.keys(RedisConstant.TOKEN + prefix + "*" + keyword + "*");
        List<String> list = new ArrayList<>(keys);
        return SaFoxUtil.searchList(list, start, size);

    }
}
