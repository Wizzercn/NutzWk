package com.budwk.app.cms.services;

import com.budwk.app.base.service.BaseService;
import com.budwk.app.cms.models.Cms_site;

public interface CmsSiteService extends BaseService<Cms_site> {
    /**
     * 通过编码获取站点信息
     *
     * @param code
     * @return
     */
    Cms_site getSite(String code);

    void create(Cms_site site);

    void update(Cms_site site);

    /**
     * 清空缓存
     */
    void clearCache();
}
