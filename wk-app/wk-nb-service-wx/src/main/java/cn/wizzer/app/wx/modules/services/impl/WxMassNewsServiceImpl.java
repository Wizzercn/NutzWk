package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wx.modules.models.Wx_mass_news;
import cn.wizzer.app.wx.modules.services.WxMassNewsService;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
@Service(interfaceClass=WxMassNewsService.class)
public class WxMassNewsServiceImpl extends BaseServiceImpl<Wx_mass_news> implements WxMassNewsService {
    public WxMassNewsServiceImpl(Dao dao) {
        super(dao);
    }
}
