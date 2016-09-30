package cn.wizzer.modules.models.wx;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_msg")
public class Wx_msg extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("openid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String openid;

    @Column
    @Comment("微信昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("信息类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;

    @Column
    @Comment("信息内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("回复ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String replyId;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(target = Wx_msg_reply.class, field = "replyId")
    private Wx_msg_reply reply;

    @One(target = Wx_config.class, field = "wxid")
    private Wx_config wxConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
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

    public Wx_msg_reply getReply() {
        return reply;
    }

    public void setReply(Wx_msg_reply reply) {
        this.reply = reply;
    }
}
