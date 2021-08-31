package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_reply_news;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxReplyNewsService;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.DateUtil;
import com.budwk.app.base.utils.PageUtil;
import com.budwk.app.cms.services.CmsArticleService;
import com.budwk.app.cms.services.CmsChannelService;
import com.budwk.app.web.commons.base.Globals;
import com.budwk.app.web.commons.ext.wx.WxService;
import com.budwk.app.web.commons.slog.annotation.SLog;
import org.apache.commons.lang3.StringUtils;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.nutz.boot.starter.ftp.FtpService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

;

/**
 * Created by Wizzer on 2016/7/5.
 */
@IocBean
@At("/platform/wx/reply/news")
public class WxReplyNewsController {
    private static final Log log = Logs.get();
    @Inject
    private WxReplyNewsService wxReplyNewsService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private CmsChannelService cmsChannelService;
    @Inject
    private CmsArticleService cmsArticleService;
    @Inject
    private WxService wxService;
    @Inject
    private FtpService ftpService;
    @Inject
    private PropertiesProxy conf;

    @At({"/", "/index/?"})
    @Ok("beetl:/platform/wx/reply/news/index.html")
    @SaCheckPermission("wx.reply")
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
    @Ok("json")
    @SaCheckPermission("wx.reply.news.add")
    @SLog(tag = "添加回复图文", msg = "图文标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    //uploadifive上传文件后contentTypy改变,需要用WhaleAdaptor接收参数
    public Object addDo(@Param("..") Wx_reply_news news, HttpServletRequest req) {
        try {
            news.setCreatedBy(SecurityUtil.getUserId());
            wxReplyNewsService.insert(news);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("wx.reply.news.edit")
    public Object edit(String id) {
        try {
            return Result.success().addData(wxReplyNewsService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("wx.reply.news.edit")
    @SLog(tag = "修改回复图文", msg = "图文标题:${args[0].title}")
    @AdaptBy(type = WhaleAdaptor.class)
    //uploadifive上传文件后contentTypy改变,需要用WhaleAdaptor接收参数
    public Object editDo(@Param("..") Wx_reply_news news, HttpServletRequest req) {
        try {
            wxReplyNewsService.updateIgnoreNull(news);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("wx.reply.news.delete")
    @SLog(tag = "删除回复图文", msg = "图文标题:${args[1].getAttribute('title')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", wxReplyNewsService.fetch(id).getTitle());
            wxReplyNewsService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete")
    @Ok("json")
    @SaCheckPermission("wx.reply.news.delete")
    @SLog(tag = "删除回复图文", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String id, HttpServletRequest req) {
        try {
            wxReplyNewsService.delete(StringUtils.split(id, ","));
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("wx.reply")
    public Object data(@Param("wxid") String wxid, @Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
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
        return Result.success().addData(wxReplyNewsService.listPage(pageNumber, pageSize, cnd));
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At("/uploadImage/?")
    @Ok("json")
    @SaCheckPermission("wx.reply")
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
                String suffixName = tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".")).toLowerCase();
                String filePath = Globals.AppUploadBase + "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/";
                String fileName = R.UU32() + suffixName;
                String url = filePath + fileName;
                if (conf.getBoolean("ftp.enabled")) {
                    if (ftpService.upload(filePath, fileName, tf.getInputStream())) {
                        return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("media_id"))
                                .addv("picurl", Strings.sNull(resp.get("url")).replace("http://", "https://"))
                                .addv("picurl2", url));
                    } else {
                        return Result.error("上传失败，请检查ftp用户是否有创建目录权限");
                    }
                } else {
                    String staticPath = conf.get("jetty.staticPath", "/files");
                    Files.write(staticPath + url, tf.getInputStream());
                    return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("media_id"))
                            .addv("picurl", Strings.sNull(resp.get("url")).replace("http://", "https://"))
                            .addv("picurl2", url));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("系统错误");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return Result.error("图片格式错误");
        }
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("wx.reply")
    public Object channel_data(@Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(cmsChannelService.listPage(pageNumber, pageSize, cnd));
    }

    @At
    @Ok("json:full")
    @SaCheckPermission("wx.reply")
    public Object article_data(@Param("searchName") String searchName, @Param("searchKeyword") String searchKeyword, @Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("pageOrderName") String pageOrderName, @Param("pageOrderBy") String pageOrderBy) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(searchName) && !Strings.isBlank(searchKeyword)) {
            cnd.and(searchName, "like", "%" + searchKeyword + "%");
        }
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(cmsArticleService.listPage(pageNumber, pageSize, cnd));
    }

}
