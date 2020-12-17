package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/8/5.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_tpl_list")
@TableIndexes({@Index(name = "INDEX_WX_TPL_LIST", fields = {"template_id","wxid"}, unique = true)})
public class Wx_tpl_list extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @PrevInsert(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("单位id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sys_unit_id;

    @Column
    @Comment("模板ID")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String template_id;

    @Column
    @Comment("模板标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String title;

    @Column
    @Comment("主营行业")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String primary_industry;

    @Column
    @Comment("副营行业")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String deputy_industry;

    @Column
    @Comment("模板内容")
    @ColDefine(type = ColType.VARCHAR, width = 300)
    private String content;

    @Column
    @Comment("模板示例")
    @ColDefine(type = ColType.VARCHAR, width = 300)
    private String example;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    public String getId() {
        return id;
    }

}
