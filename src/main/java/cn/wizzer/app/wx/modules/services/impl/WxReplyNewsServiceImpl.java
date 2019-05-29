package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_reply_news;
import cn.wizzer.app.wx.modules.services.WxReplyNewsService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxReplyNewsServiceImpl extends BaseServiceImpl<Wx_reply_news> implements WxReplyNewsService {
    public WxReplyNewsServiceImpl(Dao dao) {
        super(dao);
    }
}
