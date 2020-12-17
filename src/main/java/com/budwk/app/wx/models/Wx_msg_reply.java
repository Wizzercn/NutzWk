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
@Table("wx_msg_reply")
public class Wx_msg_reply extends BaseModel implements Serializable {
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
    @Comment("msgid")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String msgid;

    @Column
    @Comment("openid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String openid;

    @Column
    @Comment("信息类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;

    @Column
    @Comment("信息内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

}
