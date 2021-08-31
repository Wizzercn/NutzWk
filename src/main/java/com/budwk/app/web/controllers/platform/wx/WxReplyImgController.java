package com.budwk.app.web.controllers.platform.wx;

import com.budwk.app.web.commons.auth.utils.SecurityUtil;
import com.budwk.app.wx.models.Wx_config;
import com.budwk.app.wx.models.Wx_reply_img;
import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.wx.services.WxReplyImgService;
import com.budwk.app.base.result.Result;
import com.budwk.app.base.utils.DateUtil;
import com.budwk.app.base.utils.PageUtil;
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
@At("/platform/wx/reply/img")
public class WxReplyImgController {
    private static final Log log = Logs.get();
    @Inject
    private WxReplyImgService wxReplyImgService;
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;
    @Inject
    private FtpService ftpService;
    @Inject
    private PropertiesProxy conf;

    @At({"/", "/index/?"})
    @Ok("beetl:/platform/wx/reply/img/index.html")
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
    @SaCheckPermission("wx.reply.img.add")
    @SLog(tag = "添加回复图片", msg = "图片路径:${args[0].picurl}")
    public Object addDo(@Param("..") Wx_reply_img img, HttpServletRequest req) {
        try {
            img.setCreatedBy(SecurityUtil.getUserId());
            wxReplyImgService.insert(img);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/edit/?")
    @Ok("json")
    @SaCheckPermission("wx.reply")
    public Object edit(String id) {
        try {
            return Result.success().addData(wxReplyImgService.fetch(id));
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At
    @Ok("json")
    @SaCheckPermission("wx.reply.img.edit")
    @SLog(tag = "修改回复图片", msg = "图片路径:${args[0].picurl}")
    public Object editDo(@Param("..") Wx_reply_img img, HttpServletRequest req) {
        try {
            wxReplyImgService.updateIgnoreNull(img);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete/?")
    @Ok("json")
    @SaCheckPermission("wx.reply.img.delete")
    @SLog(tag = "删除回复图片", msg = "图片路径:${args[1].getAttribute('picurl')}}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            req.setAttribute("picurl", wxReplyImgService.fetch(id).getPicurl());
            wxReplyImgService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @At("/delete")
    @Ok("json")
    @SaCheckPermission("wx.reply.img.delete")
    @SLog(tag = "删除回复图片", msg = "ID:${args[0]}")
    public Object deletes(@Param("ids") String ids, HttpServletRequest req) {
        try {
            wxReplyImgService.delete(StringUtils.split(ids, ","));
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
        if (Strings.isNotBlank(pageOrderName) && Strings.isNotBlank(pageOrderBy)) {
            cnd.orderBy(pageOrderName, PageUtil.getOrder(pageOrderBy));
        }
        return Result.success().addData(wxReplyImgService.listPage(pageNumber, pageSize, cnd));
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
                                .addv("picurl", url));
                    } else {
                        return Result.error("上传失败，请检查ftp用户是否有创建目录权限");
                    }
                } else {
                    String staticPath = conf.get("jetty.staticPath", "/files");
                    Files.write(staticPath + url, tf.getInputStream());
                    return Result.success("上传成功", NutMap.NEW().addv("id", resp.get("media_id"))
                            .addv("picurl", url));
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
}
