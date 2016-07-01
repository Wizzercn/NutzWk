package cn.wizzer.modules.gm.wx.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_reply")
public class Wx_reply extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("回复类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String msgtype;

    @Column
    @Comment("关键词")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String keyword;

    @Column
    @Comment("回复内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(target = Wx_config.class, field = "wxid")
    private Wx_config wxConfig;

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

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
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
}
