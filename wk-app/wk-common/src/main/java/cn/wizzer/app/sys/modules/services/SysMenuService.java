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
     * 编辑菜单
     *
     * @param menu
     * @param pid
     */
    void edit(Sys_menu menu, String pid, List<NutMap> datas);

    /**
     * 级联删除菜单
     *
     * @param menu
     */
    void deleteAndChild(Sys_menu menu);

    /**
     * 获取左侧菜单
     *
     * @param href
     * @return
     */
    Sys_menu getLeftMenu(String href);

    /**
     * 获取左侧菜单路径
     *
     * @param list
     * @return
     */
    Sys_menu getLeftPathMenu(List<String> list);

    /**
     * 清空缓存
     */
    void clearCache();
}
