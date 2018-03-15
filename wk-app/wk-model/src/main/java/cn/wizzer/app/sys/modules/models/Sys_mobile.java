package cn.wizzer.app.sys.modules.models;

import cn.wizzer.framework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2017/4/25.
 */
@Table("sys_mobile")
@TableIndexes({@Index(name = "INDEX_SYS_MOBILE", fields = {"mobile"}, unique = true)})
public class Sys_mobile extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Id(auto = true)
    @ColDefine(type = ColType.INT)
    private int id;

    @Column
    @Comment("号码段")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String mobile;

    @Column
    @Comment("省份")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String province;

    @Column
    @Comment("城市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String city;

    @Column
    @Comment("运营商")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String operator;

    @Column
    @Comment("区号")
    @ColDefine(type = ColType.VARCHAR, width = 4)
    private String areacode;

    @Column
    @Comment("邮编")
    @ColDefine(type = ColType.VARCHAR, width = 6)
    private String zipcode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
