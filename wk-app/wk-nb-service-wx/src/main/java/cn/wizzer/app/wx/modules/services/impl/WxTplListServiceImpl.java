package cn.wizzer.app.wx.modules.services.impl;

import cn.wizzer.app.wx.modules.models.Wx_tpl_list;
import cn.wizzer.app.wx.modules.services.WxTplListService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WxTplListServiceImpl extends BaseServiceImpl<Wx_tpl_list> implements WxTplListService {
    public WxTplListServiceImpl(Dao dao) {
        super(dao);
    }
}
