package cn.wizzer.modules.sys.bean;

import cn.wizzer.common.service.core.BasePojo;
import cn.wizzer.common.util.StringUtils;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wizzer.cn on 2015/7/8.
 */
@Table("sys_log_${ym}")
public class Sys_log extends BasePojo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Id
    private long id;
    @Column("type")// aop.before aop.after aop.error
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String type;
    @Column("tag")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String tag;
    @Column("src")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String source;
    @Column("ip")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String ip;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 1024)
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static Sys_log c(String type, String tag, String uid, String msg, String source) {
        Sys_log sysLog = new Sys_log();
        sysLog.setCreateUser(uid);
        sysLog.setCreateTime(new Date());
        if (type == null || tag == null || msg == null) {
            throw new RuntimeException("t/tag/msg can't null");
        }
        if (source == null) {
            StackTraceElement[] tmp = Thread.currentThread().getStackTrace();
            if (tmp.length > 2) {
                source = tmp[2].getClassName() + "#" + tmp[2].getMethodName();
            } else {
                source = "main";
            }
        }
        sysLog.type = type;
        sysLog.tag = tag;
        sysLog.source = source;
        sysLog.msg = msg;
        sysLog.ip = StringUtils.getRemoteAddr();
        return sysLog;
    }

}
