package com.budwk.app.web.commons.ext.sms;

import com.budwk.app.web.commons.exception.SmsException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * 短信服务
 *
 * @author wizzer@qq.com
 */
@IocBean
public class SmsService {
    private static final Log log = Logs.get();
    @Inject
    private PropertiesProxy conf;

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @param text   验证码
     * @return true发送成功
     * @throws SmsException
     */
    public boolean sendCode(String mobile, String text) throws SmsException {
        try {
            if (!conf.getBoolean("sms.enabled")) {
                return true;
            }
            Credential cred = new Credential(conf.get("sms.tencent.secret-id"), conf.get("sms.tencent.secret-key"));

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {"+86"+mobile};
            req.setPhoneNumberSet(phoneNumberSet1);
            String[] templateParamSet1 = {text};
            req.setTemplateParamSet(templateParamSet1);
            req.setTemplateID(conf.get("sms.tencent.tpl.code"));
            req.setSmsSdkAppid(conf.get("sms.tencent.appid"));
            req.setSign(conf.get("sms.tencent.sign"));
            SendSmsResponse resp = client.SendSms(req);
            log.debug(SendSmsResponse.toJsonString(resp));
            return true;
        } catch (TencentCloudSDKException tencentCloudSDKException) {
            throw new SmsException(tencentCloudSDKException.getMessage());
        }
    }

    /**
     * 发送短信通知
     *
     * @param mobile 手机号码(最多200)
     * @param param  模板参数值
     * @return
     * @throws SmsException
     */
    public boolean sendMsg(String[] mobile, String[] param) throws SmsException {
        try {
            if (!conf.getBoolean("sms.enabled")) {
                return true;
            }
            Credential cred = new Credential(conf.get("sms.tencent.secret-id"), conf.get("sms.tencent.secret-key"));

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setPhoneNumberSet(mobile);
            req.setTemplateParamSet(param);
            req.setTemplateID(conf.get("sms.tencent.tpl.msg"));
            req.setSmsSdkAppid(conf.get("sms.tencent.appid"));
            req.setSign(conf.get("sms.tencent.sign"));
            SendSmsResponse resp = client.SendSms(req);
            log.debug(SendSmsResponse.toJsonString(resp));
            return true;
        } catch (TencentCloudSDKException tencentCloudSDKException) {
            throw new SmsException(tencentCloudSDKException.getMessage());
        }
    }
}
