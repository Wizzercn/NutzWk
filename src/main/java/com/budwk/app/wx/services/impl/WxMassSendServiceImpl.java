package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_mass_send;
import com.budwk.app.wx.services.WxMassSendService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMassSendServiceImpl extends BaseServiceImpl<Wx_mass_send> implements WxMassSendService {
    public WxMassSendServiceImpl(Dao dao) {
        super(dao);
    }
}
