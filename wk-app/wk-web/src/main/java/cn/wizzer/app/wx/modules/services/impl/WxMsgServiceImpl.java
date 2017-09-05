package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_msg;
import cn.wizzer.app.wx.modules.services.WxMsgService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMsgServiceImpl extends BaseServiceImpl<Wx_msg> implements WxMsgService {
    public WxMsgServiceImpl(Dao dao) {
        super(dao);
    }
}
