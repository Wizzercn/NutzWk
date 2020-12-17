package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_mass;
import com.budwk.app.wx.services.WxMassService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMassServiceImpl extends BaseServiceImpl<Wx_mass> implements WxMassService {
    public WxMassServiceImpl(Dao dao) {
        super(dao);
    }
}
