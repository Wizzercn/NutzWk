package cn.wizzer.modules.sys.service;

import cn.wizzer.common.service.core.BaseIdEntityService;
import cn.wizzer.modules.sys.bean.Sys_dict;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by Wizzer.cn on 2015/7/3.
 */
@IocBean(args = { "refer:dao" })
public class DictService extends BaseIdEntityService<Sys_dict> {
    public DictService(Dao dao) {
        super(dao);
    }

}
