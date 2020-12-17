package com.budwk.app.web.controllers.platform.sys;

import com.budwk.app.base.page.Pagination;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.sys.models.Sys_msg;
import com.budwk.app.sys.services.SysMsgService;
import com.budwk.app.sys.services.SysMsgUserService;
import com.budwk.app.sys.services.SysUserService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.web.commons.utils.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
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

;

@IocBean
@At("/platform/sys/msg")
public class SysMsgController {
    private static final Log log = Logs.get();
    @Inject
    private SysMsgService sysMsgService;
    @Inject
    private SysMsgUserService sysMsgUserService;
    @Inject
    private SysUserService sysUserService;

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
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            List<Map> mapList = new ArrayList<>();
            Pagination pagination = sysMsgService.listPage(pageNumber, pageSize, cnd);
            for (Object msg : pagination.getList()) {
                NutMap map = Lang.obj2nutmap(msg);
                map.put("all_num", sysMsgUserService.count(Cnd.where("msgId", "=", map.get("id", ""))));
                map.put("unread_num", sysMsgUserService.count(Cnd.where("msgId", "=", map.get("id", "")).and("status", "=", 0)));
                mapList.add(map);
            }
            pagination.setList(mapList);
            return Result.success().addData(pagination);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.msg")
    public Object user_view_data(@Param("type") String type, @Param("id") String id, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            String sql = "SELECT a.loginname,a.username,a.mobile,a.email,a.disabled,a.unitid,b.name as unitname,c.status,c.readat FROM sys_user a,sys_unit b,sys_msg_user c WHERE a.unitid=b.id \n" +
                    "and a.loginname=c.loginname and c.msgId='" + id + "' ";
            if (Strings.isNotBlank(type) && "unread".equals(type)) {
                sql += " and c.status=0 ";
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                sql += " order by a." + pageOrderName + " " + PageUtil.getOrder(pageOrderBy);
            }
            return Result.success().addData(sysMsgService.listPage(pageNumber, pageSize, Sqls.create(sql)));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/addDo")
    @Ok("json")
    @RequiresPermissions("sys.manager.msg.add")
    @SLog(tag = "站内消息", msg = "${args[0].title}")
    public Object addDo(@Param("..") NutMap nutMap, HttpServletRequest req) {
        try {
            Sys_msg sysMsg = nutMap.getAs("msg", Sys_msg.class);
            sysMsg.setNote(nutMap.getString("note", ""));
            sysMsg.setSendAt(Times.getTS());
            sysMsg.setCreatedBy(ShiroUtil.getPlatformUid());
            String[] users = StringUtils.split(nutMap.getString("users", ""), ",");
            sysMsgService.saveMsg(sysMsg, users);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:{locked:'password|salt',ignoreNull:false}")
    @RequiresPermissions("sys.manager.user")
    public Object user_data(@Param("searchUnit") String searchUnit, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (Strings.isNotBlank(searchName) && Strings.isNotBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(sysUserService.listPageLinks(pageNumber, pageSize, cnd, "unit"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/delete/?"})
    @Ok("json")
    @RequiresPermissions("sys.manager.msg.delete")
    @SLog(tag = "站内消息", msg = "站内信标题:${req.getAttribute('title')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", sysMsgService.fetch(id).getTitle());
            sysMsgService.deleteMsg(id);
            sysMsgUserService.clearCache();
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


}
