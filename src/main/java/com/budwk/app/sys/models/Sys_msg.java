package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wizzer on 2018/6/29.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_msg")
public class Sys_msg extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("消息类型")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String type;

    @Column
    @Comment("消息标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String title;

    @Column
    @Comment("消息内容")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Column
    @Comment("消息URL")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

    @Column
    @Comment("发送时间")
    //Long不要用ColDefine定义,兼容oracle/mysql,支持2038年以后的时间戳
    private Long sendAt;

    @Many(field = "msgId")
    private List<Sys_msg_user> userList;


}
