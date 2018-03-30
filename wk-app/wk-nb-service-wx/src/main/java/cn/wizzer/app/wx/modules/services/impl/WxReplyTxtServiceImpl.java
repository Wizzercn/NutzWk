package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.app.wx.modules.models.Wx_reply_txt;
import cn.wizzer.app.wx.modules.services.WxReplyTxtService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxReplyTxtServiceImpl extends BaseServiceImpl<Wx_reply_txt> implements WxReplyTxtService {
    public WxReplyTxtServiceImpl(Dao dao) {
        super(dao);
    }
}
