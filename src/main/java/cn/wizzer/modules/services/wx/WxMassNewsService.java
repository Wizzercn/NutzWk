package cn.wizzer.modules.services.wx;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.wx.Wx_mass_news;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/2.
 */
@IocBean(args = {"refer:dao"})
public class WxMassNewsService extends Service<Wx_mass_news> {
    public WxMassNewsService(Dao dao) {
        super(dao);
    }

}
