package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wizzer on 2016/7/3.
 */
@IocBean
@At("/private/wx/conf/account")
@Filters({@By(type = PrivateFilter.class)})
public class WxConfigController {
    private static final Log log = Logs.get();
    @Inject
    WxConfigService wxConfigService;


    @At("")
    @Ok("beetl:/private/wx/account/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/wx/account/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.account.add")
    @SLog(tag = "添加帐号", msg = "帐号名称:${args[0].appname}")
    public Object addDo(@Param("..") Wx_config conf, HttpServletRequest req) {
        try {
            wxConfigService.insert(conf);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/account/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return wxConfigService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.conf.account.edit")
    @SLog(tag = "修改帐号", msg = "帐号名称:${args[0].appname}")
    public Object editDo(@Param("..") Wx_config conf, HttpServletRequest req) {
        try {
            wxConfigService.updateIgnoreNull(conf);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.conf.account.delete")
    @SLog(tag = "删除帐号", msg = "帐号名称:${args[1].getAttribute('appname')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("appname", wxConfigService.fetch(id).getAppname());
            wxConfigService.delete(id);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return wxConfigService.data(length, start, draw, order, columns, cnd, null);
    }
}
