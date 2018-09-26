package cn.wizzer.app.web.modules.controllers.platform.sys;

import cn.wizzer.app.sys.modules.models.Sys_msg;
import cn.wizzer.app.sys.modules.services.SysMsgService;
import cn.wizzer.app.sys.modules.services.SysMsgUserService;
import cn.wizzer.app.sys.modules.services.SysUserService;
import cn.wizzer.app.web.commons.ext.websocket.WkNotifyService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.OffsetPager;
import cn.wizzer.framework.page.Pagination;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IocBean
@At("/platform/sys/msg")
public class SysMsgController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private SysMsgService sysMsgService;
    @Inject
    @Reference
    private SysMsgUserService sysMsgUserService;
    @Inject
    @Reference
    private SysUserService sysUserService;
    @Inject
    private WkNotifyService wkNotifyService;

    @At({"/", "/list/?"})
    @Ok("beetl:/platform/sys/msg/index.html")
    @RequiresPermissions("sys.manager.msg")
    public void index(String type, HttpServletRequest req) {
        req.setAttribute("type", Strings.isBlank(type) ? "all" : type);
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.msg")
    public Object data(@Param("searchType") String searchType, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(searchType) && !"all".equals(searchType)) {
                cnd.and("type", "=", searchType);
            }
            cnd.and("delFlag", "=", false);
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            List<Map> mapList = new ArrayList<>();
            Pagination pagination = sysMsgService.listPage(pageNumber, pageSize, cnd);
            for (Object msg : pagination.getList()) {
                Map map = Lang.obj2map(msg);
                map.put("all_num", sysMsgUserService.count(Cnd.where("msgId", "=", map.getOrDefault("id", ""))));
                map.put("unread_num", sysMsgUserService.count(Cnd.where("msgId", "=", map.getOrDefault("id", "")).and("status", "=", 0)));
                mapList.add(map);
            }
            pagination.setList(mapList);
            return Result.success().addData(pagination);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/addDo")
    @Ok("json")
    @RequiresPermissions("sys.manager.msg.add")
    @SLog(tag = "站内消息", msg = "${args[0].title}")
    public Object addDo(@Param("..") Sys_msg sysMsg, @Param("users") String[] users, HttpServletRequest req) {
        try {
            sysMsg.setSendAt(Times.getTS());
            sysMsg.setOpBy(StringUtil.getPlatformUid());
            Sys_msg sys_msg = sysMsgService.saveMsg(sysMsg, users);
            if (sys_msg != null) {
                wkNotifyService.notify(sys_msg, users);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/delete/?"})
    @Ok("json")
    @RequiresPermissions("sys.manager.msg.delete")
    @SLog(tag = "站内消息", msg = "${req.getAttribute('id')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            sysMsgService.deleteMsg(id);
            req.setAttribute("id", id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/msg/detail.html")
    @RequiresPermissions("sys.manager.msg")
    public void detail(String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", sysMsgService.fetch(id));
        } else {
            req.setAttribute("obj", null);
        }
    }

    @At
    @Ok("beetl:/platform/sys/msg/selectUser.html")
    @RequiresPermissions("sys.manager.msg")
    public void selectUser(HttpServletRequest req) {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.msg")
    public Object selectData(@Param("name") String name, @Param("users") String users, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.loginname,a.username,a.disabled,a.unitid,b.name as unitname FROM sys_user a,sys_unit b WHERE a.unitid=b.id ";
        if (!Strings.isBlank(name)) {
            sql += " and (a.loginname like '%" + name + "%' or a.username like '%" + name + "%') ";
        }
        if (!Strings.isBlank(users)) {
            String[] s = StringUtils.split(users, ",");
            for (String u : s) {
                sql += " and a.loginname<>'" + u + "'";
            }
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return sysUserService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }

    @At("/user/?/?")
    @Ok("beetl:/platform/sys/msg/user.html")
    @RequiresPermissions("sys.manager.msg")
    public void allUser(String id, String status, HttpServletRequest req) {
        req.setAttribute("id", id);
        req.setAttribute("status", status);
    }

    @At("/userData/?/?")
    @Ok("json:full")
    @RequiresPermissions("sys.manager.msg")
    public Object allUserData(String id, String status, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String sql = "SELECT a.loginname,a.username,a.disabled,a.unitid,b.name as unitname,c.status,c.readat FROM sys_user a,sys_unit b,sys_msg_user c WHERE a.unitid=b.id \n" +
                "and a.loginname=c.loginname and c.msgId='" + id + "' ";
        if (Strings.isNotBlank(status) && "unread".equals(status)) {
            sql += " and c.status=0";
        }
        String s = sql;
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        return sysUserService.data(length, start, draw, Sqls.create(sql), Sqls.create(s));
    }
}
