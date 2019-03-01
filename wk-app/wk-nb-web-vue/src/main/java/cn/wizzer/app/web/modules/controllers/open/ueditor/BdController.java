package cn.wizzer.app.web.modules.controllers.open.ueditor;

import cn.wizzer.app.web.commons.base.Globals;
import cn.wizzer.app.web.commons.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

/**
 * Created by Wizzer on 2016/7/9.
 */
@IocBean
@At("/open/ueditor/bd")
public class BdController {

    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:imageUpload"})
    @POST
    @At
    @Ok("raw")
    @RequiresAuthentication
    //AdaptorErrorContext必须是最后一个参数
    public Object uploadimage(@Param("Filedata") TempFile tf, @Param("callback") String callback, HttpServletRequest req, AdaptorErrorContext err) {
        NutMap nutMap = new NutMap();
        try {
            if (err != null && err.getAdaptorErr() != null) {
                return Json.toJson(nutMap.addv("state", "FAIL"));
            } else if (tf == null) {
                return Json.toJson(nutMap.addv("state", "FAIL"));
            } else {
                String uri = "/image/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + R.UU32() + tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf("."));
                String f = Globals.AppUploadPath + uri;
                Files.write(new File(f), tf.getInputStream());
                nutMap.addv("name", tf.getName());
                nutMap.addv("state", "SUCCESS");
                nutMap.addv("url", Globals.AppUploadBase + uri);
                nutMap.addv("originalName", tf.getSubmittedFileName());
                nutMap.addv("type", tf.getSubmittedFileName().substring(tf.getSubmittedFileName().lastIndexOf(".") + 1));
                nutMap.addv("size", tf.getSize());
                if (Strings.isBlank(callback)) {
                    return Json.toJson(nutMap);
                } else
                    return "<script>" + callback + "(" + Json.toJson(nutMap) + ")</script>";
            }
        } catch (Exception e) {
            return Json.toJson(nutMap.addv("state", "FAIL"));
        } catch (Throwable e) {
            return Json.toJson(nutMap.addv("state", "FAIL"));
        }
    }

}
