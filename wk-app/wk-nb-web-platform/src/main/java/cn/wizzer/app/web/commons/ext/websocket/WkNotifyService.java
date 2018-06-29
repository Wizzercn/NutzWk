package cn.wizzer.app.web.commons.ext.websocket;

import cn.wizzer.app.sys.modules.models.Sys_msg;
import cn.wizzer.app.sys.modules.models.Sys_msg_user;
import cn.wizzer.app.sys.modules.services.SysMsgService;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import cn.wizzer.app.web.commons.base.Globals;
import com.alibaba.dubbo.config.annotation.Reference;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.integration.jedis.RedisService;
import org.nutz.integration.jedis.pubsub.PubSubService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wizzer on 2018/6/28.
 */
@IocBean
public class WkNotifyService {
    private static final Log log = Logs.get();
    @Inject
    private PubSubService pubSubService;
    @Inject
    @Reference
    private SysMsgService sysMsgService;
    @Inject
    @Reference
    private SysMsgUserService sysMsgUserService;
    @Inject
    private RedisService redisService;

    private static NutMap typeMap = NutMap.NEW().addv("system", "系统消息").addv("user", "用户消息");

    @Async
    public void notify(Sys_msg innerMsg, String rooms[]) {
        String url = "/platform/msg/all";
        if (Strings.isNotBlank(innerMsg.getUrl())) {
            url = innerMsg.getUrl();
        }
        NutMap map = new NutMap();
        map.put("action", "notify");
        map.put("title", "您有新的消息");
        map.put("body", innerMsg.getTitle());
        map.put("url", url);
        String msg = Json.toJson(map, JsonFormat.compact());
        if ("system".equals(innerMsg.getType())) {//系统消息发送给所有在线用户
            Set<String> keys = redisService.keys("wsroom:*");
            for (String room : keys) {
                pubSubService.fire(room, msg);
            }
        } else if ("user".equals(innerMsg.getType())) {//用户消息发送给指定在线用户
            for (String room : rooms) {
                pubSubService.fire("wsroom:" + room, msg);
            }
        }
    }

    @Async
    public void innerMsg(String room, int size, List<NutMap> list) {
        NutMap map = new NutMap();
        map.put("action", "innerMsg");
        map.put("size", size);//未读消息数
        map.put("list", list);//最新3条消息列表  type--系统消息/用户消息  title--标题  time--时间戳
        String msg = Json.toJson(map, JsonFormat.compact());
        log.debug("msg::::" + msg);
        pubSubService.fire("wsroom:" + room, msg);
    }

    @Async
    public void getMsg(String loginname) {
        try {
            //通过用户名查询未读消息
            int size = sysMsgUserService.count(Cnd.where("delFlag", "=", false).and("loginname", "=", loginname)
                    .and("status", "=", 0));
            List<Sys_msg_user> list = sysMsgUserService.query(Cnd.where("delFlag", "=", false).and("loginname", "=", loginname)
                    , "msg", Cnd.orderBy().desc("sendAt"), new Pager().setPageNumber(1).setPageSize(5));
            List<NutMap> mapList = new ArrayList<>();
            for (Sys_msg_user msgUser : list) {
                String url = "/platform/msg/all/detail/" + msgUser.getMsgId();
                if (Strings.isNotBlank(msgUser.getMsg().getUrl())) {
                    url = msgUser.getMsg().getUrl();
                }
                mapList.add(NutMap.NEW().addv("msgId", msgUser.getMsgId()).addv("type", typeMap.getString(msgUser.getMsg().getType()))
                        .addv("title", msgUser.getMsg().getTitle())
                        .addv("url", url)
                        .addv("time", Times.format("yyyy-MM-dd HH:mm", Times.D(1000 * msgUser.getMsg().getSendAt()))));
            }
            innerMsg(loginname, size, mapList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
