package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_tpl_id;
import cn.wizzer.app.wx.modules.services.WxTplIdService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplIdServiceImpl extends BaseServiceImpl<Wx_tpl_id> implements WxTplIdService {
    public WxTplIdServiceImpl(Dao dao) {
        super(dao);
    }
}
