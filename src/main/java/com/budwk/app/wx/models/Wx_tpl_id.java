package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/8/5.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_tpl_id")
@TableIndexes({@Index(name = "INDEX_WX_TPL_ID", fields = {"id", "wxid"}, unique = true)})
public class Wx_tpl_id extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Comment("模板编号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;

    @Column
    @Comment("单位id")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String sys_unit_id;

    @Column
    @Comment("模板ID")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String template_id;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

}
