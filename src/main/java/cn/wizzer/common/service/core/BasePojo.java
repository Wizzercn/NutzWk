package cn.wizzer.common.service.core;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 共享字段,免得每个Pojo类都加创建时间和生成时间
 * Created by Wizzer.cn on 2015/6/29.
 */
public abstract class BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Prev(els=@EL("$me.now()"))
    @Column("create_time")
    @Comment("创建时间")
    protected Date createTime;
    @Prev(els=@EL("$me.now()"))
    @Column("update_time")
    @Comment("更新时间")
    protected Date updateTime;
    @Column("is_deteled")
    @Comment("更新时间")
    protected boolean isDeteled;
    public String toString() {
        return String.format("/*%s*/%s", super.toString(), Json.toJson(this, JsonFormat.compact()));
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date now() {
        return new Date();
    }

    public boolean isDeteled() {
        return isDeteled;
    }

    public void setIsDeteled(boolean isDeteled) {
        this.isDeteled = isDeteled;
    }
}
