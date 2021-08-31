package com.budwk.app.web.commons.auth.service;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.exception.BaseException;
import com.budwk.app.base.result.Result;
import com.budwk.app.web.commons.ext.sms.SmsService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;

/**
 * @author wizzer@qq.com
 */
@IocBean
@Slf4j
public class ValidateService {
    @Inject
    private RedisService redisService;
    @Inject
    private SmsService smsSendServer;

    /**
     * 获取验证码
     *
     * @return
     */
    public NutMap getCaptcha() {
        String uuid = R.UU32();
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 40);
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        String text = captcha.text();
        redisService.setex(RedisConstant.UCENTER_CAPTCHA + uuid, 180, text);
        return NutMap.NEW().addv("key", uuid).addv("codeUrl", captcha.toBase64());
    }

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号码
     * @throws BaseException
     */
    public void getSmsCode(String mobile) throws BaseException {
        String text = R.captchaNumber(4);
        String codeFromRedis = redisService.get(RedisConstant.UCENTER_SMSCODE + mobile + ":LOCK");
        if (Strings.isNotBlank(codeFromRedis)) {
            throw new BaseException("请1分钟之后再试");
        }
        smsSendServer.sendCode(mobile, text);
        log.debug("sms code:::" + text);
        redisService.setex(RedisConstant.UCENTER_SMSCODE + mobile, 300, text);
        redisService.setex(RedisConstant.UCENTER_SMSCODE + mobile + ":LOCK", 60, text);
    }

    /**
     * 核验验证码
     *
     * @param key  验证码的key
     * @param code 验证码的值
     * @throws BaseException
     */
    public void checkCode(String key, String code) throws BaseException {
        String codeFromRedis = redisService.get(RedisConstant.UCENTER_CAPTCHA + key);

        if (Strings.isBlank(code)) {
            throw new BaseException("请输入验证码");
        }
        if (Strings.isEmpty(codeFromRedis)) {
            throw new BaseException("验证码已过期");
        }
        if (!Strings.equalsIgnoreCase(code, codeFromRedis)) {
            throw new BaseException("验证码不正确");
        }
        redisService.del(RedisConstant.UCENTER_CAPTCHA + key);
    }

    /**
     * 核验短信验证码
     *
     * @param mobile 手机号码
     * @param code   验证码值
     * @throws BaseException
     */
    public void checkSMSCode(String mobile, String code) throws BaseException {
        String codeFromRedis = redisService.get(RedisConstant.UCENTER_SMSCODE + mobile);

        if (Strings.isBlank(code)) {
            throw new BaseException("请输入短信验证码");
        }
        if (Strings.isEmpty(codeFromRedis)) {
            throw new BaseException("短信验证码已过期");
        }
        if (!Strings.equalsIgnoreCase(code, codeFromRedis)) {
            throw new BaseException("短信验证码不正确");
        }
        redisService.del(RedisConstant.UCENTER_SMSCODE + mobile);
    }

}
