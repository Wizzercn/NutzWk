package cn.wizzer.app.web.commons.ext.wx;

import cn.wizzer.app.wx.modules.models.*;
import cn.wizzer.app.wx.modules.services.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.AbstractWxHandler;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.AesException;
import org.nutz.weixin.repo.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean(name = "wxHandler",singleton = false)
public class WxHandler extends AbstractWxHandler {
    private final static Log log = Logs.get();
    protected String token;
    protected String aeskey;
    protected WXBizMsgCrypt msgCrypt;
    protected String appid;
    protected WxApi2 api;
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    @Reference
    private WxUserService wxUserService;
    @Inject
    @Reference
    private WxReplyService wxReplyService;
    @Inject
    @Reference
    private WxReplyNewsService wxReplyNewsService;
    @Inject
    @Reference
    private WxReplyTxtService wxReplyTxtService;
    @Inject
    @Reference
    private WxReplyImgService wxReplyImgService;
    @Inject
    @Reference
    private WxMsgService wxMsgService;
    @Inject
    private WxService wxService;

    public boolean check(String signature, String timestamp, String nonce, String key) {
        Wx_config appInfo = wxConfigService.fetch(Cnd.where("id", "=", key));
        if (appInfo != null) {
            this.token = appInfo.getToken();
            this.aeskey = appInfo.getEncodingAESKey();
            this.appid = appInfo.getAppid();
            return Wxs.check(appInfo.getToken(), signature, timestamp, nonce);
        }
        return false;
    }

    public WXBizMsgCrypt getMsgCrypt() {
        if (this.msgCrypt == null) {
            try {
                // 若抛异常Illegal key size ,需更新JDK的加密库为不限制长度
                this.msgCrypt = new WXBizMsgCrypt(this.token, this.aeskey, this.appid);
            } catch (AesException var2) {
                throw new RuntimeException(var2);
            }
        }

        return this.msgCrypt;
    }

    // 用户发送的是文本的时候调用这个方法
    public WxOutMsg text(WxInMsg msg) {
        Wx_reply reply = wxReplyService.fetch(Cnd.where("wxid", "=", msg.getExtkey()).and("type", "=", "keyword").and("keyword", "=", msg.getContent()));
        if (reply != null) {
            if ("txt".equals(reply.getMsgType())) {
                String txtId = reply.getContent();
                Wx_reply_txt txt = wxReplyTxtService.fetch(txtId);
                return Wxs.respText(null, txt == null ? "" : txt.getContent());
            } else if ("image".equals(reply.getMsgType())) {
                String imgId = reply.getContent();
                Wx_reply_img img = wxReplyImgService.fetch(imgId);
                return Wxs.respImage(null, img.getMediaId());
            } else if ("news".equals(reply.getMsgType())) {
                String[] newsIds = StringUtils.split(Strings.sNull(reply.getContent()), ",");
                List<WxArticle> list = new ArrayList<>();
                List<Wx_reply_news> newsList = wxReplyNewsService.query(Cnd.where("id", "in", newsIds).asc("location"));
                for (Wx_reply_news news : newsList) {
                    WxArticle wxArticle = new WxArticle();
                    wxArticle.setDescription(news.getDescription());
                    wxArticle.setPicUrl(news.getPicUrl());
                    wxArticle.setTitle(news.getTitle());
                    wxArticle.setUrl(news.getUrl());
                    list.add(wxArticle);
                }
                return Wxs.respNews(null, list);
            }
        }
        Wx_user usr = wxUserService.fetch(Cnd.where("openid", "=", msg.getFromUserName()));
        Wx_msg wxMsg = new Wx_msg();
        wxMsg.setOpenid(msg.getFromUserName());
        wxMsg.setContent(EmojiParser.parseToAliases(msg.getContent(), EmojiParser.FitzpatrickAction.REMOVE));
        wxMsg.setWxid(msg.getExtkey());
        wxMsg.setType("txt");
        wxMsg.setNickname(usr == null ? "匿名" : usr.getNickname());
        wxMsg.setDelFlag(false);
        wxMsgService.insert(wxMsg);
        return Wxs.respText(null, "您的留言已收到！");
    }

    public WxOutMsg eventClick(WxInMsg msg) {
        String eventKey = msg.getEventKey();
        log.debug("eventKey: " + eventKey);
        log.debug("extKey: " + msg.getExtkey());
        Wx_reply reply = wxReplyService.fetch(Cnd.where("type", "=", "keyword").and("wxid", "=", msg.getExtkey()).and("keyword", "=", eventKey));
        if (reply != null) {
            if ("txt".equals(reply.getMsgType())) {
                String txtId = reply.getContent();
                Wx_reply_txt txt = wxReplyTxtService.fetch(txtId);
                return Wxs.respText(null, txt == null ? "" : txt.getContent());
            } else if ("image".equals(reply.getMsgType())) {
                String imgId = reply.getContent();
                Wx_reply_img img = wxReplyImgService.fetch(imgId);
                return Wxs.respImage(null, img.getMediaId());
            } else if ("news".equals(reply.getMsgType())) {
                String[] newsIds = StringUtils.split(Strings.sNull(reply.getContent()), ",");
                List<WxArticle> list = new ArrayList<>();
                List<Wx_reply_news> newsList = wxReplyNewsService.query(Cnd.where("id", "in", newsIds).asc("location"));
                for (Wx_reply_news news : newsList) {
                    WxArticle wxArticle = new WxArticle();
                    wxArticle.setDescription(news.getDescription());
                    wxArticle.setPicUrl(news.getPicUrl());
                    wxArticle.setTitle(news.getTitle());
                    wxArticle.setUrl(news.getUrl());
                    list.add(wxArticle);
                }
                return Wxs.respNews(null, list);
            }
        }
        return defaultMsg(msg);
    }

    @Override
    public WxOutMsg eventSubscribe(WxInMsg msg) {
        if (api == null)
            api = wxService.getWxApi2(msg.getExtkey());
        Wx_user usr = wxUserService.fetch(Cnd.where("openid", "=", msg.getFromUserName()));
        WxResp resp = api.user_info(msg.getFromUserName(), "zh_CN");
        if (usr == null) {
            usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
            usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
            usr.setSubscribeAt(Times.getTS());
            usr.setWxid(msg.getExtkey());
            wxUserService.insert(usr);
        } else {
            String id = usr.getId();
            usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
            usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
            usr.setOpAt(Times.getTS());
            usr.setWxid(msg.getExtkey());
            usr.setId(id);
            wxUserService.updateIgnoreNull(usr);
        }
        Wx_reply reply = wxReplyService.fetch(Cnd.where("wxid", "=", msg.getExtkey()).and("type", "=", "follow"));
        if (reply != null) {
            if ("txt".equals(reply.getMsgType())) {
                String txtId = reply.getContent();
                Wx_reply_txt txt = wxReplyTxtService.fetch(txtId);
                return Wxs.respText(null, txt == null ? "" : txt.getContent());
            } else if ("image".equals(reply.getMsgType())) {
                String imgId = reply.getContent();
                Wx_reply_img img = wxReplyImgService.fetch(imgId);
                return Wxs.respImage(null, img.getMediaId());
            } else if ("news".equals(reply.getMsgType())) {
                String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
                List<WxArticle> list = new ArrayList<>();
                List<Wx_reply_news> newsList = wxReplyNewsService.query(Cnd.where("id", "in", newsIds).asc("location"));
                for (Wx_reply_news news : newsList) {
                    WxArticle wxArticle = new WxArticle();
                    wxArticle.setDescription(news.getDescription());
                    wxArticle.setPicUrl(news.getPicUrl());
                    wxArticle.setTitle(news.getTitle());
                    wxArticle.setUrl(news.getUrl());
                    list.add(wxArticle);
                }
                return Wxs.respNews(null, list);
            }
        }
        return Wxs.respText(null, "谢谢您的关注！");
    }

    @Override
    public WxOutMsg eventUnsubscribe(WxInMsg msg) {
        Wx_user usr = wxUserService.fetch(Cnd.where("openid", "=", msg.getFromUserName()));
        if (usr != null) {
            wxUserService.update(Chain.make("subscribe", false).add("opAt", Times.getTS()), Cnd.where("openid", "=", msg.getFromUserName()));
        }
        return super.eventUnsubscribe(msg);
    }

    @Override
    public WxOutMsg defaultMsg(WxInMsg msg) {
        return Wxs.respText("这是缺省回复哦.你发送的类型是:" + msg.getMsgType());
    }
}