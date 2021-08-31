package com.budwk.app.web.controllers.open.ueditor;

import com.budwk.app.wx.services.WxConfigService;
import com.budwk.app.web.commons.ext.wx.WxService;
import cn.dev33.satoken.annotation.SaCheckLogin;
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
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/wx")
public class WxController {
    private static final Log log = Logs.get();
    @Inject
    private WxConfigService wxConfigService;
    @Inject
    private WxService wxService;

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("raw")
    @SaCheckLogin
    @SuppressWarnings("deprecation")
    public Object uploadimage(@Param("Filedata") TempFile tf, @Param("callback") String callback, HttpServletRequest req, AdaptorErrorContext err) {
        String wxid = Strings.sBlank(req.getSession().getAttribute("wxid"));
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return Json.toJson(nutMap.addv("state", "FAIL"));
            } else if (tf == null) {
                return Json.toJson(nutMap.addv("state", "FAIL"));
            } else {
                WxApi2 wxApi2 = wxService.getWxApi2(wxid);
                WxResp resp = wxApi2.uploadimg(tf.getFile());
                if (resp.errcode() != 0) {
                    return Json.toJson(nutMap.addv("state", "FAIL"));
                }
                nutMap.addv("name", tf.getName());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Strings.sNull(resp.get("url")).replace("http://","https://"));
                nutMap.addv("originalName", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                if (Strings.isBlank(callback)) {
                    return Json.toJson(nutMap);
                } else
                    return "<script>" + callback + "(" + Json.toJson(nutMap) + ")</script>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Json.toJson(nutMap.addv("state", "FAIL"));
        }
    }
}
