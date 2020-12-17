package com.budwk.app.sys.services;

import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_dict;

import java.util.List;
import java.util.Map;

public interface SysDictService extends BaseService<Sys_dict> {
    /**
     * 通过code获取名称
     *
     * @param code
     * @return
     */
    String getNameByCode(String code);

    /**
     * 通过ID获取名称
     *
     * @param id
     * @return
     */
    String getNameById(String id);

    /**
     * 通过树PATH获取子级
     *
     * @param path
     * @return
     */
    List<Sys_dict> getSubListByPath(String path);

    /**
     * 通过ID获取子级
     *
     * @param id
     * @return
     */
    List<Sys_dict> getSubListById(String id);

    /**
     * 通过code获取子级
     *
     * @param code
     * @return
     */
    List<Sys_dict> getSubListByCode(String code);

    /**
     * 通过树PATH获取子级
     *
     * @param path
     * @return
     */
    Map getSubMapByPath(String path);

    /**
     * 通过ID获取子级
     *
     * @param id
     * @return
     */
    Map getSubMapById(String id);

    /**
     * 通过code获取子级
     *
     * @param code
     * @return
     */
    Map getSubMapByCode(String code);

    /**
     * 保存数据字典
     *
     * @param dict
     * @param pid
     */
    void save(Sys_dict dict, String pid);

    /**
     * 级联删除数据
     *
     * @param dict
     */
    void deleteAndChild(Sys_dict dict);

    /**
     * 清空缓存
     */
    void clearCache();
}
