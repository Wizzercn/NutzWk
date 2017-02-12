package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import cn.wizzer.framework.util.StringUtil;
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
//    @Prev({
//            //仅做演示,实际使用oracle时,请使用触发器+序列的方式实现自增长ID,否则高并发下此种写法性能是个瓶颈
//            //实际上不推荐在主键上使用自定义sql来生成
//            @SQL(db = DB.ORACLE, value = "SELECT SYS_LOG_S.nextval FROM dual")
//    })
    private long id;

    @Column
    @Comment("创建昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String username;

    @Column// aop.before aop.after aop.error
    @Comment("日志类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String type;

    @Column("xxx")
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
    @ColDefine(type = ColType.TEXT)
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public static Sys_log c(String type, String tag, String msg, String source) {
        Sys_log sysLog = new Sys_log();
        if (type == null || tag == null || msg == null) {
            throw new RuntimeException("type/tag/msg can't null");
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
        sysLog.src = source;
        sysLog.msg = msg;
        sysLog.ip = StringUtil.getRemoteAddr();
        Object u = SecurityUtils.getSubject().getPrincipal();
        String uid = "";
        String username = "";
        if (u != null) {
            if (u instanceof Sys_user) {
                username = ((Sys_user) u).getUsername();
                uid = ((Sys_user) u).getId();
            } else if (u instanceof String) {
                username = ((String) u);
            }
        }
        sysLog.setOpBy(uid);
        sysLog.setOpAt((int) (System.currentTimeMillis() / 1000));
        sysLog.username = username;
        return sysLog;
    }
}
