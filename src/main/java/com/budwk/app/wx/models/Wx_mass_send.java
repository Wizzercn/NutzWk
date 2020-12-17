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
@Table("wx_mass_send")
public class Wx_mass_send extends BaseModel implements Serializable {
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

    public String getSys_unit_id() {
        return sys_unit_id;
    }

    public void setSys_unit_id(String sys_unit_id) {
        this.sys_unit_id = sys_unit_id;
    }

    @Column
    @Comment("群发ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String massId;

    @Column
    @Comment("Openid列表")
    @ColDefine(type = ColType.TEXT)
    private String receivers;

    @Column
    @Comment("发送状态")
    @ColDefine(type = ColType.INT)
    protected Integer status;

    @Column
    @Comment("msgId")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String msgId;

    @Column
    @Comment("errCode")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String errCode;

    @Column
    @Comment("errMsg")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String errMsg;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;


}
