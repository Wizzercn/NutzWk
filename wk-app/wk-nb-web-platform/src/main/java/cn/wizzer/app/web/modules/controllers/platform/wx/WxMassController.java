package cn.wizzer.app.web.modules.controllers.platform.wx;

import cn.wizzer.app.web.commons.ext.wx.WxService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.app.wx.modules.models.Wx_config;
import cn.wizzer.app.wx.modules.models.Wx_mass;
import cn.wizzer.app.wx.modules.models.Wx_mass_news;
import cn.wizzer.app.wx.modules.models.Wx_mass_send;
import cn.wizzer.app.wx.modules.services.WxConfigService;
import cn.wizzer.app.wx.modules.services.WxMassNewsService;
import cn.wizzer.app.wx.modules.services.WxMassSendService;
import cn.wizzer.app.wx.modules.services.WxMassService;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.weixin.bean.WxMassArticle;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/platform/wx/msg/mass")
public class WxMassController {
    private static final Log log = Logs.get();
    @Inject
    @Reference
    private WxMassService wxMassService;
    @Inject
    @Reference
    private WxMassSendService wxMassSendService;
    @Inject
    @Reference
    private WxMassNewsService wxMassNewsService;
    @Inject
    @Reference
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @At({"/", "/?"})
    @Ok("beetl:/platform/wx/msg/mass/index.html")
    @RequiresPermissions("wx.msg.mass")
    public void index(String wxid, HttpServletRequest req) {
        List<Wx_config> list = wxConfigService.query(Cnd.NEW());
        if (list.size() > 0 && Strings.isBlank(wxid)) {
            wxid = list.get(0).getId();
        }
        req.setAttribute("wxid", wxid);
        req.setAttribute("wxList", list);
    }

    @At({"/massData/", "/massData/?"})
    @Ok("json:full")
    @RequiresPermissions("wx.msg.mass")
    public Object massData(String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxMassService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/news/?")
    @Ok("beetl:/platform/wx/msg/mass/news.html")
    @RequiresPermissions("wx.msg.mass")
    public void news(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
    }

    @At("/newsData/?")
    @Ok("json:full")
    @RequiresPermissions("wx.msg.mass")
    public Object newsData(String wxid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        return wxMassNewsService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/deleteNews/?")
    @Ok("json")
    @RequiresPermissions("wx.msg.mass.delNews")
    @SLog(tag = "删除图文", msg = "图文标题:${args[1].getAttribute('title')}}")
    public Object deleteNews(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxMassNewsService.fetch(id).getTitle());
            wxMassNewsService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/addNews/?")
    @Ok("beetl:/platform/wx/msg/mass/add.html")
    @RequiresPermissions("wx.msg.mass")
    public void add(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
        req.getSession().setAttribute("wxid", wxid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.msg.mass.addNews")
    @SLog(tag = "添加图文", msg = "图文标题:${args[0].title}")
    public Object addDo(@Param("..") Wx_mass_news news, HttpServletRequest req) {
        try {
            wxMassNewsService.insert(news);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/newsDetail/?")
    @Ok("beetl:/platform/wx/msg/mass/detail.html")
    @RequiresPermissions("wx.msg.mass")
    public Object newsDetail(String id, HttpServletRequest req) {
        return wxMassNewsService.fetch(id);
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/uploadThumb/?")
    @Ok("json")
    @RequiresPermissions("wx.msg.mass")
    @SuppressWarnings("deprecation")
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadThumb(String wxid, @Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {

                WxApi2 wxApi2 = wxService.getWxApi2(wxid);
                WxResp resp = wxApi2.media_upload("thumb", tf.getFile());
                if (resp.errcode() != 0) {
                    return Result.error(resp.errmsg());
                }
                return Result.success("上传成功", resp.get("thumb_media_id"));
            }
        } catch (Exception e) {
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("图片格式错误");
        }
    }

    @At("/send/?")
    @Ok("beetl:/platform/wx/msg/mass/send.html")
    @RequiresPermissions("wx.msg.mass")
    public void send(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
    }

    @At("/select/?")
    @Ok("beetl:/platform/wx/msg/mass/select.html")
    @RequiresPermissions("wx.msg.mass")
    public void select(String wxid, HttpServletRequest req) {
        req.setAttribute("wxid", wxid);
    }

    @At
    @Ok("json")
    @RequiresPermissions("wx.msg.mass.pushNews")
    @SLog(tag = "群发消息", msg = "群发名称:${args[0].name}")
    public Object sendDo(@Param("..") Wx_mass mass, @Param("content") String content, @Param("openids") String openids, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxService.getWxApi2(mass.getWxid());
            WxOutMsg outMsg = new WxOutMsg();
            if ("news".equals(mass.getType())) {
                String[] ids = StringUtils.split(content, ",");
                int i = 0;
                for (String id : ids) {
                    wxMassNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
                List<Wx_mass_news> newsList = wxMassNewsService.query(Cnd.where("id", "in", ids).asc("location"));
                List<WxMassArticle> articles = Json.fromJsonAsList(WxMassArticle.class, Json.toJson(newsList));
                WxResp resp = wxApi2.uploadnews(articles);
                String media_id = resp.media_id();
                mass.setMedia_id(media_id);
                outMsg.setMedia_id(media_id);
                outMsg.setMsgType("mpnews");
            }
            if ("text".equals(mass.getType())) {
                outMsg.setContent(content);
                outMsg.setMsgType("text");
            }
            WxResp resp;
            if ("all".equals(mass.getScope())) {
                resp = wxApi2.mass_sendall(true, null, outMsg);
            } else {
                String[] ids = StringUtils.split(openids, ",");
                resp = wxApi2.mass_send(Arrays.asList(ids), outMsg);
            }
            mass.setStatus(resp.errcode() == 0 ? 1 : 2);
            Wx_mass wxMass = wxMassService.insert(mass);
            Wx_mass_send send = new Wx_mass_send();
            send.setWxid(wxMass.getWxid());
            send.setMassId(wxMass.getId());
            send.setErrCode(String.valueOf(resp.errcode()));
            send.setMsgId(resp.getString("msg_id"));
            if (!"all".equals(mass.getScope())) {
                send.setReceivers(content);
            }
            send.setErrMsg(resp.getString("errmsg"));
            send.setStatus(resp.errcode() == 0 ? 1 : 2);
            wxMassSendService.insert(send);
            return Result.success("system.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
    }

    @At("/sendDetail/?")
    @Ok("beetl:/platform/wx/msg/mass/sendDetail.html")
    @RequiresPermissions("wx.msg.mass")
    public Object sendDetail(String id, HttpServletRequest req) {
        Wx_mass mass = wxMassService.fetch(id);
        if ("news".equals(mass.getType())) {
            String[] ids = StringUtils.split(mass.getContent(), ",");
            req.setAttribute("news", wxMassNewsService.query(Cnd.where("id", "in", ids).asc("location")));
        }
        req.setAttribute("send", wxMassSendService.fetch(Cnd.where("massId", "=", mass.getId())));
        return mass;
    }
}
