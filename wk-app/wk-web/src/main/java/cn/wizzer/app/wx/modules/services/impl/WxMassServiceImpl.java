package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_mass;
import cn.wizzer.app.wx.modules.services.WxMassService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxMassServiceImpl extends BaseServiceImpl<Wx_mass> implements WxMassService {
    public WxMassServiceImpl(Dao dao) {
        super(dao);
    }
}
