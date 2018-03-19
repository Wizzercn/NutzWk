package cn.wizzer.app.web.commons.ext.wx;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.dao.Cnd;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.weixin.at.impl.RedisAccessTokenStore;
import org.nutz.weixin.impl.WxApi2Impl;
import org.nutz.weixin.spi.WxApi2;
import redis.clients.jedis.JedisPool;

/**
 * Created by wizzer on 2018/3/17.
 */
@IocBean
public class WxService {
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    private JedisPool jedisPool;
    @Inject
    private RedisService redisService;

    public synchronized WxApi2 getWxApi2(String wxid) {
        WxApi2Impl wxApi2 = Lang.fromBytes(redisService.get(("nutzwk:wx:wxapi:" + wxid).getBytes()), WxApi2Impl.class);
        if (wxApi2 == null) {
            Wx_config appInfo = wxConfigService.fetch(Cnd.where("id", "=", wxid));
            RedisAccessTokenStore redisAccessTokenStore = new RedisAccessTokenStore();//如果是集群部署请启用RedisAccessTokenStore
            redisAccessTokenStore.setTokenKey("nutzwk:wx:token:" + wxid);
            redisAccessTokenStore.setJedisPool(jedisPool);
            wxApi2 = new WxApi2Impl();
            wxApi2.setAppid(appInfo.getAppid());
            wxApi2.setAppsecret(appInfo.getAppsecret());
            wxApi2.setEncodingAesKey(appInfo.getEncodingAESKey());
            wxApi2.setToken(appInfo.getToken());
            wxApi2.setAccessTokenStore(redisAccessTokenStore);
            redisService.set(("nutzwk:wx:wxapi:" + wxid).getBytes(), Lang.toBytes(wxApi2));
        }
        return wxApi2;
    }

    public void delete(String wxid) {
        redisService.del(("nutzwk:wx:wxapi:" + wxid).getBytes());
    }
}
