package cn.wizzer.modules.back.wx.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.wx.models.Wx_config;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.spi.WxApi2;

/**
 * Created by wizzer on 2016/7/2.
 */
@IocBean(args = {"refer:dao"})
public class WxConfigService extends Service<Wx_config> {
    public WxConfigService(Dao dao) {
        super(dao);
    }

    public WxApi2 getWxApi2(String key) {
        Wx_config appInfo = this.fetch(Cnd.where("id", "=", key));
        WxApi2Impl wxApi2 = new WxApi2Impl();
        wxApi2.setAppid(appInfo.getAppid());
        wxApi2.setAppsecret(appInfo.getAppsecret());
        wxApi2.setEncodingAesKey(appInfo.getEncodingAESKey());
        wxApi2.setToken(appInfo.getToken());
        return wxApi2;
    }
}
