package cn.wizzer.modules.sys.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * @author Wizzer.cn
 * @time 2013-12-1 上午10:54:04
 * 
 */
@Table("sys_user_log") 
public class Sys_user_log {
	@Column 
	@Id
	private long id;
	@Column
	private long userid;
	@Column
	private int type;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String loginname;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String realname;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String note;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
	private String logintime;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
	private String loginip;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getRealname() {
		return realname;
	}
	public void setReaalname(String realname) {
		this.realname = realname;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getLogintime() {
		return logintime;
	}
	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	public String getLoginip() {
		return loginip;
	}
	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	
	
	
}
