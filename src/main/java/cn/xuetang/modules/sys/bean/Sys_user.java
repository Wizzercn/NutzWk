package cn.xuetang.modules.sys.bean;

import java.util.Hashtable;
import java.util.List;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@Table("sys_user")
@TableIndexes({@Index(name = "INDEX_LONGINNAME", fields = {"loginname"}, unique = false)})
public class Sys_user {
	@Column 
	@Id
//	@Prev({
//		@SQL(db = DB.ORACLE, value="SELECT SYS_USER_S.nextval FROM dual")
//	})
	private long uid;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 120)
	private String loginname;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String realname;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String unitid;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
	private String password;// transient 修饰符可让此字段不在对象里显示
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String salt;
	@Column
    @ColDefine(type = ColType.INT, width = 1)
	private int state;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String descript;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String position;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String address;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String telephone;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 11)
    private String mobile;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String email;
	@Column
    @ColDefine(type = ColType.INT)
    private int location;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String style;
	@Column
    @ColDefine(type = ColType.INT)
    private int logintype;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String logintime;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
	private String loginip;
	@Column
    @ColDefine(type = ColType.INT)
    private int logincount;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String loginresid;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String linkqq;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String linkweb;
	@Column
    @ColDefine(type = ColType.VARCHAR, width =100)
    private String linkcity;

	private String unitname;

	// 是否超级管理员角色
	private boolean sysrole;

	private List<Integer> rolelist;
    private List<Integer> prolist;
	private List<String> reslist;

	private Hashtable<String, String> btnmap;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
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

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getLogintype() {
		return logintype;
	}

	public void setLogintype(int logintype) {
		this.logintype = logintype;
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

	public int getLogincount() {
		return logincount;
	}

	public void setLogincount(int logincount) {
		this.logincount = logincount;
	}

	public String getLoginresid() {
		return loginresid;
	}

	public void setLoginresid(String loginresid) {
		this.loginresid = loginresid;
	}

	public String getLinkqq() {
		return linkqq;
	}

	public void setLinkqq(String linkqq) {
		this.linkqq = linkqq;
	}

	public String getLinkweb() {
		return linkweb;
	}

	public void setLinkweb(String linkweb) {
		this.linkweb = linkweb;
	}

	public String getLinkcity() {
		return linkcity;
	}

	public void setLinkcity(String linkcity) {
		this.linkcity = linkcity;
	}
	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public List<Integer> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<Integer> rolelist) {
		this.rolelist = rolelist;
	}

	public List<String> getReslist() {
		return reslist;
	}

	public void setReslist(List<String> reslist) {
		this.reslist = reslist;
	}

	public boolean getSysrole() {
		return sysrole;
	}

	public void setSysrole(boolean sysrole) {
		this.sysrole = sysrole;
	}

	public Hashtable<String, String> getBtnmap() {
		return btnmap;
	}

	public void setBtnmap(Hashtable<String, String> btnmap) {
		this.btnmap = btnmap;
	}

    public List<Integer> getProlist() {
        return prolist;
    }

    public void setProlist(List<Integer> prolist) {
        this.prolist = prolist;
    }
}
