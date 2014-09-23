package cn.xuetang.modules.sys.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午1:34:26
 *
 */
@Table("sys_role_resource")
@TableIndexes({@Index(name = "INDEX_ROLERESOURCE", fields = {"roleid","resourceid"}, unique = true)})
public class Sys_role_resource {
	@Column
	private int roleid;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String resourceid;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 1000)
    private String button;
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	
}


