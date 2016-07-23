package cn.wizzer.modules.back.robot.models;

import cn.wizzer.common.base.Model;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("rb_order")
@TableIndexes({@Index(name = "INDEX_RB_ORDER_DAY", fields = {"day"}, unique = false),@Index(name = "INDEX_RB_ORDER_QQ", fields = {"qq"}, unique = false),@Index(name = "INDEX_RB_ORDER_AT", fields = {"orderAt"}, unique = false)})
public class Rb_order extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("QQ号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String qq;

    @Column
    @Comment("QQ昵称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String nickname;

    @Column
    @Comment("订餐日期")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String day;

    @Column
    @Comment("订餐状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer orderStatus;//0 订餐  1 取消

    @Column
    @Comment("订餐时间")
    @ColDefine(type = ColType.INT, width = 5)
    private Integer orderAt;

    @Column
    @Comment("订餐价格")
    @ColDefine(type = ColType.INT, width = 5)
    private Integer money;

    @Column
    @Comment("支付状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer payStatus;//0 未支付  1 已支付

    @Column
    @Comment("支付时间")
    @ColDefine(type = ColType.INT, width = 5)
    private Integer payAt;

    @One(target = Rb_user.class, field = "qq")
    private Rb_user user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Integer orderAt) {
        this.orderAt = orderAt;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayAt() {
        return payAt;
    }

    public void setPayAt(Integer payAt) {
        this.payAt = payAt;
    }

    public Rb_user getUser() {
        return user;
    }

    public void setUser(Rb_user user) {
        this.user = user;
    }
}
