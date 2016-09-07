package cn.wizzer.modules.controllers.platform.wx;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.wx.Wx_config;
import cn.wizzer.modules.models.wx.Wx_msg;
import cn.wizzer.modules.models.wx.Wx_msg_reply;
import cn.wizzer.modules.services.wx.WxConfigService;
import cn.wizzer.modules.services.wx.WxMsgReplyService;
import cn.wizzer.modules.services.wx.WxMsgService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.*;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/8.
 */
@IocBean
@At("/platform/wx/msg/user")
@Filters({@By(type = PrivateFilter.class)})
public class WxMsgUserController {
    private static final Log log = Logs.get();
    @Inject
    WxMsgService wxMsgService;
    @Inject
    WxConfigService wxConfigService;
    @Inject
    WxMsgReplyService wxMsgReplyService;

    @At({"/", "/?"})
    @Ok("beetl:/platform/wx/msg/user/index.html")
    @RequiresAuthentication
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
    }

    @At({"/data/","/data/?"})
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(String wxid, @Param("nickname") String nickname, @Param("content") String content, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        if (!Strings.isBlank(content)) {
            cnd.and("content", "like", "%" + content + "%");
        }
        return wxMsgService.data(length, start, draw, order, columns, cnd, null);
    }

    @At({"/reply/?"})
    @Ok("beetl:/platform/wx/msg/user/reply.html")
    @RequiresAuthentication
    public Object reply(String id, @Param("type") int type, HttpServletRequest req) {
        Wx_msg msg = wxMsgService.fetch(id);
        req.setAttribute("wxid", msg.getWxid());
        req.setAttribute("type", type);
        return msg;
    }


    @At("/replyData/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object replyData(String wxid, @Param("openid") String openid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(openid)) {
            cnd.and("openid", "=", openid);
        }
        cnd.desc("opAt");
        return wxMsgService.data(5, start, draw, order, columns, cnd, "reply");
    }


    @At("/replyDo")
    @Ok("json")
    @RequiresPermissions("wx.user.list.sync")
    @SLog(tag = "回复微信", msg = "微信昵称:${args[1]}")
    public Object down(@Param("id") String id, @Param("nickname") String nickname, @Param("wxid") String wxid, @Param("openid") String openid, @Param("content") String content, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            long now = System.currentTimeMillis() / 1000;
            WxOutMsg msg = new WxOutMsg();
            msg.setCreateTime(now);
            msg.setFromUserName(config.getAppid());
            msg.setMsgType("text");
            msg.setToUserName(openid);
            msg.setContent(content);
            WxResp wxResp = wxApi2.send(msg);
            if (wxResp.errcode() != 0) {
                return Result.error(wxResp.errmsg());
            }
            Wx_msg_reply reply = new Wx_msg_reply();
            reply.setContent(content);
            reply.setType("text");
            reply.setMsgid(id);
            reply.setOpenid(openid);
            reply.setWxid(wxid);
            Wx_msg_reply reply1 = wxMsgReplyService.insert(reply);
            if (reply1 != null) {
                wxMsgService.update(org.nutz.dao.Chain.make("replyId", reply1.getId()), Cnd.where("id", "=", id));
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
