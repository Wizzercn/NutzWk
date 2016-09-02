package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Globals;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.sys.models.Sys_config;
import cn.wizzer.modules.back.wx.models.Wx_reply_txt;
import cn.wizzer.modules.back.wx.services.WxReplyTxtService;
import org.apache.commons.lang3.StringUtils;
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
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/private/wx/reply/txt")
@Filters({@By(type = PrivateFilter.class)})
public class WxReplyTxtController {
    private static final Log log = Logs.get();
    @Inject
    WxReplyTxtService wxReplyTxtService;

    @At("")
    @Ok("beetl:/private/wx/reply/txt/index.html")
    @RequiresAuthentication
    public void index() {

    }

    @At
    @Ok("beetl:/private/wx/reply/txt/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.add")
    @SLog(tag = "添加回复文本", msg = "文本标题:${args[0].title}")
    public Object addDo(@Param("..") Wx_reply_txt txt, HttpServletRequest req) {
        try {
            wxReplyTxtService.insert(txt);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/private/wx/reply/txt/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
        return wxReplyTxtService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.edit")
    @SLog(tag = "修改回复文本", msg = "文本标题:${args[0].title}")
    public Object editDo(@Param("..") Wx_reply_txt txt, HttpServletRequest req) {
        try {
            wxReplyTxtService.updateIgnoreNull(txt);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.delete")
    @SLog(tag = "删除回复文本", msg = "文本标题:${args[1].getAttribute('title')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxReplyTxtService.fetch(id).getTitle());
            wxReplyTxtService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.delete")
    @SLog(tag = "删除回复文本", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyTxtService.delete(StringUtils.split(id, ","));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return wxReplyTxtService.data(length, start, draw, order, columns, cnd, null);
    }
}
