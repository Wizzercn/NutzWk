package cn.wizzer.common.base;

import org.nutz.dao.entity.annotation.*;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.random.R;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by wizzer on 2016/6/21.
 */
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Comment("创建时间")
    @Prev(els = @EL("$me.now()"))
    protected int createAt;
    @Column
    @Comment("更新时间")
    @Prev(els = @EL("$me.now()"))
    protected int updateAt;

    public String toString() {
        return String.format("/*%s*/%s", super.toString(), Json.toJson(this, JsonFormat.compact()));
    }

    public int getCreateAt() {
        return createAt;
    }

    public void setCreateAt(int createAt) {
        this.createAt = createAt;
    }

    public int getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(int updateAt) {
        this.updateAt = updateAt;
    }

    public int now() {
        return  (int)(System.currentTimeMillis()/1000);
    }

    public String uuid() {
        return R.UU32().toLowerCase();
    }
}
