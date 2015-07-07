package cn.wizzer.modules.sys.bean;

import cn.wizzer.common.service.core.BasePojo;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/6/27.
 */
@Table("sys_role")
public class Sys_role extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Id
    private long id;
    @Column
    private String name;
    @Column
    protected String alias;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String unitid;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;
    @Column
    private long location;
    @ManyMany(from="role_id", relation="sys_role_menu", target=Sys_menu.class, to="menu_id")
    protected List<Sys_menu> menus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public List<Sys_menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Sys_menu> menus) {
        this.menus = menus;
    }
}
