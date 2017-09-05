package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxTplLogService;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @RequiresPermissions("wx.tpl.log")
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
    @RequiresPermissions("wx.tpl.log")
    public Object data(@Param("wxid") String wxid, @Param("nickname") String nickname, @Param("openid") String openid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        if (!Strings.isBlank(wxid)) {
            cnd.and("openid", "=", openid);
        }
        return wxTplLogService.data(length, start, draw, order, columns, cnd, null);
    }

}
