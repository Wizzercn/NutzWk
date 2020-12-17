package com.budwk.app.cms.models;

import com.budwk.app.base.model.BaseModel;
import lombok.Data;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Data
@Table("cms_site")
public class Cms_site extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;

    @Column
    @Comment("名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_name;

    @Column
    @Comment("域名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_domain;

    @Column
    @Comment("ICP")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_icp;

    @Column
    @Comment("LOGO")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_logo;

    @Column
    @Comment("WAPLOGO")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_wap_logo;

    @Column
    @Comment("客服QQ")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String site_qq;

    @Column
    @Comment("邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_email;

    @Column
    @Comment("电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String site_tel;

    @Column
    @Comment("微博")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String weibo_name;

    @Column
    @Comment("微博地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String weibo_url;

    @Column
    @Comment("微博二维码")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String weibo_qrcode;

    @Column
    @Comment("微信名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String wechat_name;

    @Column
    @Comment("微信ID")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String wechat_id;

    @Column
    @Comment("微信二维码")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String wechat_qrcode;

    @Column
    @Comment("关键词")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String seo_keywords;


    @Column
    @Comment("描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String seo_description;

    @Column
    @Comment("底部版权")
    @ColDefine(type = ColType.TEXT)
    private String footer_content;

}
