package com.budwk.app.web.controllers.front.wx;

import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_user;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxUserService;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.vdurmont.emoji.EmojiParser;
import org.nutz.dao.Cnd;
import org.nutz.http.Http;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 * Created by wizzer on 2016/8/6.
 */
@IocBean
@At("/public/wechat")
public class WechatController {
    private static final Log log = Logs.get();
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxUserService wxUserService;
    @Inject
    private WxService wxService;

    @At("/?/oauth")
    @Ok("re")
    @Filters
    public String oauth(String wxid, @Param(value = "goto_url", df = "/public/wx/cms/channel") String goto_url, HttpServletRequest req, HttpSession session) throws Exception {
        session.setAttribute("wechat_goto_url", goto_url);
        if (!Strings.isBlank(wxid)) {
            Wx_config config = wxConfigService.fetch(wxid);
            String back_url = Globals.AppDomain + "/public/wechat/" + wxid + "/back";
            String redirect_uri = URLEncoder.encode(back_url, "utf-8");
            log.debug("redirect_uri::" + redirect_uri);
            String state = "wechat";
            String scope = "snsapi_base";
            String oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + config.getAppid() + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=" + scope + "&state=" + state + "#wechat_redirect";
            return "redirect:" + oauth_url;
        } else {
            return "redirect:" + goto_url;
        }
    }

    @At("/?/back")
    @Ok("re")
    @Filters
    public String back(String wxid, @Param("code") String code, HttpServletRequest req, HttpSession session) throws Exception {
        String wechat_goto_url = Strings.sNull(session.getAttribute("wechat_goto_url"));
        Wx_config config = wxConfigService.fetch(wxid);
        String fmt = "https://api.weixin.qq.com/sns/oauth2/access_token"
                + "?appid=%s"
                + "&secret=%s"
                + "&code=%s"
                + "&grant_type=authorization_code";

        String url = String.format(fmt, config.getAppid(), config.getAppsecret(), code);
        String json = Http.get(url).getContent();
        NutMap map = Json.fromJson(NutMap.class, json);
        String openid = map.getString("openid");
        WxApi2 wxApi2 = wxService.getWxApi2(wxid);
        Wx_user usr = wxUserService.fetch(Cnd.where("openid", "=", openid).and("wxid", "=", wxid));
        WxResp resp = wxApi2.user_info(openid, "zh_CN");
        if (usr == null) {
            usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
            if (usr != null && usr.getNickname() != null) {
                usr.setNickname(Strings.sNull(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE)));
                usr.setSubscribeAt(resp.user().getSubscribe_time());
                usr.setWxid(wxid);
                wxUserService.insert(usr);
            }
        } else {
            String id = usr.getId();
            usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
            usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
            usr.setUpdatedAt(System.currentTimeMillis());
            usr.setWxid(wxid);
            usr.setId(id);
            wxUserService.updateIgnoreNull(usr);
        }
        session.setAttribute("wxid", wxid);
        session.setAttribute("openid", usr == null ? "" : usr.getOpenid());
        return "redirect:" + wechat_goto_url;
    }

}
