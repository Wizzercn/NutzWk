package cn.wizzer.modules.models.sys;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/8/11.
 */
@Table("sys_api")
public class Sys_api extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("appName")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String appName;

    @Column
    @Comment("appId")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String appId;

    @Column
    @Comment("appSecret")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String appSecret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
