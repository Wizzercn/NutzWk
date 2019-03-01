package cn.wizzer.app.sys.modules.services.impl;

import cn.wizzer.app.sys.modules.models.Sys_app_task;
import cn.wizzer.app.sys.modules.services.SysAppTaskService;
import cn.wizzer.framework.base.service.BaseServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2019/2/27.
 */
@IocBean(args = {"refer:dao"})
@Service(interfaceClass = SysAppTaskService.class)
public class SysAppTaskServiceImpl extends BaseServiceImpl<Sys_app_task> implements SysAppTaskService {
    public SysAppTaskServiceImpl(Dao dao) {
        super(dao);
    }

}