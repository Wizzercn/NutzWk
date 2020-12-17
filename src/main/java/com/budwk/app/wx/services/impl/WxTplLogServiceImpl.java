package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_tpl_log;
import com.budwk.app.wx.services.WxTplLogService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplLogServiceImpl extends BaseServiceImpl<Wx_tpl_log> implements WxTplLogService {
    public WxTplLogServiceImpl(Dao dao) {
        super(dao);
    }
}
