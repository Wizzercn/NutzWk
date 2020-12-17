package com.budwk.app.wx.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("wx_mass_news")
public class Wx_mass_news extends BaseModel implements Serializable {
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
    @Comment("缩略图ID")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String thumb_media_id;

    @Column
    @Comment("缩略图URL")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String picurl;

    @Column
    @Comment("作者")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String author;

    @Column
    @Comment("标题")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String title;

    @Column
    @Comment("原地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String content_source_url;

    @Column
    @Comment("图文内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("摘要")
    @ColDefine(type = ColType.TEXT)
    private String digest;

    @Column
    @Comment("显示封面")
    @ColDefine(type = ColType.INT)
    private Integer show_cover_pic;//1为显示，0为不显示

    @Column
    @Comment("是否打开评论")
    @ColDefine(type = ColType.INT)
    private Integer need_open_comment;//0不打开，1打开

    @Column
    @Comment("是否粉丝才可评论")
    @ColDefine(type = ColType.INT)
    private Integer only_fans_can_comment;//0所有人可评论，1粉丝才可评论

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM wx_mass_news"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM wx_mass_news")
    })
    private Integer location;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String wxid;

    @One(field = "wxid")
    private Wx_config wxConfig;

}
