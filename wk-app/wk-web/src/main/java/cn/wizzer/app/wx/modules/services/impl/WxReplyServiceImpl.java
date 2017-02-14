package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_reply;
import cn.wizzer.app.wx.modules.services.WxReplyService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxReplyServiceImpl extends BaseServiceImpl<Wx_reply> implements WxReplyService {
    public WxReplyServiceImpl(Dao dao) {
        super(dao);
    }
}
