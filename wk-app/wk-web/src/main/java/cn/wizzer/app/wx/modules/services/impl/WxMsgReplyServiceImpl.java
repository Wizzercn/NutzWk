package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_msg_reply;
import cn.wizzer.app.wx.modules.services.WxMsgReplyService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMsgReplyServiceImpl extends BaseServiceImpl<Wx_msg_reply> implements WxMsgReplyService {
    public WxMsgReplyServiceImpl(Dao dao) {
        super(dao);
    }
}
