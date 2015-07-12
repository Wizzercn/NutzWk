package cn.wizzer.modules.sys.bean;

import cn.wizzer.common.service.core.BasePojo;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer.cn on 2015/6/29.
 */
@Table("sys_unit")
public class Sys_unit extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String id;
    @Column
    @Comment("单位名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;
    @Column
    @Comment("机构编码")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String unitcode;
    @Column
    @Comment("单位介绍")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;
    @Column
    @Comment("单位地址")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String address;
    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String telephone;
    @Column
    @Comment("单位邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String email;
    @Column
    @Comment("单位网站")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String website;
    @Column
    @Comment("排序字段")
    private int location;
    @Column("has_children")
    @Comment("有子节点")
    private boolean hasChildren;

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

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
