package com.budwk.app.cms.services.impl;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.cms.services.CmsSiteService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import com.budwk.app.cms.models.Cms_site;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

@IocBean(args = {"refer:dao"})
@CacheDefaults(cacheName = RedisConstant.PLATFORM_REDIS_WKCACHE_PREFIX + "cms_site")
public class CmsSiteServiceImpl extends BaseServiceImpl<Cms_site> implements CmsSiteService {
    public CmsSiteServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private PropertiesProxy conf;

    @CacheResult
    public Cms_site getSite(String code) {
        return this.fetch(Cnd.where("id", "=", code));
    }

    public void create(Cms_site site) {
        this.insert(site);
    }

    public void update(Cms_site site) {
        this.updateIgnoreNull(site);
    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
