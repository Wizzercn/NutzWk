package com.budwk.app.base.model;

import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.nutz.dao.interceptor.annotation.PrevInsert;
import org.nutz.dao.interceptor.annotation.PrevUpdate;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Data
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Comment("创建人")
    @PrevInsert(els = @EL("$me.createdByUid()"))
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String createdBy;

    /**
     * Long不要用ColDefine定义,兼容oracle/mysql,支持2038年以后的时间戳
     * 13位时间戳哦,不再是11位
     */
    @Column
    @Comment("创建时间")
    @PrevInsert(now = true)
    private Long createdAt;

    @Column
    @Comment("修改人")
    @PrevInsert(els = @EL("$me.updatedByUid()"))
    @PrevUpdate(els = @EL("$me.updatedByUid()"))
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String updatedBy;

    /**
     * Long不要用ColDefine定义,兼容oracle/mysql,支持2038年以后的时间戳
     * 13位时间戳哦,不再是11位
     */
    @Column
    @Comment("修改时间")
    @PrevInsert(now = true)
    @PrevUpdate(now = true)
    private Long updatedAt;

    @Column
    @Comment("删除标记")
    @PrevInsert(els = @EL("$me.flag()"))
    @ColDefine(type = ColType.BOOLEAN)
    private Boolean delFlag;

    public String toJsonString() {
        return Json.toJson(this, JsonFormat.compact());
    }

    public Boolean flag() {
        return false;
    }

    public String createdByUid() {
        String uid = getCreatedBy();
        if (Strings.isNotBlank(uid)) {
            return uid;
        }
        try {
            HttpServletRequest request = Mvcs.getReq();
            if (request != null) {
                return Strings.sNull(request.getSession(true).getAttribute("platform_uid"));
            }
        } catch (Exception e) {
        }
        return "";
    }

    public String updatedByUid() {
        String uid = getUpdatedBy();
        if (Strings.isNotBlank(uid)) {
            return uid;
        }
        try {
            HttpServletRequest request = Mvcs.getReq();
            if (request != null) {
                return Strings.sNull(request.getSession(true).getAttribute("platform_uid"));
            }
        } catch (Exception e) {
        }
        return "";
    }
}
