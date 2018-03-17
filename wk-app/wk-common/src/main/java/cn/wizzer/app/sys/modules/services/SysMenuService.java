package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.framework.base.service.BaseService;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysMenuService extends BaseService<Sys_menu> {
    /**
     * 保存菜单
     * @param menu
     * @param pid
     */
    void save(Sys_menu menu, String pid);

    /**
     * 级联删除菜单
     * @param unit
     */
    void deleteAndChild(Sys_menu unit);
}
