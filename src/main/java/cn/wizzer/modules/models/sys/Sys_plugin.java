package cn.wizzer.modules.models.sys;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by Wizzer on 2016/12/6.
 */
@Table("sys_plugin")
@TableIndexes({@Index(name = "INDEX_SYS_PLUGIN", fields = {"code"}, unique = true),@Index(name = "INDEX_SYS_CLASSNAME", fields = {"className"}, unique = true)})
public class Sys_plugin extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("唯一标识")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String code;

    @Column
    @Comment("类名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String className;

    @Column
    @Comment("执行参数")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String args;

    @Column
    @Comment("文件路径")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String path;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
