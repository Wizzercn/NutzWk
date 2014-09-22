package cn.xuetang.modules.sys.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer.cn
 * @time 2013-12-1 上午10:54:04
 * 
 */
@Table("sys_user_log") 
public class Sys_user_log {
	@Column 
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_USER_LOG_S.nextval FROM dual")
	})
	private long id;
	@Column
	private long userid;
	@Column
	private int type;
	@Column
	private String loginname;
	@Column
	private String realname;
	@Column
	private String note;
	@Column
	private String logintime;
	@Column
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
