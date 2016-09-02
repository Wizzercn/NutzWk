package cn.wizzer.modules.back.wx.services;

import cn.wizzer.common.base.Service;
import cn.wizzer.modules.back.wx.models.Wx_tpl_log;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
@IocBean(args = {"refer:dao"})
public class WxTplLogService extends Service<Wx_tpl_log> {
	private static final Log log = Logs.get();

    public WxTplLogService(Dao dao) {
    	super(dao);
    }
}

