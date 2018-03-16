package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_area;
import cn.wizzer.framework.base.service.BaseService;

public interface SysAreaService extends BaseService<Sys_area> {
    void save(Sys_area shoparea, String pid);
    void deleteAndChild(Sys_area shoparea);
    String getNameByCode(String code);
}
