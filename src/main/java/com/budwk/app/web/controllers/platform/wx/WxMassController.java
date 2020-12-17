package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_mass;
import com.budwk.app.wx.models.Wx_mass_news;
import com.budwk.app.wx.models.Wx_mass_send;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxMassNewsService;
import com.budwk.app.wx.services.WxMassSendService;
import com.budwk.app.wx.services.WxMassService;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import com.budwk.app.base.utils.DateUtil;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.web.commons.utils.ShiroUtil;
import com.budwk.app.base.result.Result;;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.boot.starter.ftp.FtpService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
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
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/platform/wx/msg/mass")
public class WxMassController {
    private static final Log log = Logs.get();
    @Inject
    private WxMassService wxMassService;
    @Inject
    private WxMassSendService wxMassSendService;
    @Inject
    private WxMassNewsService wxMassNewsService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;
    @Inject
    private FtpService ftpService;
    @Inject
    private PropertiesProxy conf;

    @At({"/", "/?"})
    @Ok("beetl:/platform/wx/msg/mass/index.html")
    @RequiresPermissions("wx.msg.mass")
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
    @Ok("json:full")
    @RequiresPermissions("wx.msg.mass")
    public Object massData(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxMassService.listPageLinks(pageNumber, pageSize, cnd, "massSend"));
    }

    @At("/news/?")
    @Ok("beetl:/platform/wx/msg/mass/news.html")
    @RequiresPermissions("wx.msg.mass")
    public void news(String wxid, HttpServletRequest req, HttpSession session) {
        req.setAttribute("wxid", wxid);
        session.setAttribute("wxid", wxid);
    }

    @At("/newsData/?")
    @Ok("json:full")
    @RequiresPermissions("wx.msg.mass")
    public Object newsData(String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(wxid)) {
            cnd.and("wxid", "=", wxid);
        }
        if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        return Result.success().addData(wxMassNewsService.listPage(pageNumber, pageSize, cnd));
    }

    @At("/deleteNews/?")
    @Ok("json")
    @RequiresPermissions("wx.msg.mass.delNews")
    @SLog(tag = "删除图文", msg = "图文标题:${args[1].getAttribute('title')}}")
    public Object deleteNews(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxMassNewsService.fetch(id).getTitle());
            wxMassNewsService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }


    @At
    @Ok("json")
    @RequiresPermissions("wx.msg.mass.addNews")
    @SLog(tag = "添加图文", msg = "图文标题:${args[0].title}")
    public Object addDo(@Param("..") Wx_mass_news news, HttpServletRequest req) {
        try {
            news.setCreatedBy(ShiroUtil.getPlatformUid());
            wxMassNewsService.insert(news);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/newsDetail/?")
    @Ok("json")
    @RequiresPermissions("wx.msg.mass")
    public Object newsDetail(String id, HttpServletRequest req) {
        try {
            return Result.success().addData(wxMassNewsService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
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
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                if (conf.getBoolean("ftp.enabled")) {
                    if (ftpService.upload(filePath, fileName, tf.getInputStream())) {
                        return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("thumb_media_id"))
                                .addv("picurl", url));
                    } else {
                        return Result.error("上传失败，请检查ftp用户是否有创建目录权限");
                    }
                } else {
                    String staticPath = conf.get("jetty.staticPath", "/files");
                    Files.write(staticPath + url, tf.getInputStream());
                    return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("thumb_media_id"))
                            .addv("picurl", url));
                }
            }
        } catch (Exception e) {
            return Result.error("系统错误");
        } catch (Throwable e) {
            return Result.error("图片格式错误");
        }
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/uploadImage/?")
    @Ok("json")
    @RequiresPermissions("wx.msg.mass")
    @SuppressWarnings("deprecation")
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadImage(String wxid, @Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return NutMap.NEW().addv("code", 1).addv("msg", "文件不合法");
            } else if (tf == null) {
                return Result.error("空文件");
            } else {
                WxApi2 wxApi2 = wxService.getWxApi2(wxid);
                WxResp resp = wxApi2.add_material("image", tf.getFile());
                if (resp.errcode() != 0) {
                    return Result.error(resp.errmsg());
                }
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".") + 1).toLowerCase();
                String filePath = Globals.AppUploadBase + "/image/" + DateUtil.format(new Date(), "yyyyMMdd");
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                if (conf.getBoolean("ftp.enabled")) {
                    ftpService.upload(filePath, fileName, tf.getInputStream());
                } else {
                    String staticPath = conf.get("jetty.staticPath", "/files");
                    Files.write(staticPath + url, tf.getInputStream());
                }
                return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("media_id"))
                        .addv("picurl", url));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
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
    public Object sendDo(@Param("..") Wx_mass mass, @Param("receivers") String openids, @Param("newsids") String newsids, HttpServletRequest req) {
        try {
            WxApi2 wxApi2 = wxService.getWxApi2(mass.getWxid());
            WxOutMsg outMsg = new WxOutMsg();
            if ("news".equals(mass.getType())) {
                String[] ids = StringUtils.split(newsids, ",");
                int i = 0;
                for (String id : ids) {
                    wxMassNewsService.update(org.nutz.dao.Chain.make("location", i), Cnd.where("id", "=", id));
                    i++;
                }
                List<Wx_mass_news> newsList = wxMassNewsService.query(Cnd.where("id", "in", ids).asc("location"));
                List<WxMassArticle> articles = Json.fromJsonAsList(WxMassArticle.class, Json.toJson(newsList));
                WxResp resp = wxApi2.uploadnews(articles);
                log.debug(resp);
                String media_id = resp.media_id();
                mass.setMedia_id(media_id);
                outMsg.setMedia_id(media_id);
                outMsg.setMsgType("mpnews");
            }
            if ("text".equals(mass.getType())) {
                outMsg.setContent(mass.getContent());
                outMsg.setMsgType("text");
            }
            if ("image".equals(mass.getType())) {
                outMsg.setMedia_id(mass.getMedia_id());
                outMsg.setMsgType("image");
            }
            WxResp resp;
            if ("all".equals(mass.getScope())) {
                resp = wxApi2.mass_sendall(true, null, outMsg);
            } else {
                String[] ids = StringUtils.split(openids, ",");
                resp = wxApi2.mass_send(Arrays.asList(ids), outMsg);
            }
            log.debug(resp);
            int status = resp.errcode() == 0 ? 1 : 2;
            String errmsg = resp.getString("errmsg");
            if (status != 1) {
                return Result.error(errmsg);
            }
            mass.setStatus(resp.errcode() == 0 ? 1 : 2);
            Wx_mass wxMass = wxMassService.insert(mass);
            Wx_mass_send send = new Wx_mass_send();
            send.setWxid(wxMass.getWxid());
            send.setMassId(wxMass.getId());
            send.setErrCode(String.valueOf(resp.errcode()));
            send.setMsgId(resp.getString("msg_id"));
            if (!"all".equals(mass.getScope())) {
                send.setReceivers(openids);
            }
            send.setErrMsg(errmsg);
            send.setStatus(status);
            wxMassSendService.insert(send);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
