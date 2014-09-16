package cn.xuetang.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2013-12-30 11:20:35
*/
@Table("SYS_CONFIG")
public class Sys_config 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_CONFIG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String cname;
	@Column
	private String cvalue;
	@Column
	private String note;
	@Column
	private int location;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getCname()
	{
		return cname;
	}
	public void setCname(String cname)
	{
		this.cname=cname;
	}
	public String getCvalue()
	{
		return cvalue;
	}
	public void setCvalue(String cvalue)
	{
		this.cvalue=cvalue;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note=note;
	}
	public int getLocation()
	{
		return location;
	}
	public void setLocation(int location)
	{
		this.location=location;
	}

}