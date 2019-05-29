package cn.wizzer.app.cms.modules.services;

import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.app.cms.modules.models.Cms_link;

import java.util.List;

public interface CmsLinkService extends BaseService<Cms_link>{
    List<Cms_link> getLinkList(String code, int size);
    void clearCache();
}
