package cn.wizzer.modules.sys.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午1:17:36
 *
 */
@Table("sys_resource")
public class Sys_resource {
	@Column
	@Name
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String id;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
	private String url;
	@Column
    @ColDefine(type = ColType.INT, width = 1)
	private int state;
	@Column
	private int subtype;
	@Column
	private int location;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
	private String descript;
	@Column
    @ColDefine(type = ColType.VARCHAR, width = 1000)
	private String button;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String style;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSubtype() {
		return subtype;
	}
	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}


