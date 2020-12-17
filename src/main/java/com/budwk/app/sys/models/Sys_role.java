package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wizzer on 2016/6/21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_role")
@TableIndexes({@Index(name = "INDEX_SYS_ROLE_CODE", fields = {"code"}, unique = true)})
public class Sys_role extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
    private String id;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String code;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String aliasName;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitid;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @One(field = "unitid")
    public Sys_unit unit;

    @ManyMany(from = "roleId", relation = "sys_role_menu", to = "menuId")
    protected List<Sys_menu> menus;

    @ManyMany(from = "roleId", relation = "sys_user_role", to = "userId")
    private List<Sys_user> users;

}
