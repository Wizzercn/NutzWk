package com.budwk.app.base.constant;

/**
 * @author wizzer@qq.com
 */
public class RedisConstant {
    public final static String PLATFORM_REDIS_PREFIX = "budwk5mini:";
    public final static String PLATFORM_REDIS_WKCACHE_PREFIX = PLATFORM_REDIS_PREFIX + "wkcache:";
    public final static String REDIS_KEY_WSROOM = PLATFORM_REDIS_PREFIX + "wsroom:";
    public final static String REDIS_KEY_LOGIN_ADMIN_CAPTCHA = PLATFORM_REDIS_PREFIX + "admin:login:captcha:";
    public final static String REDIS_KEY_ADMIN_PUBSUB = PLATFORM_REDIS_PREFIX + "admin:pubsub:";
    public final static String REDIS_KEY_WX_TOKEN = PLATFORM_REDIS_PREFIX + "wx:token:";
    public final static String REDIS_CAPTCHA_KEY = PLATFORM_REDIS_PREFIX + "platfrom:captcha:";
    public final static String REDIS_SMSCODE_KEY = PLATFORM_REDIS_PREFIX + "platfrom:smscode:";

    public final static String REDIS_KEY_API_SIGN_DEPLOY_NONCE = PLATFORM_REDIS_PREFIX + "api:sign:deploy:nonce:";
    public final static String REDIS_KEY_API_SIGN_OPEN_NONCE = PLATFORM_REDIS_PREFIX + "api:sign:open:nonce:";
}
