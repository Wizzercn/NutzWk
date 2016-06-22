package cn.wizzer.nutzwk.models.sys;

import cn.wizzer.common.base.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_role")
public class Sys_role extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els={@EL("uuid()")})
    private String id;
    @Column
    private String name;
    @Column
    private String code;
    @Column
    protected String aliasName;
    @Column
    private boolean disabled;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitid;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;
    @One(target = Sys_unit.class, field = "unitid")
    public Sys_unit unit;
    @ManyMany(from="roleId", relation="sys_role_menu", target=Sys_menu.class, to="menuId")
    protected List<Sys_menu> menus;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Sys_menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Sys_menu> menus) {
        this.menus = menus;
    }
}
