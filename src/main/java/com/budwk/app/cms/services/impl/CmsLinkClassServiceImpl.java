package com.budwk.app.cms.services.impl;

import com.budwk.app.cms.services.CmsLinkClassService;
import com.budwk.app.cms.models.Cms_link_class;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(args = {"refer:dao"})
public class CmsLinkClassServiceImpl extends BaseServiceImpl<Cms_link_class> implements CmsLinkClassService {
    public CmsLinkClassServiceImpl(Dao dao) {
        super(dao);
    }
}
