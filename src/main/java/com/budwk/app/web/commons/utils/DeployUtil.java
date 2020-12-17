package com.budwk.app.web.commons.utils;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.SignUtil;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.Map;

/**
 * 应用管理服务端接口签名
 *
 * @author wizzer(wizzer @ qq.com) on 2019/3/8.
 */
@IocBean
public class DeployUtil {
    private static final Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;
    @Inject
    private RedisService redisService;
    /**
     * 签名验证有效时间：一分钟
     */
    private static final int TIMEOUT_ONE_MINIUTE = 60000;

    public Result checkSign(Map<String, Object> paramMap) {
        try {
            String mo_appid = conf.get("deploy.appid", "");
            String mo_appkey = conf.get("deploy.appkey", "");
            String appid = Strings.sNull(paramMap.get("appid"));
            String sign = Strings.sNull(paramMap.get("sign"));
            String timestamp = Strings.sNull(paramMap.get("timestamp"));
            String nonce = Strings.sNull(paramMap.get("nonce"));
            if (Strings.isBlank(mo_appid) || Strings.isBlank(mo_appkey)) {
                return Result.error(1, "appid不正确");
            }
            // 时间戳相差大于1分钟则为无效的
            if (Times.getTS() - Long.parseLong(timestamp) > TIMEOUT_ONE_MINIUTE) {
                return Result.error(2, "timestamp不正确");
            }
            if (Strings.isBlank(nonce)) {
                return Result.error(3, "nonce不正确");
            }
            String nonceCache = redisService.get(RedisConstant.REDIS_KEY_API_SIGN_DEPLOY_NONCE + appid + "_" + nonce);
            // 如果一分钟内nonce是重复的则为无效,让nonce只能使用一次
            if (Strings.isNotBlank(nonceCache)) {
                return Result.error(3, "nonce不正确");

            }
            if (!SignUtil.createSign(mo_appkey, paramMap).equalsIgnoreCase(sign)) {
                return Result.error(4, "sign签名不正确");
            }
            //nonce保存到缓存
            redisService.setex(RedisConstant.REDIS_KEY_API_SIGN_DEPLOY_NONCE + appid + "_" + nonce, 60, nonce);
            return Result.success("验证成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(-1, "系统异常");
        }
    }
}
