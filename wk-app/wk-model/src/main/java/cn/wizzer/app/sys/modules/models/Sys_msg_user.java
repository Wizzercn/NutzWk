package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2018/6/29.
 */
@Table("sys_msg_user")
public class Sys_msg_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("消息ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String msgId;

    @Column
    @Comment("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String loginname;

    @Column
    @Comment("消息状态")
    @ColDefine(type = ColType.INT)
    private int status;//0--未读  1--已读

    @Column
    @Comment("读取时间")
    @ColDefine(type = ColType.INT)
    private Long readAt;

    @One(field = "msgId")
    private Sys_msg msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getReadAt() {
        return readAt;
    }

    public void setReadAt(Long readAt) {
        this.readAt = readAt;
    }

    public Sys_msg getMsg() {
        return msg;
    }

    public void setMsg(Sys_msg msg) {
        this.msg = msg;
    }
}
