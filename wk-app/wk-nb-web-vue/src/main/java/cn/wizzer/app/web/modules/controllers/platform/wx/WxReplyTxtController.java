package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.web.commons.utils.PageUtil;
import cn.wizzer.app.web.commons.utils.StringUtil;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_reply_txt;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxReplyTxtService;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
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
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/platform/wx/reply/txt")
public class WxReplyTxtController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxReplyTxtService wxReplyTxtService;
    @Inject
    @Reference
    private WxConfigService wxConfigService;

    @At({"/", "/?"})
    @Ok("beetl:/platform/wx/reply/txt/index.html")
    @RequiresPermissions("wx.reply")
    public void index(String wxid, HttpServletRequest req, HttpSession session) {
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
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.add")
    @SLog(tag = "添加回复文本", msg = "文本标题:${args[0].title}")
    public Object addDo(@Param("..") Wx_reply_txt txt, HttpServletRequest req) {
        try {
            txt.setOpBy(StringUtil.getPlatformUid());
            wxReplyTxtService.insert(txt);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @RequiresPermissions("wx.reply")
    public Object edit(String id) {
        try {
            return Result.success().addData(wxReplyTxtService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.edit")
    @SLog(tag = "修改回复文本", msg = "文本标题:${args[0].title}")
    public Object editDo(@Param("..") Wx_reply_txt txt, HttpServletRequest req) {
        try {
            wxReplyTxtService.updateIgnoreNull(txt);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
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
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions("wx.reply.txt.delete")
    @SLog(tag = "删除回复文本", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String ids, HttpServletRequest req) {
        try {
            wxReplyTxtService.delete(StringUtils.split(ids, ","));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("wx.reply")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxReplyTxtService.listPage(pageNumber, pageSize, cnd));
    }
}
