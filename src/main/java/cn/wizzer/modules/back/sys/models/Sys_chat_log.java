package cn.wizzer.modules.back.sys.models;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;

import cn.wizzer.common.base.Model;
/**
 * qq群聊天记录
 */
@Table("sys_chat_log")
public class Sys_chat_log extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("群名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String groupName;
    @Column
    @Comment("群Id")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String groupId;
    
    @Column
    @Comment("发送人昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String sender;
    
    @Column
    @Comment("发送人昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String senderName;
    
    @Column
    @Comment("消息")
    @ColDefine(type = ColType.TEXT)
    private String message;
    
    @Column
    @Comment("创建时间")
    protected Integer createdAt;
     
    
    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
 
	public Integer getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}

}
