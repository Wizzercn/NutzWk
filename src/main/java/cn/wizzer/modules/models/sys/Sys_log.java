package cn.wizzer.modules.models.sys;

import cn.wizzer.common.util.StringUtil;
import cn.wizzer.common.base.BaseModel;
import org.apache.shiro.SecurityUtils;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_log")
public class Sys_log extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Id
    private long id;
    @Column
    @Comment("创建人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String createBy;
    @Column
    @Comment("创建昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String nickname;
    @Column// aop.before aop.after aop.error
    @Comment("日志类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;
    @Column
    @Comment("日志标识")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String tag;
    @Column
    @Comment("执行类")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String src;
    @Column
    @Comment("来源IP")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String ip;
    @Column
    @Comment("日志内容")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Sys_log c(String type, String tag, String msg) {
        Sys_log sysLog = new Sys_log();
        if (type == null || tag == null || msg == null) {
            throw new RuntimeException("type/tag/msg can't null");
        }
        String source = "";
        StackTraceElement[] tmp = Thread.currentThread().getStackTrace();
        if (tmp.length > 2) {
            source = tmp[2].getClassName() + "#" + tmp[2].getMethodName();
        }
        sysLog.type = type;
        sysLog.tag = tag;
        sysLog.src = source;
        sysLog.msg = msg;
        sysLog.ip = StringUtil.getRemoteAddr();
        Object u = SecurityUtils.getSubject().getPrincipal();
        String uid="";
        String nickname="";
        if (u != null) {
            if (u instanceof Sys_user) {
                nickname = ((Sys_user) u).getNickname();
                uid = ((Sys_user) u).getId();
            } else if (u instanceof String) {
                nickname = ((String) u);
            }
        }
        sysLog.nickname=nickname;
        sysLog.createBy=uid;
        return sysLog;
    }
}
