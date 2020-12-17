package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_mass_news;
import com.budwk.app.wx.services.WxMassNewsService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMassNewsServiceImpl extends BaseServiceImpl<Wx_mass_news> implements WxMassNewsService {
    public WxMassNewsServiceImpl(Dao dao) {
        super(dao);
    }
}
