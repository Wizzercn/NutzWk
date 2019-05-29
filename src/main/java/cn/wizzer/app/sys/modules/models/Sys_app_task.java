package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 应用管理--推送任务表
 * Created by wizzer on 2019/2/27.
 */
@Table("sys_app_task")
public class Sys_app_task extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("实例名")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String name;

    @Column
    @Comment("执行动作")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String action;

    @Column
    @Comment("APP版本")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String appVersion;

    @Column
    @Comment("配置版本")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String confVersion;

    @Column
    @Comment("进程ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String processId;

    @Column
    @Comment("推送主机")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String hostName;//主机名 如果是推送给全部主机则为多条

    @Column
    @Comment("主机IP")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String hostAddress;//只为展示用

    @Column
    @Comment("推送状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer status;//0-待执行,1-执行中,2-执行成功,3-执行失败,4-撤销任务

    @Column
    @Comment("反馈时间")
    private Long pushAt;

    @Column
    @Comment("反馈结果")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pushResult;

    @One(field = "opBy")
    private Sys_user user;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getConfVersion() {
        return confVersion;
    }

    public void setConfVersion(String confVersion) {
        this.confVersion = confVersion;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPushAt() {
        return pushAt;
    }

    public void setPushAt(Long pushAt) {
        this.pushAt = pushAt;
    }

    public String getPushResult() {
        return pushResult;
    }

    public void setPushResult(String pushResult) {
        this.pushResult = pushResult;
    }

    public Sys_user getUser() {
        return user;
    }

    public void setUser(Sys_user user) {
        this.user = user;
    }
}
