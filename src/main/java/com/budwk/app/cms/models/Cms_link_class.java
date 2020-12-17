package com.budwk.app.cms.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Data
@Table("cms_link_class")
@TableIndexes({@Index(name = "INDEX_CMS_LINK_CLASS", fields = {"code"}, unique = true)})
public class Cms_link_class extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("分类名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String name;

    @Column
    @Comment("分类编码")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String code;

    @ManyMany(from = "classId", relation = "cms_class_link", to = "linkId")
    private List<Cms_link> links;

}
