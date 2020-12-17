package com.budwk.app.cms.services.impl;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.cms.services.CmsLinkClassService;
import com.budwk.app.cms.services.CmsLinkService;
import com.budwk.app.cms.models.Cms_link;
import com.budwk.app.cms.models.Cms_link_class;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

import java.util.ArrayList;
import java.util.List;

@IocBean(args = {"refer:dao"})
@CacheDefaults(cacheName = RedisConstant.PLATFORM_REDIS_WKCACHE_PREFIX + "cms_link")
public class CmsLinkServiceImpl extends BaseServiceImpl<Cms_link> implements CmsLinkService {
    public CmsLinkServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private CmsLinkClassService cmsLinkClassService;

    @CacheResult
    public List<Cms_link> getLinkList(String code, int size) {
        List<Cms_link> links = new ArrayList<>();
        Cms_link_class cmsLinkClass = cmsLinkClassService.fetch(Cnd.where("code", "=", code));
        if (cmsLinkClass != null) {
            Pager pager = new Pager();
            pager.setPageSize(size);
            pager.setPageNumber(1);
            links = this.query(Cnd.where("classId", "=", cmsLinkClass.getId()).desc("createdAt"), pager);
        }
        return links;
    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
