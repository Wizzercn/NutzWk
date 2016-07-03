package cn.wizzer.modules.back.wx.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.wx.models.Wx_menu;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/2.
 */
@IocBean(args = {"refer:dao"})
public class WxMenuService extends Service<Wx_menu> {
    public WxMenuService(Dao dao) {
        super(dao);
    }


}
