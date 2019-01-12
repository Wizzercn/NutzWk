package cn.wizzer.app.sys.modules.services;

import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.framework.base.service.BaseService;

import java.util.List;

public interface SysMsgUserService extends BaseService<Sys_msg_user> {

    /**
     * 获取未读消息数量
     *
     * @param loginname
     * @return
     */
    int getUnreadNum(String loginname);

    /**
     * 获取未读消息列表
     *
     * @param loginname
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<Sys_msg_user> getUnreadList(String loginname, int pageNumber, int pageSize);

    /**
     * 删除用户缓存
     *
     * @param loginname
     */
    void deleteCache(String loginname);

    /**
     * 清空缓存
     */
    void clearCache();
}
