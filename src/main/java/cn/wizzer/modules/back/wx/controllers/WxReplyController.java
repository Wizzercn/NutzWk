package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_reply;
import cn.wizzer.modules.back.wx.models.Wx_reply_txt;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxReplyNewsService;
import cn.wizzer.modules.back.wx.services.WxReplyService;
import cn.wizzer.modules.back.wx.services.WxReplyTxtService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

/**
 * Created by wizzer on 2016/7/6.
 */
@IocBean
@At("/private/wx/reply")
@Filters({@By(type = PrivateFilter.class)})
public class WxReplyController {
    private static final Log log = Logs.get();
    @Inject
    WxReplyTxtService wxReplyTxtService;
    @Inject
    WxReplyNewsService wxReplyNewsService;
    @Inject
    WxReplyService wxReplyService;
    @Inject
    WxConfigService wxConfigService;

    @At({"/conf", "/conf/?"})
    @Ok("beetl:/private/wx/reply/index.html")
    @RequiresAuthentication
    public void index(String wxid, @Param("type") String type, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", wxid);
        req.setAttribute("type", type);
    }

    @At
    @Ok("beetl:/private/wx/reply/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.add", "wx.reply.keyword.add"}, logical = Logical.OR)
    @SLog(tag = "添加绑定", msg = "绑定类型:${args[0].type}")
    public Object addDo(@Param("..") Wx_reply reply, HttpServletRequest req) {
        try {
            wxReplyService.insert(reply);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/reply/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return wxReplyService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.edit", "wx.reply.keyword.edit"}, logical = Logical.OR)
    @SLog(tag = "修改绑定", msg = "绑定类型:${args[0].type}")
    public Object editDo(@Param("..") Wx_reply reply, HttpServletRequest req) {
        try {
            wxReplyService.updateIgnoreNull(reply);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "绑定类型:${args[1].getAttribute('type')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("type", wxReplyService.fetch(id).getType());
            wxReplyService.delete(id);
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(StringUtils.split(id, ","));
            return Result.success("system.success", req);
        } catch (Exception e) {
            return Result.error("system.error", req);
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("type") String type, @Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(type)) {
            cnd.and("type", "=", type);
        }
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxReplyService.data(length, start, draw, order, columns, cnd, null);
    }
}
