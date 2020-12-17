package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_tpl_id;
import com.budwk.app.wx.services.WxTplIdService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplIdServiceImpl extends BaseServiceImpl<Wx_tpl_id> implements WxTplIdService {
    public WxTplIdServiceImpl(Dao dao) {
        super(dao);
    }
}
