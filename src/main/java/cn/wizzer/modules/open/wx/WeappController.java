package cn.wizzer.modules.open.wx;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.back.robot.models.Rb_order;
import cn.wizzer.modules.back.robot.models.Rb_user;
import cn.wizzer.modules.back.robot.services.RbOrderService;
import cn.wizzer.modules.back.robot.services.RbUserService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/open/wxopen")
public class WeappController {
    private static final Log log = Logs.get();
    @Inject
    RbOrderService rbOrderService;
    @Inject
    RbUserService rbUserService;

    @At("/bind")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object bind(@Param("qq") String qq, @Param("name") String name, HttpServletRequest req) throws IOException {
        try {
            Rb_user user = rbUserService.fetch(Cnd.where("qq", "=", qq).and("name", "=", name));
            if (user != null) {
                if (user.isDisabled()) {
                    return Result.error(-1, "用户被禁用");
                }
                return Result.success("绑定成功");
            }
            return Result.error(-2, "用户不存在");
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    @At("/list")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object list(@Param("qq") String qq, @Param("year") String year, @Param("month") String month, HttpServletRequest req) throws IOException {
        try {
            List<Rb_order> list = rbOrderService.query(Cnd.where("qq", "=", qq).and("day", "like", year + "-" + month + "%").and("orderStatus","=",0));
            String[] str = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                str[i] = list.get(i).getDay();
            }
            return Result.success("获取成功", str);
        } catch (Exception e) {
            return Result.error("获取失败");
        }
    }

    @At("/order")
    @Ok("json")
    @AdaptBy(type = JsonAdaptor.class)
    public Object order(@Param("qq") String qq, @Param("action") String action, HttpServletRequest req) throws IOException {
        try {
            Rb_user user = rbUserService.fetch(Cnd.where("qq", "=", qq));
            String at = user.getName() + "：";
            String day = DateUtil.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Globals.ROBOT.isDisabled() && is67(day)) {
                return Result.success("周末不开张。");
            }
            int now = (int) (System.currentTimeMillis() / 1000);
            int end = now;
            try {
                end = (int) (sdf.parse(day + " " + Globals.ROBOT.getTimeEnd()).getTime() / 1000);
            } catch (Exception e) {
                log.error(e);
            }
            int start = now;
            try {
                start = (int) (sdf.parse(day + " " + Globals.ROBOT.getTimeStart()).getTime() / 1000);
            } catch (Exception e) {
                log.error(e);
            }
            if ("d".equals(action)) {
                if (now > end) {
                    return Result.success(at + " " + Globals.ROBOT.getConfirmNoway());//超过订餐时间，贿赂群主看看？
                } else if (now < start) {
                    return Result.success(at + " " + Globals.ROBOT.getConfirmNo());//还没到订餐时间，这么快就饿了，早饭没吃吧？
                }
                Rb_order order = rbOrderService.fetch(Cnd.where("qq", "=", qq).and("day", "=", day));
                if (order == null) {
                    //如果订单不存在
                    order = new Rb_order();
                    order.setQq(qq);
                    order.setDay(day);
                    order.setNickname(user.getName());
                    order.setOrderAt((int) (System.currentTimeMillis() / 1000));
                    order.setOrderStatus(0);//0 订餐 1 取消
                    order.setPayStatus(0);//0 未支付 1 已支付
                    order.setMoney(Globals.ROBOT.getMoney());
                    rbOrderService.insert(order);
                    return Result.success(at + " " + Globals.ROBOT.getConfirmTxt());//订餐成功。
                } else {
                    if (order.getOrderStatus() == 1) {
                        rbOrderService.update(Chain.make("orderStatus", 0), Cnd.where("id", "=", order.getId()));
                        return Result.success(at + " " + Globals.ROBOT.getConfirmTxt());//订餐成功。
                    } else {
                        return Result.success(at + " 你已订过啦。");//订餐成功。
                    }
                }
            }
            if ("c".equals(action)) {
                Rb_order order = rbOrderService.fetch(Cnd.where("qq", "=", qq).and("day", "=", day));
                if (now > end) {
                    if (order != null) {
                        return Result.success(at + " " + Globals.ROBOT.getCancelNoway());//超过订餐时间，不能取消，乖乖掏钱吧。
                    } else {
                        return Result.success(at + " " + Globals.ROBOT.getCancelNo());//你还没有订餐，怎么取消？
                    }
                } else if (now < start) {
                    return "";
                }
                if (order == null) {
                    return Result.success(at + " " + Globals.ROBOT.getCancelNo());//你还没有订餐，怎么取消？
                } else {
                    if (order.getOrderStatus() == 0) {
                        rbOrderService.update(Chain.make("orderStatus", 1), Cnd.where("id", "=", order.getId()));
                    }
                    return Result.success(at + " " + Globals.ROBOT.getCancelTxt());//取消成功。
                }
            }
            return Result.error("订餐失败");
        } catch (Exception e) {
            return Result.error("订餐失败");
        }
    }

    /**
     * 判断是否周末
     *
     * @param bDate 日期
     * @return boolean
     */
    private static boolean is67(String bDate) {
        try {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = format1.parse(bDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            } else return false;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
