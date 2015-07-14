package cn.wizzer.modules.sys.bean;

import cn.wizzer.common.service.core.BasePojo;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer.cn on 2015/6/29.
 */
@Table("sys_user_profile")
public class Sys_user_profile extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**关联的用户id*/
    @Name
    @Column("user_id")
    @ColDefine(type = ColType.VARCHAR, width = 64)
    protected String userId;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String position;
    /**用户昵称*/
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    protected String nickname;
    /**用户邮箱*/
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    protected String email;
    /**邮箱是否已经验证过*/
    @Column("email_checked")
    protected boolean emailChecked;
    /**头像的byte数据*/
    @Column
    protected byte[] avatar;
    /**性别*/
    @Column
    protected String gender;
    /**自我介绍*/
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    protected String description;
    @Column("link_address")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    protected String address;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 11)
    private String mobile;
    @Column("link_qq")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String linkQq;
    @Column("link_website")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String linkWebsite;
    @Column("link_province")
    @ColDefine(type = ColType.VARCHAR, width =100)
    private String linkProvince;
    @Column("link_city")
    @ColDefine(type = ColType.VARCHAR, width =100)
    private String linkCity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(boolean emailChecked) {
        this.emailChecked = emailChecked;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLinkQq() {
        return linkQq;
    }

    public void setLinkQq(String linkQq) {
        this.linkQq = linkQq;
    }

    public String getLinkWebsite() {
        return linkWebsite;
    }

    public void setLinkWebsite(String linkWebsite) {
        this.linkWebsite = linkWebsite;
    }

    public String getLinkProvince() {
        return linkProvince;
    }

    public void setLinkProvince(String linkProvince) {
        this.linkProvince = linkProvince;
    }

    public String getLinkCity() {
        return linkCity;
    }

    public void setLinkCity(String linkCity) {
        this.linkCity = linkCity;
    }
}
