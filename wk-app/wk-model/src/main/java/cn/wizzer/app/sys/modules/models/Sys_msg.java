package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wizzer on 2018/6/29.
 */
@Table("sys_msg")
public class Sys_msg extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;

    @Column
    @Comment("消息标题")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String title;

    @Column
    @Comment("消息内容")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String note;

    @Column
    @Comment("消息URL")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

    @Column
    @Comment("发送时间")
    @ColDefine(type = ColType.INT)
    private Long sendAt;

    @Many(field = "msgId")
    private List<Sys_msg_user> userList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSendAt() {
        return sendAt;
    }

    public void setSendAt(Long sendAt) {
        this.sendAt = sendAt;
    }

    public List<Sys_msg_user> getUserList() {
        return userList;
    }

    public void setUserList(List<Sys_msg_user> userList) {
        this.userList = userList;
    }
}
