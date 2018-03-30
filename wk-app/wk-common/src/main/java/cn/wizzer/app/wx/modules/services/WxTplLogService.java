package cn.wizzer.app.wx.modules.services;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.wx.modules.models.Wx_tpl_log;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2017/2/14.
 */
@RpcService
public interface WxTplLogService extends BaseService<Wx_tpl_log> {

}
