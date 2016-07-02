package cn.wizzer.modules.gm.wx.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.gm.wx.models.Wx_config;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/2.
 */
@IocBean(args = {"refer:dao"})
public class WxConfigService extends Service<Wx_config> {
    public WxConfigService(Dao dao) {
        super(dao);
    }

}
