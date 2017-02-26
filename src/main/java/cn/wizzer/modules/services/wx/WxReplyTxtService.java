package cn.wizzer.modules.services.wx;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.wx.Wx_reply_txt;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean(args = {"refer:dao"})
public class WxReplyTxtService extends Service<Wx_reply_txt> {
    public WxReplyTxtService(Dao dao) {
        super(dao);
    }
}
