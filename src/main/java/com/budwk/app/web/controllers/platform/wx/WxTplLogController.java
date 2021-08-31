package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxTplLogService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.base.result.Result;;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@IocBean
@At("/platform/wx/tpl/log")
public class WxTplLogController {
    private static final Log log = Logs.get();
    @Inject
    private WxTplLogService wxTplLogService;
    @Inject
    private WxConfigService wxConfigService;

    @At({"", "/index/?"})
    @Ok("beetl:/platform/wx/tpl/log/index.html")
    @SaCheckPermission("wx.tpl.log")
    public void index(String wxid, HttpServletRequest req) {
        Wx_config wxConfig = null;
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxConfig = list.get(0);
        }
        if (Strings.isNotBlank(wxid)) {
            wxConfig = wxConfigService.fetch(wxid);
        }
        req.setAttribute("wxConfig", wxConfig);
        req.setAttribute("wxList", list);
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("wx.tpl.log")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxTplLogService.listPage(pageNumber, pageSize, cnd));
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @SaCheckPermission("wx.tpl.log")
    @SLog(tag = "删除模板发送日志", msg = "ID:${args[2].getAttribute('id')}")
    public Object delete(String oneId, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                wxTplLogService.delete(ids);
                req.setAttribute("id", Arrays.toString(ids));
            } else {
                wxTplLogService.delete(oneId);
                req.setAttribute("id", oneId);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
