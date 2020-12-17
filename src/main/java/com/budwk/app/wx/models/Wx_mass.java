package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/1.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_mass")
public class Wx_mass extends BaseModel implements Serializable {
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
    @Comment("群发名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("群发类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;//text image news

    @Column
    @Comment("媒体文件ID")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String media_id;

    @Column
    @Comment("图片地址")//type=image
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String picurl;

    @Column
    @Comment("Scope")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String scope;

    @Column
    @Comment("Content")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("发送状态")
    @ColDefine(type = ColType.INT)
    protected Integer status;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;

    @One(field = "id",key = "massId")
    private Wx_mass_send massSend;

}
