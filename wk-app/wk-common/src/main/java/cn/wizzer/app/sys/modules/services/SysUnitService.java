package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_unit;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysUnitService extends BaseService<Sys_unit> {
    void save(Sys_unit unit, String pid);

    void deleteAndChild(Sys_unit unit);
}
