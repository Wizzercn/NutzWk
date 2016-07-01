package cn.wizzer.modules.gm.wx.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_mass_send")
public class Wx_mass_send extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

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
    @ColDefine(type = ColType.INT, width = 1)
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

    @One(target = Wx_config.class, field = "wxid")
    private Wx_config wxConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMassId() {
        return massId;
    }

    public void setMassId(String massId) {
        this.massId = massId;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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
