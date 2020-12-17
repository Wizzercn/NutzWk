package com.budwk.app.sys.services;

import com.budwk.app.base.service.BaseService;
import com.budwk.app.sys.models.Sys_msg;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface SysMsgService extends BaseService<Sys_msg> {
    /**
     * 保存信息同时发送
     *
     * @param sysMsg
     * @param users
     */
    Sys_msg saveMsg(Sys_msg sysMsg, String[] users);

    /**
     * 删除消息及消息用户表数据
     *
     * @param id
     */
    void deleteMsg(String id);


    /**
     * 通知客户端弹窗
     *
     * @param innerMsg
     * @param rooms
     */
    void notify(Sys_msg innerMsg, String rooms[]);

    /**
     * 通知客户端有新消息及消息数量
     *
     * @param room 用户名
     * @param size
     * @param list
     */
    void innerMsg(String room, int size, List<NutMap> list);

    /**
     * 获取某用户的未读消息数量及列表
     *
     * @param loginname 用户名
     */
    void getMsg(String loginname);

    /**
     * 通知下线
     *
     * @param loginname     用户名
     * @param httpSessionId
     */
    void offline(String loginname, String httpSessionId);

    /**
     * 向用户发送站内消息
     *
     * @param loginname 用户名
     * @param title     标题
     * @param body      内容
     * @param sender    发送者
     */
    void sendMsg(String loginname, String title, String body, String sender);
}
