package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/1.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_config")
public class Wx_config extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;

    @Column
    @Comment("公众号名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String appname;

    @Column
    @Comment("原始ID")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String ghid;

    @Column
    @Comment("Appid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String appid;

    @Column
    @Comment("Appsecret")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String appsecret;

    @Column
    @Comment("单位id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sys_unit_id;

    @Column
    @Comment("EncodingAESKey")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String encodingAESKey;

    @Column
    @Comment("Token")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String token;

    @Column
    @Comment("access_token")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String access_token;

    @Column
    @Comment("access_token_expires")
    @ColDefine(type = ColType.INT)
    private Integer access_token_expires;

    @Column
    @Comment("access_token_lastat")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String access_token_lastat;

    @Column
    @Comment("禁用支付")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean payEnabled;

    @Column
    @Comment("支付信息")
    @ColDefine(type = ColType.TEXT)
    private String payInfo;

}
