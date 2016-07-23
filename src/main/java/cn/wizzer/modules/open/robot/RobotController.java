package cn.wizzer.modules.open.robot;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.back.robot.models.Rb_order;
import cn.wizzer.modules.back.robot.models.Rb_user;
import cn.wizzer.modules.back.robot.services.RbConfigService;
import cn.wizzer.modules.back.robot.services.RbOrderService;
import cn.wizzer.modules.back.robot.services.RbUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Hex;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/22.
 */
@IocBean
@At("/open/robot")
public class RobotController {
    private static final Log log = Logs.get();
    @Inject
    RbConfigService rbConfigService;
    @Inject
    RbOrderService rbOrderService;
    @Inject
    RbUserService rbUserService;
    /**
     * 命令开始符号
     */
    private static final char cmd1 = ':';
    private static final char cmd2 = '：';
    /**
     * AT模板
     */
    private static final String AT_TPL = "@%s[%s]";

    @At
    @Ok("raw")
    @Fail("void")
    public Object msg(HttpServletRequest req) throws Exception {
        NutMap data=new NutMap();
        if(req.getParameter("Sender")==null){
            return "";
        }
        data.addv("GroupName",new String(req.getParameter("GroupName").getBytes("iso-8859-1"), "utf-8"));
        data.addv("Sender",new String(req.getParameter("Sender").getBytes("iso-8859-1"), "utf-8"));
        data.addv("SendTime",new String(req.getParameter("SendTime").getBytes("iso-8859-1"), "utf-8"));
        data.addv("SenderName",new String(req.getParameter("SenderName").getBytes("iso-8859-1"), "utf-8"));
        data.addv("Message",new String(req.getParameter("Message").getBytes("iso-8859-1"), "utf-8"));
        data.addv("RobotQQ",new String(req.getParameter("RobotQQ").getBytes("iso-8859-1"), "utf-8"));
        data.addv("Event",new String(req.getParameter("Event").getBytes("iso-8859-1"), "utf-8"));
        data.addv("Key",new String(req.getParameter("Key").getBytes("iso-8859-1"), "utf-8"));
        data.addv("GroupId",new String(req.getParameter("GroupId").getBytes("iso-8859-1"), "utf-8"));
        if (!Strings.equals(data.getString("Event"), "ClusterIM")) {
            return "";
        }
        String token = data.getString("Key");
        if (!Strings.sBlank(token).equals(Globals.ROBOT.getToken())) {
            return "";
        }
        log.debug("data:" + Json.toJson(data));
        if (!Strings.startsWithChar(data.getString("Message"), cmd1) && !Strings.startsWithChar(data.getString("Message"), cmd2)) {
            return "";
        }
        //第一步,判断用户表是否存在,不存在则创建
        String qq = data.getString("Sender");
        String nickname = data.getString("SenderName");
        Rb_user user = rbUserService.fetch(Cnd.where("qq", "=", qq));
        if (user == null) {
            user = new Rb_user();
            user.setQq(qq);
            user.setName(nickname);
            user.setDisabled(false);
            rbUserService.insert(user);
        }
        if (user.isDisabled()) {
            return user.getName() + "：" + " 你已被禁止使用此功能，请联系管理员。";
        }
        //第二步,根据关键词判断操作类型
        String key = Strings.sBlank(data.getString("Message"));
        String day = DateUtil.getDate();
        log.debug("key::" + key);
        if (key.length() == 1)
            return "";
        key = key.substring(1).trim();
        if (key.length() == 0)
            return "";
        if (key.equals("签到"))
            return "";
        if (Globals.ROBOT == null)
            return "";
        if ("查询".equals(key) || "帮助".equals(key) || "help".equals(key.toLowerCase())) {
            return ":订饭 or :取消  (" + Globals.ROBOT.getTimeStart() + " 至 " + Globals.ROBOT.getTimeEnd() + ")";
        }
        if ("统计".equals(key) || "合计".equals(key) || "count".equals(key.toLowerCase())) {
            int count = rbOrderService.count(Cnd.where("day", "=", day).and("orderStatus", "=", 0));
            return day + " 订餐人数： " + count + "人";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Globals.ROBOT.isDisabled() && is67(day)) {
            return "周末不开张。";
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
        String at = user.getName() + "：";//String.format(AT_TPL, nickname, qq);
        String[] qr = StringUtils.split(Globals.ROBOT.getConfirmKey(), ",");
        //第三步,如果是订餐
        if (useLoop(qr, key)) {
            if (now > end) {
                return at + " " + Globals.ROBOT.getConfirmNoway();//超过订餐时间，贿赂群主看看？
            } else if (now < start) {
                return at + " " + Globals.ROBOT.getConfirmNo();//还没到订餐时间，这么快就饿了，早饭没吃吧？
            }
            Rb_order order = rbOrderService.fetch(Cnd.where("qq", "=", qq).and("day", "=", day));
            if (order == null) {
                //如果订单不存在
                order = new Rb_order();
                order.setQq(qq);
                order.setDay(day);
                order.setNickname(nickname);
                order.setOrderAt((int) (System.currentTimeMillis() / 1000));
                order.setOrderStatus(0);//0 订餐 1 取消
                order.setPayStatus(0);//0 未支付 1 已支付
                order.setMoney(Globals.ROBOT.getMoney());
                rbOrderService.insert(order);
                return at + " " + Globals.ROBOT.getConfirmTxt();//订餐成功。
            } else {
                if (order.getOrderStatus() == 1) {
                    rbOrderService.update(Chain.make("orderStatus", 0), Cnd.where("id", "=", order.getId()));
                }
                return at + " " + Globals.ROBOT.getConfirmTxt();//订餐成功。
            }
        }
        //第四步,如果是取消订餐
        String[] cl = StringUtils.split(Globals.ROBOT.getCancelKey(), ",");
        if (useLoop(cl, key)) {
            Rb_order order = rbOrderService.fetch(Cnd.where("qq", "=", qq).and("day", "=", day));
            if (now > end) {
                if (order != null) {
                    return at + " " + Globals.ROBOT.getCancelNoway();//超过订餐时间，不能取消，乖乖掏钱吧。
                } else {
                    return at + " " + Globals.ROBOT.getCancelNo();//你还没有订餐，怎么取消？
                }
            } else if (now < start) {
                return "";
            }
            if (order == null) {
                return at + " " + Globals.ROBOT.getCancelNo();//你还没有订餐，怎么取消？
            } else {
                if (order.getOrderStatus() == 0) {
                    rbOrderService.update(Chain.make("orderStatus", 1), Cnd.where("id", "=", order.getId()));
                }
                return at + " " + Globals.ROBOT.getCancelTxt();//取消成功。
            }
        }
        return "";
    }

    /**
     * 判断字符串在数组里
     *
     * @param arr         数组
     * @param targetValue 字符串
     * @return boolean
     */
    private static boolean useLoop(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }
        return false;
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
