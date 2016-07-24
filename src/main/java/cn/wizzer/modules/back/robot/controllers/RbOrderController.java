package cn.wizzer.modules.back.robot.controllers;

import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.common.util.DateUtil;
import cn.wizzer.modules.back.robot.models.Rb_user;
import cn.wizzer.modules.back.robot.services.RbOrderService;
import cn.wizzer.modules.back.robot.services.RbUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wizzer on 2016/7/23.
 */
@IocBean
@At("/private/robot/order")
@Filters({@By(type = PrivateFilter.class)})
public class RbOrderController {
    private static final Log log = Logs.get();
    @Inject
    RbOrderService rbOrderService;
    @Inject
    RbUserService rbUserService;

    @At("")
    @Ok("beetl:/private/robot/order/index.html")
    @RequiresAuthentication
    public void index() {
    }

    @At("/detail/?")
    @Ok("beetl:/private/robot/order/detail.html")
    @RequiresAuthentication
    public void detail(String qq, @Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        Rb_user user = rbUserService.fetch(qq);
        req.setAttribute("qq", qq);
        req.setAttribute("name", user.getName());
        req.setAttribute("beginDate", beginDate);
        req.setAttribute("endDate", endDate);

    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object enable(String id, HttpServletRequest req) {
        try {
            rbOrderService.update(Chain.make("orderStatus", 0), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object disable(String id, HttpServletRequest req) {
        try {
            rbOrderService.update(Chain.make("orderStatus", 1), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/payno/?")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object payno(String id, HttpServletRequest req) {
        try {
            rbOrderService.update(Chain.make("payStatus", 0), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/payone/?")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object payone(String id, HttpServletRequest req) {
        try {
            rbOrderService.update(Chain.make("payStatus", 1), Cnd.where("id", "=", id));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/payqq/?")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object payqq(String qq, @Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        try {
            Cnd cnd = Cnd.NEW();
            if (!Strings.isBlank(qq))
                cnd.and("qq", "=", qq);
            int startT = DateUtil.getTime(Strings.sBlank(beginDate) + " 00:00:00");
            int endT = DateUtil.getTime(Strings.sBlank(endDate) + " 23:59:59");
            cnd.and("orderAt", ">=", startT);
            cnd.and("orderAt", "<=", endT);
            cnd.and("payStatus", "=", 0);
            rbOrderService.update(Chain.make("payStatus", 1), cnd);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/payall")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object payall(@Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        try {
            Cnd cnd = Cnd.NEW();
            int startT = DateUtil.getTime(Strings.sBlank(beginDate) + " 00:00:00");
            int endT = DateUtil.getTime(Strings.sBlank(endDate) + " 23:59:59");
            cnd.and("orderAt", ">=", startT);
            cnd.and("orderAt", "<=", endT);
            cnd.and("payStatus", "=", 0);
            cnd.and("orderStatus", "=", 0);
            rbOrderService.update(Chain.make("payStatus", 1), cnd);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/payinfo")
    @Ok("json")
    @RequiresPermissions("order.robot.order.edit")
    public Object payinfo(@Param("beginDate") String beginDate, @Param("endDate") String endDate, HttpServletRequest req) {
        try {
            String sql = "SELECT ta.qq,ta.money,COUNT(ta.id) AS c,SUM(ta.money) AS m,tb.name FROM rb_order ta,rb_user tb WHERE ta.qq=tb.qq AND ta.orderStatus=0 AND ta.payStatus=0 ";
            int startT = DateUtil.getTime(Strings.sBlank(beginDate) + " 00:00:00");
            int endT = DateUtil.getTime(Strings.sBlank(endDate) + " 23:59:59");
            sql += " and ta.orderAt>=" + startT;
            sql += " and ta.orderAt<=" + endT;
            sql += " and tb.disabled=0";
            sql += " GROUP BY ta.qq";
            sql += " order by COUNT(ta.id) desc ";
            return Result.success("system.success", rbOrderService.list(Sqls.create(sql)));
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detai/data/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object detailData(String qq, @Param("payStatus") String payStatus, @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(qq))
            cnd.and("qq", "=", qq);
        int startT = DateUtil.getTime(Strings.sBlank(beginDate) + " 00:00:00");
        int endT = DateUtil.getTime(Strings.sBlank(endDate) + " 23:59:59");
        cnd.and("orderAt", ">=", startT);
        cnd.and("orderAt", "<=", endT);
        return rbOrderService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("qq") String qq, @Param("name") String name, @Param("payStatus") String payStatus, @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT ta.qq,ta.money,COUNT(ta.id) AS c,SUM(ta.money) AS m,SUM(CASE WHEN ta.payStatus=0 THEN 1 ELSE 0 END) AS w,SUM(CASE WHEN ta.payStatus=1 THEN 1 ELSE 0 END) AS f,tb.name FROM rb_order ta,rb_user tb WHERE ta.qq=tb.qq AND ta.orderStatus=0 ";
        if (!Strings.isBlank(qq))
            sql += " and ta.qq='" + qq + "'";
        if (!Strings.isBlank(qq))
            sql += " and tb.name='" + name + "'";
        if ("w".equals(payStatus)) {
            sql += " and ta.payStatus=0";
        } else if ("f".equals(payStatus)) {
            sql += " and ta.payStatus=1";
        }
        int startT = DateUtil.getTime(Strings.sBlank(beginDate) + " 00:00:00");
        int endT = DateUtil.getTime(Strings.sBlank(endDate) + " 23:59:59");
        sql += " and ta.orderAt>=" + startT;
        sql += " and ta.orderAt<=" + endT;
        sql += " and tb.disabled=0";
        sql += " GROUP BY ta.qq";
        Sql countSql = Sqls.create(sql);
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                String str = Sqls.escapeSqlFieldValue(col.getData()).toString();
                if ("qq".equals(str)) {
                    s += " order by ta." + str + " " + o.getDir();
                } else if ("name".equals(str)) {
                    s += " order by tb." + str + " " + o.getDir();
                } else if ("m".equals(str)) {
                    s += " order by SUM(ta.money) " + o.getDir();
                } else if ("c".equals(str)) {
                    s += " order by COUNT(ta.id) " + o.getDir();
                } else if ("w".equals(str)) {
                    s += " order by SUM(CASE WHEN ta.payStatus=0 THEN 1 ELSE 0 END) " + o.getDir();
                } else if ("f".equals(str)) {
                    s += " order by SUM(CASE WHEN ta.payStatus=1 THEN 1 ELSE 0 END) " + o.getDir();
                }
            }
        }
        return rbOrderService.data(length, start, draw, countSql, Sqls.create(s));
    }
}
