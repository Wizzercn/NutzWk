package com.budwk.app.web.commons.ext.validate;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.web.commons.exception.CaptchaException;
import com.budwk.app.web.commons.exception.SmsException;
import com.budwk.app.web.commons.ext.sms.SmsService;
import com.wf.captcha.ArithmeticCaptcha;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.UUID;

/**
 * @author wizzer@qq.com
 */
@IocBean
public class ValidateService {
    private static final Log log = Logs.get();
    @Inject
    private RedisService redisService;
    @Inject
    private SmsService smsService;

    public NutMap getCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        String text = captcha.text();
        redisService.setex(RedisConstant.REDIS_CAPTCHA_KEY + uuid, 180, text);
        return NutMap.NEW().addv("key", uuid).addv("codeUrl", captcha.toBase64());
    }

    public void getSMSCode(String mobile) throws SmsException {
        String text = R.captchaNumber(4);

        String codeFromRedis = redisService.get(RedisConstant.REDIS_SMSCODE_KEY + mobile + ":LOCK");
        if (Strings.isNotBlank(codeFromRedis)) {
            throw new SmsException("请1分钟之后再试");
        }

        if (smsService.sendCode(mobile, text)) {
            log.debug("sms code:::" + text);
            redisService.setex(RedisConstant.REDIS_SMSCODE_KEY + mobile, 300, text);
            redisService.setex(RedisConstant.REDIS_SMSCODE_KEY + mobile + ":LOCK", 60, text);
        }
    }

    public void checkCode(String key, String code) throws CaptchaException {
        String codeFromRedis = redisService.get(RedisConstant.REDIS_CAPTCHA_KEY + key);

        if (Strings.isBlank(code)) {
            throw new CaptchaException("请输入验证码");
        }
        if (Strings.isEmpty(codeFromRedis)) {
            throw new CaptchaException("验证码已过期");
        }
        if (!Strings.equalsIgnoreCase(code, codeFromRedis)) {
            throw new CaptchaException("验证码不正确");
        }
        redisService.del(RedisConstant.REDIS_SMSCODE_KEY + key);
    }

    public void checkSMSCode(String mobile, String code) throws CaptchaException {
        String codeFromRedis = redisService.get(RedisConstant.REDIS_SMSCODE_KEY + mobile);

        if (Strings.isBlank(code)) {
            throw new CaptchaException("请输入短信验证码");
        }
        if (Strings.isEmpty(codeFromRedis)) {
            throw new CaptchaException("短信验证码已过期");
        }
        if (!Strings.equalsIgnoreCase(code, codeFromRedis)) {
            throw new CaptchaException("短信验证码不正确");
        }
        redisService.del(RedisConstant.REDIS_SMSCODE_KEY + mobile);
    }
}
