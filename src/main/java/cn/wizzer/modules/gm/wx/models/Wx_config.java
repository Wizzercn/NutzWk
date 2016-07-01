package cn.wizzer.modules.gm.wx.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/1.
 */
@Table("wx_config")
public class Wx_config extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("公众号名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String appname;

    @Column
    @Comment("原始ID")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String ghid;

    @Column
    @Comment("Appid")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String appid;

    @Column
    @Comment("Appsecret")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String appsecret;

    @Column
    @Comment("EncodingAESKey")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String encodingAESKey;

    @Column
    @Comment("Token")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String token;

    @Column
    @Comment("Access_token")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String access_token;

    @Column
    @Comment("Expire_time")
    protected Integer expire_time;

    @Column
    @Comment("jsapi_ticket")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String jsapi_ticket;

    @Column
    @Comment("jsapi_time")
    protected Integer jsapi_time;

    @Column
    @Comment("创建人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getGhid() {
        return ghid;
    }

    public void setGhid(String ghid) {
        this.ghid = ghid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Integer expire_time) {
        this.expire_time = expire_time;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public Integer getJsapi_time() {
        return jsapi_time;
    }

    public void setJsapi_time(Integer jsapi_time) {
        this.jsapi_time = jsapi_time;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
