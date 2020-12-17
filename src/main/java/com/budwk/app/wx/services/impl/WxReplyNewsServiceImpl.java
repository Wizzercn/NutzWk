package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_reply_news;
import com.budwk.app.wx.services.WxReplyNewsService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxReplyNewsServiceImpl extends BaseServiceImpl<Wx_reply_news> implements WxReplyNewsService {
    public WxReplyNewsServiceImpl(Dao dao) {
        super(dao);
    }
}
