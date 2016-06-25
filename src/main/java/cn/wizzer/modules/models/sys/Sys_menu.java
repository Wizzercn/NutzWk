package cn.wizzer.modules.models.sys;

import cn.wizzer.common.base.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_menu")
@TableIndexes({@Index(name = "INDEX_SYS_MENU_PATH", fields = {"path"}, unique = true),@Index(name = "INDEX_SYS_MENU_PREM", fields = {"permission"}, unique = true)})
public class Sys_menu extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;
    @Column
    @Comment("父级ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String parentId;
    @Column
    @Comment("树路径")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String path;
    @Column
    @Comment("菜单名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;
    @Column
    @Comment("菜单别名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String aliasName;
    @Column
    @Comment("资源类型")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String type;
    @Column
    @Comment("菜单链接")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String href;
    @Column
    @Comment("打开方式")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String target;
    @Column
    @Comment("菜单图标")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String icon;
    @Column
    @Comment("是否显示")
    private boolean isShow;
    @Column
    @Comment("是否禁用")
    private boolean disabled;
    @Column
    @Comment("权限标识")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String permission;
    @Column
    @Comment("菜单介绍")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;
    @Column
    @Comment("排序字段")
    @Prev( @SQL("select max(location)+1 from sys_menu") )
    private int location;
    @Column
    @Comment("有子节点")
    private boolean hasChildren;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
