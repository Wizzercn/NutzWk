package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_app_list;
import cn.wizzer.app.sys.modules.services.SysAppListService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2019/2/27.
 */
@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysAppListService.class)
public class SysAppListServiceImpl extends BaseServiceImpl<Sys_app_list> implements SysAppListService {
    public SysAppListServiceImpl(Dao dao) {
        super(dao);
    }

}