package cn.wizzer.modules.back.sys.models;

import java.io.Serializable;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import cn.wizzer.common.base.Model;

/**
 * qq群用户提交的黑名单
 */
@Table("sys_qun_black_user")
@TableIndexes({@Index(name = "INDEX_SYS_QUN_BLACK_CONTACT", fields = {"contact"})})
public class Sys_qun_black_user extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("联系方式")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String contact;

    @Column
    @Comment("昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String nickname;
    
    @Column
    @Comment("详情")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String text;
    
    @Column
    @Comment("创建时间")
    protected Integer createdAt;
     
    @Column
    @Comment("最新活跃时间")
    protected Integer loginAt;
    
    @Column
    @Comment("禁用时间")
    protected Integer disabledAt;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Integer createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getDisabledAt() {
		return disabledAt;
	}

	public void setDisabledAt(Integer disabledAt) {
		this.disabledAt = disabledAt;
	}

	public Integer getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Integer loginAt) {
		this.loginAt = loginAt;
	}
	

}
