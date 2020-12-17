package com.budwk.app.sys.services;

import com.budwk.app.sys.models.Sys_api;
import com.budwk.app.base.service.BaseService;

public interface SysApiService extends BaseService<Sys_api> {
    /**
     * 创建密钥
     */
    void createAppkey(String name, String userId) throws Exception;

    /**
     * 删除密钥
     */
    void deleteAppkey(String appid) throws Exception;

    /**
     * 启用禁用
     */
    void updateAppkey(String appid, boolean disabled) throws Exception;

    /**
     * 通过appid获取appkey
     *
     * @param appid appid
     * @return Sys_api
     */
    String getAppkey(String appid);

    /**
     * 删除缓存
     *
     * @param appid appid
     */
    void deleteCache(String appid);

    /**
     * 清空缓存
     */
    void clearCache();
}
