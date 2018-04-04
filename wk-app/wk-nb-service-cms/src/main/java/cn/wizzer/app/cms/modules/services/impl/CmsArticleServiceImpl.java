package cn.wizzer.app.cms.modules.services.impl;

import cn.wizzer.framework.base.service.BaseServiceImpl;
import cn.wizzer.app.cms.modules.models.Cms_article;
import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.framework.page.Pagination;
import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.plugins.wkcache.annotation.CacheDefaults;
import org.nutz.plugins.wkcache.annotation.CacheRemoveAll;
import org.nutz.plugins.wkcache.annotation.CacheResult;

@IocBean(args = {"refer:dao"})
@Service(interfaceClass=CmsArticleService.class)
@CacheDefaults(cacheName = "cms_article")
public class CmsArticleServiceImpl extends BaseServiceImpl<Cms_article> implements CmsArticleService {
    public CmsArticleServiceImpl(Dao dao) {
        super(dao);
    }

    @CacheResult
    @HystrixCommand
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
