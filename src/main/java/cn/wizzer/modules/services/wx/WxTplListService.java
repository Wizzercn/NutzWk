package cn.wizzer.modules.services.wx;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.models.wx.Wx_tpl_list;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
@IocBean(args = {"refer:dao"})
public class WxTplListService extends Service<Wx_tpl_list> {
	private static final Log log = Logs.get();

    public WxTplListService(Dao dao) {
    	super(dao);
    }
}

