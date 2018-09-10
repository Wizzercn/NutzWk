package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/30.
 */
@Table("sys_task")
public class Sys_task extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("任务名")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;

    @Column
    @Comment("执行类")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String jobClass;

    @Column
    @Comment("任务说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("定时规则")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String cron;

    @Column
    @Comment("执行参数")
    @ColDefine(type = ColType.TEXT)
    private String data;

    @Column
    @Comment("执行时间")
    @ColDefine(type = ColType.INT)
    private Long exeAt;

    @Column
    @Comment("执行结果")
    @ColDefine(type = ColType.TEXT)
    private String exeResult;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

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

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getExeAt() {
        return exeAt;
    }

    public void setExeAt(Long exeAt) {
        this.exeAt = exeAt;
    }

    public String getExeResult() {
        return exeResult;
    }

    public void setExeResult(String exeResult) {
        this.exeResult = exeResult;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
