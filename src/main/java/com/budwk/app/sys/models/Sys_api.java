package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * API密钥管理
 * Created by wizzer on 2019/2/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_api")
public class Sys_api extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String appid;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String appkey;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String name;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

}
