package com.budwk.app.sys.services;

import com.budwk.app.sys.models.Sys_config;
import com.budwk.app.base.service.BaseService;

import java.util.List;

/**
 * Created by wizzer on 2016/12/23.
 */
public interface SysConfigService extends BaseService<Sys_config> {
    /**
     * 查询所有数据
     * @return
     */
    List<Sys_config> getAllList();
}
