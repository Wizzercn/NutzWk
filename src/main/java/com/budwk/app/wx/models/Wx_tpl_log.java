package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/8/5.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_tpl_log")
public class Wx_tpl_log extends BaseModel implements Serializable {
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
    @Comment("openid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String openid;

    @Column
    @Comment("微信昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("发送内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("发送状态")
    @ColDefine(type = ColType.INT)
    private int status;//0 默认 1 成功 2 失败

    @Column
    @Comment("发送结果")
    @ColDefine(type = ColType.TEXT)
    private String result;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

}
