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
    @Column("create_user")
    @Comment("创建用户")
    protected long createUser;
    @Column("is_deteled")
    @Comment("是否删除")
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

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
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
