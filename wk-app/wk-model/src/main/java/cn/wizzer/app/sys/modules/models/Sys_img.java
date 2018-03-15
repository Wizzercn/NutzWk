package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 图片配置表
 * Created by wizzer on 2016/9/27.
 */
@Table("sys_img")
public class Sys_img extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;

    @Column
    @Comment("缩略图宽")
    @ColDefine(type = ColType.INT)
    private int thumb_w;

    @Column
    @Comment("缩略图高")
    @ColDefine(type = ColType.INT)
    private int thumb_h;

    @Column
    @Comment("启用水印")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean wm_has;

    @Column
    @Comment("水印图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String wm_img;

    @Column
    @Comment("缩略图水印图")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String wm_img_s;

    @Column
    @Comment("水印图位置")
    @ColDefine(type = ColType.INT)
    private int wm_position;//1-9 共9个位置，请使用 Images.WATERMARK_{XXX} 进行设置

    @Column
    @Comment("水印图透明度")
    @ColDefine(type = ColType.FLOAT)
    private float wm_opacity;//透明度, 要求大于0小于1, 默认为0.5f

    @Column
    @Comment("水印图位置")
    @ColDefine(type = ColType.INT)
    private int wm_margin;//水印距离四周的边距 默认为0

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThumb_w() {
        return thumb_w;
    }

    public void setThumb_w(int thumb_w) {
        this.thumb_w = thumb_w;
    }

    public int getThumb_h() {
        return thumb_h;
    }

    public void setThumb_h(int thumb_h) {
        this.thumb_h = thumb_h;
    }

    public boolean isWm_has() {
        return wm_has;
    }

    public void setWm_has(boolean wm_has) {
        this.wm_has = wm_has;
    }

    public String getWm_img() {
        return wm_img;
    }

    public void setWm_img(String wm_img) {
        this.wm_img = wm_img;
    }

    public String getWm_img_s() {
        return wm_img_s;
    }

    public void setWm_img_s(String wm_img_s) {
        this.wm_img_s = wm_img_s;
    }

    public int getWm_position() {
        return wm_position;
    }

    public void setWm_position(int wm_position) {
        this.wm_position = wm_position;
    }

    public float getWm_opacity() {
        return wm_opacity;
    }

    public void setWm_opacity(float wm_opacity) {
        this.wm_opacity = wm_opacity;
    }

    public int getWm_margin() {
        return wm_margin;
    }

    public void setWm_margin(int wm_margin) {
        this.wm_margin = wm_margin;
    }
}
