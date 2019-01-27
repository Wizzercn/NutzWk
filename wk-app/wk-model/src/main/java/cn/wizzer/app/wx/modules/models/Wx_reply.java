package cn.wizzer.app.wx.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_reply")
public class Wx_reply extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("单位id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sys_unit_id;

    @Column
    @Comment("回复类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String msgType;

    @Column
    @Comment("关键词")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String keyword;

    @Column
    @Comment("回复内容")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String content;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;

    @One(field = "content", key = "id")
    private Wx_reply_img replyImg;

    @One(field = "content", key = "id")
    private Wx_reply_news replyNews;

    @One(field = "content", key = "id")
    private Wx_reply_txt replyTxt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public Wx_config getWxConfig() {
        return wxConfig;
    }

    public void setWxConfig(Wx_config wxConfig) {
        this.wxConfig = wxConfig;
    }

    public String getSys_unit_id() {
        return sys_unit_id;
    }

    public void setSys_unit_id(String sys_unit_id) {
        this.sys_unit_id = sys_unit_id;
    }

    public Wx_reply_img getReplyImg() {
        return replyImg;
    }

    public void setReplyImg(Wx_reply_img replyImg) {
        this.replyImg = replyImg;
    }

    public Wx_reply_news getReplyNews() {
        return replyNews;
    }

    public void setReplyNews(Wx_reply_news replyNews) {
        this.replyNews = replyNews;
    }

    public Wx_reply_txt getReplyTxt() {
        return replyTxt;
    }

    public void setReplyTxt(Wx_reply_txt replyTxt) {
        this.replyTxt = replyTxt;
    }
}
