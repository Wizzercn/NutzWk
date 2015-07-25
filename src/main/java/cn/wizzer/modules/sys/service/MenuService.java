package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseService;
import cn.wizzer.modules.sys.bean.Sys_menu;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer.cn on 2015/7/25.
 */
@IocBean(args = {"refer:dao"})
public class MenuService  extends BaseService<Sys_menu> {
    public MenuService(Dao dao) {
        super(dao);
    }

}
