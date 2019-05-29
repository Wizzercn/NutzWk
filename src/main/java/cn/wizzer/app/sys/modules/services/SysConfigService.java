package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_config;
import cn.wizzer.framework.base.service.BaseService;

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
