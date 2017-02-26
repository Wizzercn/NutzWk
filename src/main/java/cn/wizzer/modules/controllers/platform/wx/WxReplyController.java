package cn.wizzer.modules.controllers.platform.wx;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.models.wx.Wx_config;
import cn.wizzer.modules.models.wx.Wx_reply;
import cn.wizzer.modules.models.wx.Wx_reply_news;
import cn.wizzer.modules.services.wx.WxConfigService;
import cn.wizzer.modules.services.wx.WxReplyNewsService;
import cn.wizzer.modules.services.wx.WxReplyService;
import cn.wizzer.modules.services.wx.WxReplyTxtService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.*;
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
@At("/platform/wx/reply/conf")
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

    @At("/?")
    @Ok("beetl:/platform/wx/reply/conf/index.html")
    @RequiresAuthentication
    public void index(String type, @Param("wxid") String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxList", list);
        req.setAttribute("wxid", wxid);
        req.setAttribute("type", type);
    }

    @At("/?/add")
    @Ok("beetl:/platform/wx/reply/conf/add.html")
    @RequiresAuthentication
    public void add(String type, @Param("wxid") String wxid, HttpServletRequest req) {
        req.setAttribute("config", wxConfigService.fetch(wxid));
        req.setAttribute("type", type);
        req.setAttribute("wxid", wxid);

    }

    @At("/?/addDo")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.add", "wx.reply.keyword.add"}, logical = Logical.OR)
    @SLog(tag = "添加绑定", msg = "绑定类型:${args[0]}")
    public Object addDo(String type, @Param("..") Wx_reply reply, HttpServletRequest req) {
        try {
            if ("follow".equals(reply.getType())) {
                int c = wxReplyService.count(Cnd.where("type", "=", "follow").and("wxid", "=", reply.getWxid()));
                if (c > 0) {
                    return Result.error("关注事件只可设置一条");
                }
            }
            if ("keyword".equals(reply.getType())) {
                int c = wxReplyService.count(Cnd.where("keyword", "=", reply.getKeyword()).and("wxid", "=", reply.getWxid()));
                if (c > 0) {
                    return Result.error("关键词已存在");
                }
            }
            wxReplyService.insert(reply);
            if ("news".equals(reply.getMsgType())) {
                String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
                int i = 0;
                for (String id : newsIds) {
                    wxReplyNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/?/edit/?")
    @Ok("beetl:/platform/wx/reply/conf/edit.html")
    @RequiresAuthentication
    public Object edit(String type, String id, HttpServletRequest req) {
        Wx_reply reply = wxReplyService.fetch(id);
        if ("txt".equals(reply.getMsgType())) {
            req.setAttribute("txt", wxReplyTxtService.fetch(reply.getContent()));
        } else if ("news".equals(reply.getMsgType())) {
            String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
            List<Wx_reply_news> newsList = wxReplyNewsService.query(Cnd.where("id", "in", newsIds).asc("location"));
            req.setAttribute("news", newsList);
        }
        req.setAttribute("type", reply.getType());
        req.setAttribute("wxid", reply.getWxid());
        req.setAttribute("config", wxConfigService.fetch(reply.getWxid()));
        return reply;
    }

    @At("/?/editDo")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.edit", "wx.reply.keyword.edit"}, logical = Logical.OR)
    @SLog(tag = "修改绑定", msg = "绑定类型:${args[0]}")
    public Object editDo(String type, @Param("..") Wx_reply reply, HttpServletRequest req) {
        try {
            wxReplyService.updateIgnoreNull(reply);
            if ("news".equals(reply.getMsgType())) {
                String[] newsIds = Strings.sBlank(reply.getContent()).split(",");
                int i = 0;
                for (String id : newsIds) {
                    wxReplyNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/?/delete/?")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "绑定类型:${args[0]}")
    public Object delete(String type, String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/?/delete")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "绑定类型:${args[0]}")
    public Object deletes(String type, @Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(StringUtils.split(id, ","));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/?/data")
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(String type, @Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(type)) {
            cnd.and("type", "=", type);
        }
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxReplyService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/?/select")
    @Ok("beetl:/platform/wx/reply/conf/select.html")
    @RequiresAuthentication
    public void select(String type, @Param("wxid") String wxid, @Param("msgType") String msgType, HttpServletRequest req) {
        req.setAttribute("type", type);
        req.setAttribute("wxid", wxid);
        req.setAttribute("msgType", msgType);
    }

    @At("/?/selectData")
    @Ok("json:full")
    @RequiresAuthentication
    public Object selectData(String type, @Param("msgType") String msgType, @Param("wxid") String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if ("txt".equals(msgType)) {
            return wxReplyTxtService.data(length, start, draw, order, columns, cnd, null);
        } else {
            return wxReplyNewsService.data(length, start, draw, order, columns, cnd, null);
        }
    }
}
