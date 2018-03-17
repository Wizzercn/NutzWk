package cn.wizzer.app.cms.modules.services;

import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.app.cms.modules.models.Cms_channel;

public interface CmsChannelService extends BaseService<Cms_channel>{
    void save(Cms_channel channel, String pid);
    void deleteAndChild(Cms_channel channel);
}
