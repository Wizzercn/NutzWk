package cn.wizzer.app.wx.modules.services;

import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.app.wx.modules.models.Wx_config;
import org.nutz.weixin.spi.WxApi2;

public interface WxConfigService extends BaseService<Wx_config>{
    WxApi2 getWxApi2(String wxid);
}
