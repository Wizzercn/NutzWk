package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_tpl_list;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxTplListService;
import com.budwk.app.base.result.Result;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.web.commons.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

;

@IocBean
@At("/platform/wx/tpl/list")
public class WxTplListController {
    private static final Log log = Logs.get();
    @Inject
    private WxTplListService wxTplListService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @At({"", "/index/?"})
    @Ok("beetl:/platform/wx/tpl/list/index.html")
    @RequiresPermissions("wx.tpl.list")
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
    @RequiresPermissions("wx.tpl.list")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return Result.success().addData(wxTplListService.listPage(pageNumber, pageSize, cnd));
    }

    @At
    @Ok("json")
    @SLog(tag = "获取模板列表", msg = "")
    @RequiresPermissions("wx.tpl.list.get")
    public Object getDo(@Param("wxid") String wxid, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxService.getWxApi2(wxid);
            WxResp wxResp = wxApi2.get_all_private_template();
            List<Wx_tpl_list> lists = wxResp.getList("template_list", Wx_tpl_list.class);
            for (Wx_tpl_list o : lists) {
                o.setWxid(wxid);
                o.setCreatedBy(ShiroUtil.getPlatformUid());
                try {
                    wxTplListService.insert(o);
                } catch (Exception e) {
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

}
