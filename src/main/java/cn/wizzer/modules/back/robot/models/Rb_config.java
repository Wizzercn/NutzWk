package cn.wizzer.modules.back.robot.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("rb_config")
public class Rb_config extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String id;//robot

    @Column
    @Comment("确认关键词")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String confirmKey;

    @Column
    @Comment("成功提示")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String confirmTxt;//订餐成功。

    @Column
    @Comment("不允许")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String confirmNoway;//不在订餐时间。

    @Column
    @Comment("没到时间")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String confirmNo;//没到订餐时间。

    @Column
    @Comment("订过提示")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String confirmHas;//没到订餐时间。

    @Column
    @Comment("取消关键词")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cancelKey;

    @Column
    @Comment("取消提示")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cancelTxt;//取消成功。

    @Column
    @Comment("不允许")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cancelNoway;//超过订餐时间，不能取消。

    @Column
    @Comment("没有订餐")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String cancelNo;//你还没有订餐，不能取消。

    @Column
    @Comment("订餐开始时间")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String timeStart;

    @Column
    @Comment("订餐结束时间")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String timeEnd;

    @Column
    @Comment("每份价格")
    @ColDefine(type = ColType.INT, width = 5)
    private Integer money;

    @Column
    @Comment("接口Token")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String token;

    @Column
    @Comment("周末禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfirmKey() {
        return confirmKey;
    }

    public void setConfirmKey(String confirmKey) {
        this.confirmKey = confirmKey;
    }

    public String getConfirmTxt() {
        return confirmTxt;
    }

    public void setConfirmTxt(String confirmTxt) {
        this.confirmTxt = confirmTxt;
    }

    public String getConfirmNoway() {
        return confirmNoway;
    }

    public void setConfirmNoway(String confirmNoway) {
        this.confirmNoway = confirmNoway;
    }

    public String getConfirmNo() {
        return confirmNo;
    }

    public void setConfirmNo(String confirmNo) {
        this.confirmNo = confirmNo;
    }

    public String getConfirmHas() {
        return confirmHas;
    }

    public void setConfirmHas(String confirmHas) {
        this.confirmHas = confirmHas;
    }

    public String getCancelKey() {
        return cancelKey;
    }

    public void setCancelKey(String cancelKey) {
        this.cancelKey = cancelKey;
    }

    public String getCancelTxt() {
        return cancelTxt;
    }

    public void setCancelTxt(String cancelTxt) {
        this.cancelTxt = cancelTxt;
    }

    public String getCancelNoway() {
        return cancelNoway;
    }

    public void setCancelNoway(String cancelNoway) {
        this.cancelNoway = cancelNoway;
    }

    public String getCancelNo() {
        return cancelNo;
    }

    public void setCancelNo(String cancelNo) {
        this.cancelNo = cancelNo;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
