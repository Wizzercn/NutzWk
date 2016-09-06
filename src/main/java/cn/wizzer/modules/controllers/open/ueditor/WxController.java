package cn.wizzer.modules.controllers.open.ueditor;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.services.wx.WxConfigService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
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
@Filters({@By(type = PrivateFilter.class)})
public class WxController {
    private static final Log log = Logs.get();
    @Inject
    WxConfigService wxConfigService;


    @At
    @Ok("json")
    public Object index(@Param("action") String action, HttpServletRequest req) {
        return Json.fromJson(Files.read(Globals.AppRoot + "/assets/plugins/ueditor/nutz/configWx.json").replace("$base", Globals.AppBase));
    }

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("json")
    @RequiresAuthentication
    public Object uploadimage(@Param("Filedata") TempFile tf, HttpServletRequest req, AdaptorErrorContext err) {
        String wxid = Strings.sBlank(req.getSession().getAttribute("wxid"));
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                nutMap.addv("state", "FAIL");
                return nutMap;
            } else if (tf == null) {
                nutMap.addv("state", "FAIL");
                return nutMap;
            } else {
                WxApi2 wxApi2 = wxConfigService.getWxApi2(wxid);
                WxResp resp = wxApi2.uploadimg(tf.getFile());
                if (resp.errcode() != 0) {
                    nutMap.addv("state", "FAIL");
                    return nutMap;
                }
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", resp.get("url"));
                nutMap.addv("original", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().indexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                return nutMap;
            }
        } catch (Exception e) {
            nutMap.addv("state", "FAIL");
            return nutMap;
        } catch (Throwable e) {
            nutMap.addv("state", "FAIL");
            return nutMap;
        }
    }
}
