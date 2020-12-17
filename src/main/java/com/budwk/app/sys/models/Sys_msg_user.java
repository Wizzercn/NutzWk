package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2018/6/29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_msg_user")
public class Sys_msg_user extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("消息ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String msgId;

    @Column
    @Comment("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String loginname;

    @Column
    @Comment("消息状态")
    @ColDefine(type = ColType.INT)
    private int status;//0--未读  1--已读

    @Column
    @Comment("读取时间")
    //Long不要用ColDefine定义,兼容oracle/mysql,支持2038年以后的时间戳
    private Long readAt;

    @One(field = "msgId")
    private Sys_msg msg;

}
