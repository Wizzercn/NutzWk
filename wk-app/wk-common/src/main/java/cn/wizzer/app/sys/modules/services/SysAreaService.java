package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_area;
import cn.wizzer.framework.base.service.BaseService;

public interface SysAreaService extends BaseService<Sys_area> {
    /**
     * 保存地区
     * @param shoparea
     * @param pid
     */
    void save(Sys_area shoparea, String pid);

    /**
     * 级联删除地区
     * @param shoparea
     */
    void deleteAndChild(Sys_area shoparea);

    /**
     * 通过地区编码获取名称
     * @param code
     * @return
     */
    String getNameByCode(String code);
}
