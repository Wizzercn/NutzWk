package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_reply_txt;
import com.budwk.app.wx.services.WxReplyTxtService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxReplyTxtServiceImpl extends BaseServiceImpl<Wx_reply_txt> implements WxReplyTxtService {
    public WxReplyTxtServiceImpl(Dao dao) {
        super(dao);
    }
}
