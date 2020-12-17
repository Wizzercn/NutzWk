package com.budwk.app.cms.services;

import com.budwk.app.cms.models.Cms_channel;
import com.budwk.app.base.service.BaseService;

import java.util.List;

public interface CmsChannelService extends BaseService<Cms_channel> {
    /**
     * 添加栏目
     *
     * @param channel
     * @param pid
     */
    void save(Cms_channel channel, String pid);

    /**
     * 级联删除栏目
     *
     * @param channel
     */
    void deleteAndChild(Cms_channel channel);

    /**
     * 从缓存中获取栏目数据
     *
     * @param id
     * @param code
     * @return
     */
    Cms_channel getChannel(String id, String code);

    /**
     * 根据编码判断栏目是否存在
     *
     * @param code
     * @return
     */
    boolean hasChannel(String code);

    /**
     * 从缓存中获取栏目列表
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    List<Cms_channel> listChannel(String parentId, String parentCode);

    /**
     * 清空缓存
     */
    void clearCache();
}
