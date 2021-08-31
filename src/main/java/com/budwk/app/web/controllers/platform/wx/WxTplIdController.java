package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_tpl_id;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxTplIdService;
import com.budwk.app.base.result.Result;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.budwk.app.web.commons.slog.annotation.SLog;
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
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

;

@IocBean
@At("/platform/wx/tpl/id")
public class WxTplIdController {
    private static final Log log = Logs.get();
    @Inject
    private WxTplIdService wxTplIdService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @At({"", "/index/?"})
    @Ok("beetl:/platform/wx/tpl/id/index.html")
    @SaCheckPermission("wx.tpl.id")
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
    @SaCheckPermission("wx.tpl.id")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return Result.success().addData(wxTplIdService.listPage(pageNumber, pageSize, cnd));
    }


    @At
    @Ok("json")
    @SLog(tag = "添加模板", msg = "")
    @SaCheckPermission("wx.tpl.id.add")
    public Object addDo(@Param("..") Wx_tpl_id wxTplId, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxService.getWxApi2(wxTplId.getWxid());
            WxResp wxResp = wxApi2.template_api_add_template(wxTplId.getId());
            if (wxResp.errcode() == 0) {
                wxTplId.setCreatedBy(SecurityUtil.getUserId());
                wxTplId.setTemplate_id(wxResp.template_id());
                wxTplIdService.insert(wxTplId);
                return Result.success();
            }
            return Result.error(wxResp.errmsg());
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At({"/delete", "/delete/?"})
    @Ok("json")
    @SLog(tag = "删除模板", msg = "ID:${args[3].getAttribute('id')}")
    @SaCheckPermission("wx.tpl.id.delete")
    public Object delete(String id, @Param("wxid") String wxid, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxService.getWxApi2(wxid);
            if (ids != null && ids.length > 0) {
                for (String i : ids) {
                    Wx_tpl_id wxTplId = wxTplIdService.fetch(Cnd.where("id", "=", id).and("wxid", "=", wxid));
                    WxResp wxResp = wxApi2.template_api_del_template(wxTplId.getTemplate_id());
                    if (wxResp.errcode() == 0) {
                        wxTplIdService.clear(Cnd.where("id", "=", i).and("wxid", "=", wxid));
                        return Result.success();
                    }
                }
                wxTplIdService.delete(ids);
                req.setAttribute("id", Arrays.toString(ids));
            } else {
                Wx_tpl_id wxTplId = wxTplIdService.fetch(Cnd.where("id", "=", id).and("wxid", "=", wxid));
                WxResp wxResp = wxApi2.template_api_del_template(wxTplId.getTemplate_id());
                if (wxResp.errcode() == 0) {
                    wxTplIdService.clear(Cnd.where("id", "=", id).and("wxid", "=", wxid));
                    return Result.success();
                }
                req.setAttribute("id", id);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

}
