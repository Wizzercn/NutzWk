package cn.wizzer.app.wf.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.wf.modules.models.Wf_category;
import cn.wizzer.app.wf.modules.services.WfCategoryService;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class WfCategoryServiceImpl extends BaseServiceImpl<Wf_category> implements WfCategoryService {
    public WfCategoryServiceImpl(Dao dao) {
        super(dao);
    }
}
