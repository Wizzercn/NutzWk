package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.app.wx.modules.models.Wx_reply_news;
import cn.wizzer.app.wx.modules.services.WxReplyNewsService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/platform/wx/reply/news")
public class WxReplyNewsController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxReplyNewsService wxReplyNewsService;

    @At("")
    @Ok("beetl:/platform/wx/reply/news/index.html")
    @RequiresPermissions("wx.reply")
    public void index() {

    }

    @At
    @Ok("beetl:/platform/wx/reply/news/add.html")
    @RequiresPermissions("wx.reply")
    public void add() {

    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.news.add")
    @SLog(tag = "添加回复图文", msg = "图文标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    //uploadifive上传文件后contentTypy改变,需要用WhaleAdaptor接收参数
    public Object addDo(@Param("..") Wx_reply_news news, HttpServletRequest req) {
        try {
            news.setOpBy(StringUtil.getPlatformUid());
            wxReplyNewsService.insert(news);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/wx/reply/news/edit.html")
    @RequiresPermissions("wx.reply")
    public Object edit(String id) {
        return wxReplyNewsService.fetch(id);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.news.edit")
    @SLog(tag = "修改回复图文", msg = "图文标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    //uploadifive上传文件后contentTypy改变,需要用WhaleAdaptor接收参数
    public Object editDo(@Param("..") Wx_reply_news news, HttpServletRequest req) {
        try {
            news.setOpBy(StringUtil.getPlatformUid());
            news.setOpAt(Times.getTS());
            wxReplyNewsService.updateIgnoreNull(news);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("wx.reply.news.delete")
    @SLog(tag = "删除回复图文", msg = "图文标题:${args[1].getAttribute('title')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxReplyNewsService.fetch(id).getTitle());
            wxReplyNewsService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("wx.reply.news.delete")
    @SLog(tag = "删除回复图文", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyNewsService.delete(StringUtils.split(id, ","));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wx.reply")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return wxReplyNewsService.data(length, start, draw, order, columns, cnd, null);
    }
}
