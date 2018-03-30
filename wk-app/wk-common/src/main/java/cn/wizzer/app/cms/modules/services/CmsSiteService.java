package cn.wizzer.app.cms.modules.services;

import cn.wizzer.framework.base.service.BaseService;

import org.nutz.boot.starter.literpc.annotation.RpcService;

import cn.wizzer.app.cms.modules.models.Cms_site;

@RpcService
public interface CmsSiteService extends BaseService<Cms_site> {
    /**
     * 通过编码获取站点信息
     *
     * @param code
     * @return
     */
    Cms_site getSite(String code);

    /**
     * 清空缓存
     */
    void clearCache();
}
