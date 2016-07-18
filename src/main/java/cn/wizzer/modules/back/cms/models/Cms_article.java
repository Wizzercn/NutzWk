package cn.wizzer.modules.back.cms.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Table("cms_article")
public class Cms_article extends Model implements Serializable {
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
    @Comment("文章标题")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String title;

    @Column
    @Comment("文章简介")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String info;

    @Column
    @Comment("文章作者")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String author;

    @Column
    @Comment("标题图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String picurl;

    @Column
    @Comment("文章内容")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("是否禁用")
    private boolean disabled;

    @Column
    @Comment("排序字段")
    @Prev(@SQL("SELECT IFNULL(MAX(location),0)+1 FROM cms_article"))
    private Integer location;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String channelId;

    @One(target = Cms_channel.class, field = "channelId")
    private Cms_channel channel;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Cms_channel getChannel() {
        return channel;
    }

    public void setChannel(Cms_channel channel) {
        this.channel = channel;
    }
}
