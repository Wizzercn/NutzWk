package cn.wizzer.modules.back.wx.controllers;

import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.base.Result;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.common.page.DataTableColumn;
import cn.wizzer.common.page.DataTableOrder;
import cn.wizzer.modules.back.wx.models.Wx_config;
import cn.wizzer.modules.back.wx.models.Wx_user;
import cn.wizzer.modules.back.wx.services.WxConfigService;
import cn.wizzer.modules.back.wx.services.WxMenuService;
import cn.wizzer.modules.back.wx.services.WxUserService;
import com.vdurmont.emoji.EmojiParser;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.*;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

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
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
    }

    @At({"/data/","/data/?"})
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

    @At("/down/?")
    @Ok("json")
    @RequiresPermissions("wx.user.list.sync")
    @SLog(tag = "同步微信会员", msg = "公众号:${args[1].getAttribute('appname')}")
    public Object down(String wxid, HttpServletRequest req) {
        try {
            Wx_config config = wxConfigService.fetch(wxid);
            req.setAttribute("appname", config.getAppname());
            WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
            wxApi2.user_get(new Each<String>() {
                public void invoke(int index, String _ele, int length)
                        throws ExitLoop, ContinueLoop, LoopException {
                    WxResp resp = wxApi2.user_info(_ele, "zh_CN");
                    log.info(Json.toJson(resp));
                    log.debug(index
                            + " : "
                            + _ele
                            + ", nickname: "
                            + resp.user().getNickname());
                    Wx_user usr = Json.fromJson(Wx_user.class, Json.toJson(resp.user()));
                    usr.setOpAt((int) (System.currentTimeMillis() / 1000));
                    usr.setNickname(EmojiParser.parseToAliases(usr.getNickname(), EmojiParser.FitzpatrickAction.REMOVE));
                    usr.setSubscribeAt((int) (resp.user().getSubscribe_time()));
                    usr.setWxid(wxid);
                    if (wxUserService.fetch(Cnd.where("wxid", "=", wxid).and("openid", "=", usr.getOpenid())) == null)
                        wxUserService.insert(usr);
                }
            });
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
