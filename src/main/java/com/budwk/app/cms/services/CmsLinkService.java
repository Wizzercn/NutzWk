package com.budwk.app.cms.services;

import com.budwk.app.cms.models.Cms_link;
import com.budwk.app.base.service.BaseService;

import java.util.List;

public interface CmsLinkService extends BaseService<Cms_link> {
    List<Cms_link> getLinkList(String code, int size);
    void clearCache();
}
