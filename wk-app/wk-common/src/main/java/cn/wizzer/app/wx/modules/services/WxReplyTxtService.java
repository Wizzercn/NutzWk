package cn.wizzer.app.wx.modules.services;

import cn.wizzer.framework.base.service.BaseService;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.wx.modules.models.Wx_reply_txt;

@RpcService
public interface WxReplyTxtService extends BaseService<Wx_reply_txt>{

}
