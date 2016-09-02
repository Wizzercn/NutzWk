package cn.wizzer.modules.back.wx.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_menu")
public class Wx_menu extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("父ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String parentId;

    @Column
    @Comment("树路径")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String path;

    @Column
    @Comment("菜单名称")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String menuName;

    @Column
    @Comment("菜单类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String menuType;

    @Column
    @Comment("关键词")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String menuKey;

    @Column
    @Comment("网址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

    @Column
    @Comment("排序字段")
    @Prev(@SQL("SELECT IFNULL(MAX(location),0)+1 FROM wx_menu"))
    private Integer location;

    @Column
    @Comment("有子节点")
    private boolean hasChildren;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(target = Wx_config.class, field = "wxid")
    private Wx_config wxConfig;

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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public Wx_config getWxConfig() {
        return wxConfig;
    }

    public void setWxConfig(Wx_config wxConfig) {
        this.wxConfig = wxConfig;
    }
}
