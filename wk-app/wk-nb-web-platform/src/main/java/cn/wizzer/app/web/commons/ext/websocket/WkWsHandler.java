package cn.wizzer.app.web.commons.ext.websocket;

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
    private WkNotifyService wkNotifyService;
    /**
     * 获取通知 对应的消息是  {action:"msg", room:"superadmin"}
     */
    public void msg(NutMap req) {
        msg(req.getString("room"));
    }

    public void msg(String room) {
        if (!Strings.isBlank(room)) {
            wkNotifyService.getMsg(room);
        }
    }
}
