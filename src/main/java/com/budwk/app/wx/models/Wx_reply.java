package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_reply")
public class Wx_reply extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
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


}
