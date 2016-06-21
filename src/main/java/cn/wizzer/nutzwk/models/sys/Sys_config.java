package cn.wizzer.nutzwk.models.sys;

import cn.wizzer.nutzwk.models.BaseModel;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
public class Sys_config extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Name
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String configKey;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String configValue;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
