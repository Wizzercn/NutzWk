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
    @Comment("推送类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;//app 或 conf

    @Column
    @Comment("推送内容")
    @ColDefine(type = ColType.TEXT)
    private String data;//json 含动作文件版本等

    @Column
    @Comment("推送主机")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String hostname;//主机名 如果是推送给全部主机则为多条

    @Column
    @Comment("主机IP")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String hostAddress;//只为展示用

    @Column
    @Comment("推送状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer status;//0 待接收 1 已接收 2 已反馈

    @Column
    @Comment("反馈时间")
    private Long pushAt;

    @Column
    @Comment("反馈结果")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String pushResult;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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
}
