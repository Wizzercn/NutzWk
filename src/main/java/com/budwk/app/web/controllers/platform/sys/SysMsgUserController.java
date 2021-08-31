package com.budwk.app.web.controllers.platform.sys;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.budwk.app.base.result.Result;
import com.budwk.app.sys.services.SysMsgService;
import com.budwk.app.sys.services.SysMsgUserService;
import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@IocBean
@At("/platform/sys/msg/user")
public class SysMsgUserController {
    private static final Log log = Logs.get();
    @Inject
    private SysMsgUserService sysMsgUserService;
    @Inject
    private SysMsgService sysMsgService;

    @At("/all")
    @Ok("beetl:/platform/sys/msg/user/indexAll.html")
    @SaCheckPermission("sys.msg.all")
    public void index(@Param("type") String type, HttpServletRequest req) {
        req.setAttribute("type", Strings.isBlank(type) ? "all" : type);
    }

    @At("/read")
    @Ok("beetl:/platform/sys/msg/user/indexRead.html")
    @SaCheckPermission("sys.msg.read")
    public void read(@Param("type") String type, HttpServletRequest req) {
        req.setAttribute("type", Strings.isBlank(type) ? "all" : type);

    }

    @At("/unread")
    @Ok("beetl:/platform/sys/msg/user/indexUnread.html")
    @SaCheckPermission("sys.msg.unread")
    public void unread(@Param("type") String type, HttpServletRequest req) {
        req.setAttribute("type", Strings.isBlank(type) ? "all" : type);
    }

    @At("/data/?")
    @Ok("json:full")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    public Object data(String status, @Param("searchType") String type, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(status) && "read".equals(status)) {
                cnd.and("a.status", "=", 1);
            }
            if (Strings.isNotBlank(status) && "unread".equals(status)) {
                cnd.and("a.status", "=", 0);
            }
            cnd.and("a.loginname", "=", SecurityUtil.getUserLoginname());
            cnd.and("a.delFlag", "=", false);
            cnd.desc("a.createdAt");
            if (Strings.isNotBlank(type) && !"all".equals(type)) {
                cnd.and("b.type", "=", type);
            }
            Sql sql = Sqls.create("SELECT b.type,b.title,b.sendat,a.* FROM sys_msg b LEFT JOIN sys_msg_user a ON b.id=a.msgid $condition");
            sql.setCondition(cnd);
            Sql sqlCount = Sqls.create("SELECT count(*) FROM sys_msg b LEFT JOIN sys_msg_user a ON b.id=a.msgid $condition");
            sqlCount.setCondition(cnd);
            return Result.success().addData(sysMsgService.listPage(pageNumber, pageSize, sql, sqlCount));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    @SLog(tag = "站内消息", msg = "${req.getAttribute('id')}")
    public Object delete(String id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                sysMsgUserService.update(Chain.make("delFlag", true)
                        .add("updatedAt", System.currentTimeMillis())
                        .add("updatedBy", SecurityUtil.getUserId()), Cnd.where("id", "in", ids).and("loginname", "=", SecurityUtil.getUserLoginname()));
                req.setAttribute("id", Arrays.toString(ids));
            } else {
                sysMsgUserService.update(Chain.make("delFlag", true)
                        .add("updatedAt", System.currentTimeMillis())
                        .add("updatedBy", SecurityUtil.getUserId()), Cnd.where("id", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
                req.setAttribute("id", id);
            }
            sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckLogin
    public Object unread_num() {
        try {
            NutMap nutMap = NutMap.NEW();
            nutMap.put("system", sysMsgUserService.count(Sqls.create("SELECT count(*) from sys_msg a,sys_msg_user b WHERE a.id=b.msgId AND a.type='system' AND a.delFlag=false AND b.status=0 AND b.delFlag=false AND b.loginname=@loginname").setParam("loginname", SecurityUtil.getUserLoginname())));
            nutMap.put("user", sysMsgUserService.count(Sqls.create("SELECT count(*) from sys_msg a,sys_msg_user b WHERE a.id=b.msgId AND a.type='user' AND a.delFlag=false AND b.status=0 AND b.delFlag=false AND b.loginname=@loginname").setParam("loginname", SecurityUtil.getUserLoginname())));
            return Result.success().addData(nutMap);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/status/read")
    @Ok("json")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    @SLog(tag = "站内消息", msg = "${req.getAttribute('id')}")
    public Object read(@Param("ids") String[] ids, HttpServletRequest req) {
        try {
            sysMsgUserService.update(Chain.make("status", 1).add("readAt", Times.getTS())
                    .add("createdAt", System.currentTimeMillis())
                    .add("createdBy", SecurityUtil.getUserId()), Cnd.where("id", "in", ids).and("loginname", "=", SecurityUtil.getUserLoginname()));
            sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            req.setAttribute("id", Arrays.toString(ids));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/status/readAll")
    @Ok("json")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    @SLog(tag = "站内消息", msg = "readAll")
    public Object readAll(HttpServletRequest req) {
        try {
            sysMsgUserService.update(Chain.make("status", 1).add("readAt", Times.getTS())
                    .add("createdAt", System.currentTimeMillis()).add("createdBy", SecurityUtil.getUserId()), Cnd.where("loginname", "=", SecurityUtil.getUserLoginname()));
            sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/all/detail/?")
    @Ok("beetl:/platform/sys/msg/user/detailAll.html")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    public void allDetail(String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //判断用户是否是正常获取消息
            int num = sysMsgUserService.count(Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
            if (num > 0) {
                req.setAttribute("obj", sysMsgService.fetch(id));
                sysMsgUserService.update(Chain.make("status", 1), Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
                sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            } else {
                req.setAttribute("obj", null);
            }
        } else {
            req.setAttribute("obj", null);
        }
    }

    @At("/read/detail/?")
    @Ok("beetl:/platform/sys/msg/user/detailRead.html")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    public void readDetail(String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //判断用户是否是正常获取消息
            int num = sysMsgUserService.count(Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
            if (num > 0) {
                req.setAttribute("obj", sysMsgService.fetch(id));
                sysMsgUserService.update(Chain.make("status", 1), Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
                sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            } else {
                req.setAttribute("obj", null);
            }
        } else {
            req.setAttribute("obj", null);
        }
    }

    @At("/unread/detail/?")
    @Ok("beetl:/platform/sys/msg/user/detailUnread.html")
    @SaCheckPermission(value = {"sys.msg.all", "sys.msg.read", "sys.msg.unread"}, mode = SaMode.OR)
    public void unreadDetail(String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            //判断用户是否是正常获取消息
            int num = sysMsgUserService.count(Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
            if (num > 0) {
                req.setAttribute("obj", sysMsgService.fetch(id));
                sysMsgUserService.update(Chain.make("status", 1), Cnd.where("msgid", "=", id).and("loginname", "=", SecurityUtil.getUserLoginname()));
                sysMsgUserService.deleteCache(SecurityUtil.getUserLoginname());
            } else {
                req.setAttribute("obj", null);
            }
        } else {
            req.setAttribute("obj", null);
        }
    }

}
