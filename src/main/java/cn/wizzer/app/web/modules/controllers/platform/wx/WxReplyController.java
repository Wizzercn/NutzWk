package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_reply;
import cn.wizzer.app.wx.modules.services.*;
import cn.wizzer.framework.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wizzer on 2016/7/6.
 */
@IocBean
@At("/platform/wx/reply/conf")
public class WxReplyController {
    private static final Log log = Logs.get();
    @Inject
    private WxReplyTxtService wxReplyTxtService;
    @Inject
    private WxReplyNewsService wxReplyNewsService;
    @Inject
    private WxReplyImgService wxReplyImgService;
    @Inject
    private WxReplyService wxReplyService;
    @Inject
    private WxConfigService wxConfigService;

    @At({"/?", "/?/index/?"})
    @Ok("beetl:/platform/wx/reply/conf/index.html")
    @RequiresPermissions("wx.reply")
    public void index(String type, String wxid, HttpServletRequest req) {
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
        req.setAttribute("type", type);
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
            reply.setOpBy(StringUtil.getPlatformUid());
            wxReplyService.insert(reply);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/?/edit/?")
    @Ok("json")
    @RequiresPermissions("wx.reply")
    public Object edit(String type, String id, HttpServletRequest req) {
        try {
            Wx_reply reply = wxReplyService.fetch(id);
            if ("txt".equals(reply.getMsgType())) {
                req.setAttribute("txt", wxReplyTxtService.fetch(reply.getContent()));
            } else if ("news".equals(reply.getMsgType())) {
                req.setAttribute("txt", wxReplyTxtService.fetch(reply.getContent()));
            } else if ("image".equals(reply.getMsgType())) {
                req.setAttribute("image", wxReplyImgService.fetch(reply.getContent()));
            }
            return Result.success().addData(reply);
        } catch (Exception e) {
            return Result.error();
        }
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/?/delete/?")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "绑定类型:${args[0]}")
    public Object delete(String type, String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/?/delete")
    @Ok("json")
    @RequiresPermissions(value = {"wx.reply.follow.delete", "wx.reply.keyword.delete"}, logical = Logical.OR)
    @SLog(tag = "删除绑定", msg = "绑定类型:${args[0]}")
    public Object deletes(String type, @Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyService.delete(StringUtils.split(id, ","));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/?/data")
    @Ok("json:full")
    @RequiresPermissions("wx.reply")
    public Object data(String type, @Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (!Strings.isBlank(type)) {
                cnd.and("type", "=", type);
            }
            if (!Strings.isBlank(wxid)) {
                cnd.and("wxid", "=", wxid);
            }
            if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
                cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
            }
            return Result.success().addData(wxReplyService.listPageLinks(pageNumber, pageSize, cnd, "^(replyImg|replyTxt|replyNews)$"));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/?/selectData")
    @Ok("json:full")
    @RequiresPermissions("wx.reply")
    public Object selectData(String type, @Param("wxid") String wxid, @Param("msgType") String msgType) {
        try {
            if ("txt".equals(msgType)) {
                return Result.success().addData(wxReplyTxtService.query(Cnd.orderBy().desc("opAt")));
            } else if ("image".equals(msgType)) {
                return Result.success().addData(wxReplyImgService.query(Cnd.orderBy().desc("opAt")));
            } else if ("news".equals(msgType)) {
                return Result.success().addData(wxReplyNewsService.query(Cnd.orderBy().desc("opAt")));
            }
            return Result.error();
        } catch (Exception e) {
            return Result.error();
        }
    }
}
