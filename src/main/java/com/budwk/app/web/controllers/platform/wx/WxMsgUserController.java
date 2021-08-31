package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_msg_reply;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxMsgReplyService;
import com.budwk.app.wx.services.WxMsgService;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.base.result.Result;;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
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
public class WxMsgUserController {
    private static final Log log = Logs.get();
    @Inject
    private WxMsgService wxMsgService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxMsgReplyService wxMsgReplyService;
    @Inject
    private WxService wxService;

    @At({"/", "/?"})
    @Ok("beetl:/platform/wx/msg/user/index.html")
    @SaCheckPermission("wx.user.list")
    public void index(String wxid, HttpServletRequest req) {
        Wx_config wxConfig = null;
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxConfig = list.get(0);
        }
        if (Strings.isNotBlank(wxid)) {
            wxConfig = wxConfigService.fetch(wxid);
        }
        req.setAttribute("wxConfig", wxConfig);
        req.setAttribute("wxList", list);
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("wx.user.list")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxMsgService.listPageLinks(pageNumber, pageSize, cnd, "reply"));
    }

    @At({"/replyDo/?"})
    @Ok("json")
    @SaCheckPermission("wx.user.list.sync")
    @SLog(tag = "回复微信", msg = "微信昵称:${args[2]}")
    public Object replyDo(String wxid, @Param("msgid") String msgid, @Param("nickname") String nickname, @Param("openid") String openid, @Param("replyContent") String content, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            WxApi2 wxApi2 = wxService.getWxApi2(wxid);
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
            reply.setMsgid(msgid);
            reply.setOpenid(openid);
            reply.setWxid(wxid);
            Wx_msg_reply reply1 = wxMsgReplyService.insert(reply);
            if (reply1 != null) {
                wxMsgService.update(org.nutz.dao.Chain.make("replyId", reply1.getId()), Cnd.where("id", "=", msgid));
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
