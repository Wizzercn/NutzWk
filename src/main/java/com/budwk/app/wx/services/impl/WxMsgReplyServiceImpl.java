package com.budwk.app.wx.services.impl;

import com.budwk.app.wx.models.Wx_msg_reply;
import com.budwk.app.wx.services.WxMsgReplyService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMsgReplyServiceImpl extends BaseServiceImpl<Wx_msg_reply> implements WxMsgReplyService {
    public WxMsgReplyServiceImpl(Dao dao) {
        super(dao);
    }
}
