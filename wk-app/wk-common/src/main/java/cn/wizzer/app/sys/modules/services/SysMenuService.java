package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_menu;
import cn.wizzer.framework.base.service.BaseService;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * Created by wizzer on 2016/12/22.
 */
public interface SysMenuService extends BaseService<Sys_menu> {
    /**
     * 保存菜单
     *
     * @param menu
     * @param pid
     */
    void save(Sys_menu menu, String pid, List<NutMap> datas);

    /**
     * 级联删除菜单
     *
     * @param menu
     */
    void deleteAndChild(Sys_menu menu);
}
