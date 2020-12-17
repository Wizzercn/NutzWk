package com.budwk.app.web.commons.ext.websocket;

import com.budwk.app.base.constant.RedisConstant;
import com.budwk.app.sys.services.SysMsgService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.plugins.mvc.websocket.handler.SimpleWsHandler;

@IocBean
public class WkWsHandler extends SimpleWsHandler {
    private static final Log log = Logs.get();
    @Inject
    private SysMsgService sysMsgService;

    @Override
    public void join(NutMap req) {
        join(req.getString("room"));
    }

    @Override
    public void left(NutMap req) {
        left(req.getString("room"));
    }

    @Override
    public void join(String room) {
        if (!Strings.isBlank(room)) {
            room = RedisConstant.REDIS_KEY_WSROOM + room;
            log.debugf("session(id=%s) join room(name=%s)", session.getId(), room);
            roomProvider.join(room, session.getId());
            sysMsgService.getMsg(room.split(":")[1]);
        }
    }

    @Override
    public void left(String room) {
        if (!Strings.isBlank(room)) {
            room = RedisConstant.REDIS_KEY_WSROOM + room;
            log.debugf("session(id=%s) left room(name=%s)", session.getId(), room);
            roomProvider.left(room, session.getId());
        }
    }

    @Override
    public void depose() {
        //覆盖原生写法,因为room= loginname + httpSessionId 和聊天室的机制不一样,不覆盖的话功能会异常
    }
}
