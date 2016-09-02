package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_tpl_list;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxTplListService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/private/wx/tpl/list")
@Filters({@By(type = PrivateFilter.class)})
public class WxTplListController {
    private static final Log log = Logs.get();
    @Inject
    private WxTplListService wxTplListService;
    @Inject
    private WxConfigService wxConfigService;

    @At({"", "/index/?"})
    @Ok("beetl:/private/wx/tpl/list/index.html")
    @RequiresAuthentication
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", Strings.sBlank(wxid));
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxTplListService.data(length, start, draw, order, columns, cnd, null);
    }


    @At
    @Ok("json")
    @SLog(tag = "获取模板列表", msg = "")
    @RequiresPermissions("wx.tpl.list.get")
    public Object getDo(@Param("wxid") String wxid, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            WxResp wxResp = wxApi2.get_all_private_template();
            List<Wx_tpl_list> lists = wxResp.getList("template_list", Wx_tpl_list.class);
            for (Wx_tpl_list o : lists) {
                o.setWxid(wxid);
                wxTplListService.insert(o);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
