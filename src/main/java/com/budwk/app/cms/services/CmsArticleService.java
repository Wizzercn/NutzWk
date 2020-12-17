package com.budwk.app.cms.services;

import com.budwk.app.base.page.Pagination;
import com.budwk.app.cms.models.Cms_article;
import com.budwk.app.base.service.BaseService;
import org.nutz.dao.Condition;

public interface CmsArticleService extends BaseService<Cms_article> {
    /**
     * 获取文章列表
     *
     * @param pageNumber
     * @param pageSize
     * @param cnd
     * @return
     */
    Pagination getListPage(int pageNumber, int pageSize, Condition cnd);

    /**
     * 从缓存根据条件查询一篇文章
     *
     * @param cnd
     * @return
     */
    Cms_article getArticle(Condition cnd);

    /**
     * 清空缓存
     */
    void clearCache();
}
