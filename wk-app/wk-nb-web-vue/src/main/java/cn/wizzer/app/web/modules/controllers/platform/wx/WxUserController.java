package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.ext.wx.WxService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_user;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxUserService;
import cn.wizzer.framework.base.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.vdurmont.emoji.EmojiParser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/8.
 */
@IocBean
@At("/platform/wx/user")
public class WxUserController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxUserService wxUserService;
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @At({"/index", "/index/?"})
    @Ok("beetl:/platform/wx/user/index.html")
    @RequiresPermissions("wx.user.list")
    public void index(String wxid, HttpServletRequest req) {
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
    @Ok("json:full")
    @RequiresPermissions("wx.user.list")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        try {
            Cnd cnd = Cnd.NEW();
            if (!Strings.isBlank(wxid)) {
                cnd.and("wxid", "=", wxid);
            }
            if (Strings.isNotBlank(searchName) && !Strings.isNotBlank(searchKeyword)) {
                cnd.and(searchName, "like", "%" + searchKeyword + "%");
            }
            return Result.success().addData(wxUserService.listPage(pageNumber, pageSize, cnd));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At({"/down/", "/down/?"})
    @Ok("json")
    @RequiresPermissions("wx.user.list.sync")
    @SLog(tag = "同步微信会员", msg = "公众号:${args[1].getAttribute('appname')}")
    public Object down(String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            req.setAttribute("appname", config.getAppname());
            WxApi2 wxApi2 = wxService.getWxApi2(wxid);
            wxApi2.user_get(new Each<String>() {
                public void invoke(int index, String _ele, int length)
                        throws ExitLoop, ContinueLoop, LoopException {
                    WxResp resp = wxApi2.user_info(_ele, "zh_CN");
                    Wx_user usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
                    usr.setOpAt(Times.getTS());
                    usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
                    usr.setSubscribeAt(resp.user().getSubscribe_time());
                    usr.setWxid(wxid);
                    if (wxUserService.fetch(Cnd.where("wxid", "=", wxid).and("openid", "=", usr.getOpenid())) == null)
                        wxUserService.insert(usr);
                }
            });
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error();
        }
    }
}
