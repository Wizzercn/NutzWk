package com.budwk.app.sys.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_unit")
@TableIndexes({@Index(name = "INDEX_SYS_UNIT_PATH", fields = {"path"}, unique = true),
        @Index(name = "INDEX_SYS_UNIT_UNITCODE", fields = {"unitcode"}, unique = true)
})
public class Sys_unit extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("父级ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String parentId;

    @Column
    @Comment("树路径")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String path;

    @Column
    @Comment("单位名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("单位别名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String aliasName;

    @Column
    @Comment("机构编码")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String unitcode;

    @Column
    @Comment("单位介绍")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("单位地址")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String address;

    @Column
    @Comment("联系电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String telephone;

    @Column
    @Comment("单位邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String email;

    @Column
    @Comment("单位网站")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String website;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db= DB.MYSQL,value = "SELECT IFNULL(MAX(location),0)+1 FROM sys_unit"),
            @SQL(db= DB.ORACLE,value = "SELECT COALESCE(MAX(location),0)+1 FROM sys_unit")
    })
    private Integer location;

    @Column
    @Comment("有子节点")
    private boolean hasChildren;

    @Column
    @Comment("单位logo")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String logo;

}
