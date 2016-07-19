package cn.wizzer.modules.back.cms.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Table("cms_site")
public class Cms_site extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("预留商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String shopid;

    @Column
    @Comment("网站名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_name;

    @Column
    @Comment("网站域名")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_domain;

    @Column
    @Comment("网站备案")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String site_icp;

    @Column
    @Comment("网站LOGO")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_logo;

    @Column
    @Comment("WAP版LOGO")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_wap_logo;

    @Column
    @Comment("客服QQ")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String site_qq;

    @Column
    @Comment("网站邮箱")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String site_email;

    @Column
    @Comment("客服电话")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String site_tel;

    @Column
    @Comment("微博名称")
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
    @Comment("网页关键词")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String seo_keywords;


    @Column
    @Comment("网页描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String seo_description;

    @Column
    @Comment("底部版权")
    @ColDefine(type = ColType.TEXT)
    private String footer_content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getSite_domain() {
        return site_domain;
    }

    public void setSite_domain(String site_domain) {
        this.site_domain = site_domain;
    }

    public String getSite_icp() {
        return site_icp;
    }

    public void setSite_icp(String site_icp) {
        this.site_icp = site_icp;
    }

    public String getSite_logo() {
        return site_logo;
    }

    public void setSite_logo(String site_logo) {
        this.site_logo = site_logo;
    }

    public String getSite_wap_logo() {
        return site_wap_logo;
    }

    public void setSite_wap_logo(String site_wap_logo) {
        this.site_wap_logo = site_wap_logo;
    }

    public String getSite_qq() {
        return site_qq;
    }

    public void setSite_qq(String site_qq) {
        this.site_qq = site_qq;
    }

    public String getSite_email() {
        return site_email;
    }

    public void setSite_email(String site_email) {
        this.site_email = site_email;
    }

    public String getSite_tel() {
        return site_tel;
    }

    public void setSite_tel(String site_tel) {
        this.site_tel = site_tel;
    }

    public String getWeibo_name() {
        return weibo_name;
    }

    public void setWeibo_name(String weibo_name) {
        this.weibo_name = weibo_name;
    }

    public String getWeibo_url() {
        return weibo_url;
    }

    public void setWeibo_url(String weibo_url) {
        this.weibo_url = weibo_url;
    }

    public String getWeibo_qrcode() {
        return weibo_qrcode;
    }

    public void setWeibo_qrcode(String weibo_qrcode) {
        this.weibo_qrcode = weibo_qrcode;
    }

    public String getWechat_name() {
        return wechat_name;
    }

    public void setWechat_name(String wechat_name) {
        this.wechat_name = wechat_name;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getWechat_qrcode() {
        return wechat_qrcode;
    }

    public void setWechat_qrcode(String wechat_qrcode) {
        this.wechat_qrcode = wechat_qrcode;
    }

    public String getSeo_keywords() {
        return seo_keywords;
    }

    public void setSeo_keywords(String seo_keywords) {
        this.seo_keywords = seo_keywords;
    }

    public String getSeo_description() {
        return seo_description;
    }

    public void setSeo_description(String seo_description) {
        this.seo_description = seo_description;
    }

    public String getFooter_content() {
        return footer_content;
    }

    public void setFooter_content(String footer_content) {
        this.footer_content = footer_content;
    }
}
