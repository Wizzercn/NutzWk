package cn.wizzer.app.wx.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Table("wx_user")
@TableIndexes({@Index(name = "INDEX_WX_USER_OPENID", fields = {"openid"}, unique = true)})
public class Wx_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("单位id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sys_unit_id;

    @Column
    @Comment("openid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String openid;

    @Column
    @Comment("unionid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String unionid;

    @Column
    @Comment("微信昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("是否关注")
    private boolean subscribe;

    @Column
    @Comment("关注时间")
    @ColDefine(type = ColType.INT, width = 16)
    private Long subscribeAt;

    @Column
    @Comment("性别")
    @ColDefine(type = ColType.INT)
    private Integer sex;

    @Column
    @Comment("国家")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String country;

    @Column
    @Comment("省份")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String province;

    @Column
    @Comment("城市")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String city;

    @Column
    @Comment("头像")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String headimgurl;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Long getSubscribeAt() {
        return subscribeAt;
    }

    public void setSubscribeAt(Long subscribeAt) {
        this.subscribeAt = subscribeAt;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
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

    public String getSys_unit_id() {
        return sys_unit_id;
    }

    public void setSys_unit_id(String sys_unit_id) {
        this.sys_unit_id = sys_unit_id;
    }
}
