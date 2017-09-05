package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_plugin;
import cn.wizzer.app.sys.modules.services.SysPluginService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2016/12/23.
 */
@IocBean(args = {"refer:dao"})
public class SysPluginServiceImpl extends BaseServiceImpl<Sys_plugin> implements SysPluginService {
    public SysPluginServiceImpl(Dao dao) {
        super(dao);
    }
}