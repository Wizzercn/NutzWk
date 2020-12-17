package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_tpl_list;
import com.budwk.app.wx.services.WxTplListService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplListServiceImpl extends BaseServiceImpl<Wx_tpl_list> implements WxTplListService {
    public WxTplListServiceImpl(Dao dao) {
        super(dao);
    }
}
