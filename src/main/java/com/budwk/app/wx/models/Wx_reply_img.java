package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2018/11/7.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_reply_img")
@TableMeta("{'mysql-charset':'utf8mb4'}")
public class Wx_reply_img extends BaseModel implements Serializable {
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
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @Column
    @Comment("图片地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String picurl;

    @Column
    @Comment("媒体ID")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String mediaId;

}
