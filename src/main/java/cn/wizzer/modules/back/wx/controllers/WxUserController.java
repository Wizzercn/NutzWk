package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxMenuService;
import cn.wizzer.modules.back.wx.services.WxUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/8.
 */
@IocBean
@At("/private/wx/user")
@Filters({@By(type = PrivateFilter.class)})
public class WxUserController {
    private static final Log log = Logs.get();
    @Inject
    WxUserService wxUserService;
    @Inject
    WxConfigService wxConfigService;

    @At({"/index", "/index/?"})
    @Ok("beetl:/private/wx/user/index.html")
    @RequiresAuthentication
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid",wxid);
        req.setAttribute("wxList",list);
    }

    @At("/data/?")
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(String wxid, @Param("nickname") String nickname, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(nickname)) {
            cnd.and("nickname", "like", "%" + nickname + "%");
        }
        return wxUserService.data(length, start, draw, order, columns, cnd, null);
    }
}
