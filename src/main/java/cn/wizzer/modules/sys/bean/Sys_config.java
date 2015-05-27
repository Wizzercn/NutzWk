package cn.wizzer.modules.sys.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * @author Wizzer
 * @time 2013-12-30 11:20:35
 */
@Table("SYS_CONFIG")
public class Sys_config {
    @Column
    @Id
    private int id;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String cname;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String cvalue;
    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;
    @Column
    private int location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

}