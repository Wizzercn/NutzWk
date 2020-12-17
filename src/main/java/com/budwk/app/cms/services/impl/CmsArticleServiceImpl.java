package com.budwk.app.cms.services.impl;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.base.page.Pagination;
import com.budwk.app.cms.models.Cms_article;
import com.budwk.app.cms.services.CmsArticleService;
import com.budwk.app.base.service.impl.BaseServiceImpl;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

@IocBean(args = {"refer:dao"})
@CacheDefaults(cacheName = RedisConstant.PLATFORM_REDIS_WKCACHE_PREFIX + "cms_article")
public class CmsArticleServiceImpl extends BaseServiceImpl<Cms_article> implements CmsArticleService {
    public CmsArticleServiceImpl(Dao dao) {
        super(dao);
    }

    @CacheResult
    public Pagination getListPage(int pageNumber, int pageSize, Condition cnd) {
        return this.listPage(pageNumber, pageSize, cnd);
    }

    @CacheResult
    public Cms_article getArticle(Condition cnd) {
        return this.fetch(cnd);
    }

    @CacheRemoveAll
    public void clearCache() {

    }
}
