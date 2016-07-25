package cn.wizzer.modules.back.sys.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_role")
@TableIndexes({@Index(name = "INDEX_SYS_ROLE_CODE", fields = {"code"}, unique = true)})
public class Sys_role extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String code;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String aliasName;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitid;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @One(target = Sys_unit.class, field = "unitid")
    public Sys_unit unit;

    @ManyMany(from = "roleId", relation = "sys_role_menu", target = Sys_menu.class, to = "menuId")
    protected List<Sys_menu> menus;

    @ManyMany(from = "roleId", relation = "sys_user_role", target = Sys_menu.class, to = "userId")
    private List<Sys_user> users;

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

    public List<Sys_user> getUsers() {
        return users;
    }

    public void setUsers(List<Sys_user> users) {
        this.users = users;
    }
}
