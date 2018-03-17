package cn.wizzer.app.cms.modules.services;

import cn.wizzer.framework.base.service.BaseService;
import cn.wizzer.app.cms.modules.models.Cms_channel;

public interface CmsChannelService extends BaseService<Cms_channel>{
    /**
     * 添加栏目
     * @param channel
     * @param pid
     */
    void save(Cms_channel channel, String pid);

    /**
     * 级联删除栏目
     * @param channel
     */
    void deleteAndChild(Cms_channel channel);
}
