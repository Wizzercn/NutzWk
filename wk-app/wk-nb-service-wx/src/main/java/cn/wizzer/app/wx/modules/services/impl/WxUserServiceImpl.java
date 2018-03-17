package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_user;
import cn.wizzer.app.wx.modules.services.WxUserService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxUserServiceImpl extends BaseServiceImpl<Wx_user> implements WxUserService {
    public WxUserServiceImpl(Dao dao) {
        super(dao);
    }
}
