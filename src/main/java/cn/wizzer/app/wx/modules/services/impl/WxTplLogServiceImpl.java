package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.app.wx.modules.models.Wx_tpl_log;
import cn.wizzer.app.wx.modules.services.WxTplLogService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplLogServiceImpl extends BaseServiceImpl<Wx_tpl_log> implements WxTplLogService {
    public WxTplLogServiceImpl(Dao dao) {
        super(dao);
    }
}
