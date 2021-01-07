package com.budwk.app.web.controllers.front.wx;

import com.budwk.app.base.result.Result;
import com.budwk.app.cms.services.CmsArticleService;
import com.budwk.app.cms.services.CmsChannelService;
import com.budwk.app.cms.services.CmsSiteService;
import com.budwk.app.wx.services.WxConfigService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by wizzer on 2016/8/20.
 */
@IocBean
@At("/public/wx/cms")
public class WxCmsController {
    private static final Log log = Logs.get();
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private CmsSiteService cmsSiteService;
    @Inject
    private CmsChannelService cmsChannelService;
    @Inject
    private CmsArticleService cmsArticleService;

    @At({"/channel/?", "/channel/"})
    @Ok("beetl:/public/wx/cms/channel.html")
    public Object channel(String channelId, HttpServletRequest req) {
        req.setAttribute("channelId", channelId);
        req.setAttribute("site", cmsSiteService.fetch("site"));
        return cmsChannelService.query(Cnd.where("disabled", "=", false).asc("location"));
    }

    @At("/list/?")
    @Ok("json")
    public Object list(String channelId, HttpServletRequest req) {
        try {
            Cnd cnd = Cnd.NEW();
            if (!"all".equals(channelId)) {
                cnd.and("channelId", "=", channelId);
            }
            return Result.success("", cmsArticleService.query("^(id|title|info|picurl)$", cnd.desc("publishAt")));
        } catch (Exception e) {
            log.error(e);
            return Result.error("");
        }
    }

    @At("/article/?")
    @Ok("beetl:/public/wx/cms/article.html")
    public Object article(String id, HttpServletRequest req) {
        return cmsArticleService.fetch(id);
    }

    @At("/test")
    @Ok("json")
    public Object test(HttpServletRequest req, HttpSession session) {
        log.debug("session::::" + Strings.sNull(session.getAttribute("openid")));
        return Result.success("wahhhahhaha");
    }
}
