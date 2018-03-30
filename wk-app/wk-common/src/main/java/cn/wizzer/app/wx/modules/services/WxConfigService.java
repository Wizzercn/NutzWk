package cn.wizzer.app.wx.modules.services;

import cn.wizzer.framework.base.service.BaseService;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.wx.modules.models.Wx_config;

@RpcService
public interface WxConfigService extends BaseService<Wx_config>{

}
