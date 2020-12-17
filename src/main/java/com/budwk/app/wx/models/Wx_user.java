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
@Table("wx_user")
@TableIndexes({@Index(name = "INDEX_WX_USER_OPENID", fields = {"openid"}, unique = true)})
public class Wx_user extends BaseModel implements Serializable {
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
    @Comment("unionid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String unionid;

    @Column
    @Comment("微信昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("是否关注")
    private boolean subscribe;

    @Column
    @Comment("关注时间")
    //Long不要用ColDefine定义,兼容oracle/mysql,支持2038年以后的时间戳
    private Long subscribeAt;

    @Column
    @Comment("性别")
    @ColDefine(type = ColType.INT)
    private Integer sex;

    @Column
    @Comment("国家")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String country;

    @Column
    @Comment("省份")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String province;

    @Column
    @Comment("城市")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String city;

    @Column
    @Comment("头像")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String headimgurl;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;

}
