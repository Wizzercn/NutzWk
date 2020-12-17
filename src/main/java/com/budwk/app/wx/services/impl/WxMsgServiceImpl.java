package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_msg;
import com.budwk.app.wx.services.WxMsgService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMsgServiceImpl extends BaseServiceImpl<Wx_msg> implements WxMsgService {
    public WxMsgServiceImpl(Dao dao) {
        super(dao);
    }
}
