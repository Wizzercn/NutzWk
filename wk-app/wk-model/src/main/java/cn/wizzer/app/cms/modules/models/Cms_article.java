package cn.wizzer.app.cms.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/7/18.
 */
@Table("cms_article")
public class Cms_article extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    //@Prev(els = {@EL("ig(view.tableName,'')")}) 主键生成器示例
    private String id;

    @Column
    @Comment("站点ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String siteid;

    @Column
    @Comment("预留商城ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String shopid;

    @Column
    @Comment("文章标题")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String title;

    @Column
    @Comment("链接地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String url;

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
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("发布时间")
    @ColDefine(type = ColType.INT, width = 16)
    private Long publishAt;

    @Column
    @Comment("截至时间")
    @ColDefine(type = ColType.INT, width = 16)
    private Long endAt;

    @Column
    @Comment("同步状态")
    @ColDefine(type = ColType.INT)
    @Default(value = "0")
    private int status;

    @Column
    @Comment("浏览量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer view_num;

    @Column
    @Comment("排序字段")
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(location),0)+1 FROM cms_article"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(location),0)+1 FROM cms_article")
    })
    private Integer location;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String channelId;

    @One(field = "channelId")
    private Cms_channel channel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Long getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Long publishAt) {
        this.publishAt = publishAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public Integer getView_num() {
        return view_num;
    }

    public void setView_num(Integer view_num) {
        this.view_num = view_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
