package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_task")
public class Sys_task extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
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
    private Long exeAt;

    @Column
    @Comment("执行结果")
    @ColDefine(type = ColType.TEXT)
    private String exeResult;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

}
