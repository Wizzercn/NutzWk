package cn.wizzer.modules.services.wx;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.wx.Wx_reply;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean(args = {"refer:dao"})
public class WxReplyService extends Service<Wx_reply> {
    public WxReplyService(Dao dao) {
        super(dao);
    }
}
