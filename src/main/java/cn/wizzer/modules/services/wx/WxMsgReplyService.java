package cn.wizzer.modules.services.wx;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.wx.Wx_msg_reply;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean(args = {"refer:dao"})
public class WxMsgReplyService extends Service<Wx_msg_reply> {
    public WxMsgReplyService(Dao dao) {
        super(dao);
    }
}
